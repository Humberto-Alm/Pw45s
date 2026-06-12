package br.edu.utfpr.pb.pw45s.server.controller;

import br.edu.utfpr.pb.pw45s.server.dto.*;
import br.edu.utfpr.pb.pw45s.server.mapper.AddressMapper;
import br.edu.utfpr.pb.pw45s.server.mapper.OrderMapper;
import br.edu.utfpr.pb.pw45s.server.mapper.ProductMapper;
import br.edu.utfpr.pb.pw45s.server.minio.service.MinioService;
import br.edu.utfpr.pb.pw45s.server.model.*;
import br.edu.utfpr.pb.pw45s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw45s.server.service.AuthService;
import br.edu.utfpr.pb.pw45s.server.service.ICrudService;
import br.edu.utfpr.pb.pw45s.server.service.IOrderItensService;
import br.edu.utfpr.pb.pw45s.server.service.IOrderService;
import cn.hutool.core.io.resource.InputStreamResource;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("orders")
public class OrderController extends CrudController<Order, OrderDTO, Long> {
    private final IOrderService orderService;
    private final IOrderItensService orderItensService;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final AuthService authService;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
    private final MinioService minioService;

    private static final String BUCKET_COMPROVANTES = "comprovantes";

    public OrderController(IOrderService orderService,
                           IOrderItensService orderItensService,
                           OrderMapper orderMapper,
                           AuthService authService,
                           OrderRepository orderRepository,
                           AddressMapper addressMapper,
                           ProductMapper productMapper,
                           MinioService minioService) {
        this.orderService = orderService;
        this.orderItensService = orderItensService;
        this.orderMapper = orderMapper;
        this.authService = authService;
        this.orderRepository = orderRepository;
        this.addressMapper = addressMapper;
        this.productMapper = productMapper;
        this.minioService = minioService;
    }

    @Override
    protected ICrudService<Order, Long> getService() {
        return orderService;
    }

    @Override
    protected OrderDTO toDto(Order entity) {
        return orderMapper.toDto(entity);
    }

    @Override
    protected Order toEntity(OrderDTO dto) {
        return orderMapper.toEntity(dto);
    }

    @GetMapping("/my_orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders() {
        User user = authService.getAuthenticatedUser();
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByStatus(
            @PathVariable OrderStatus status) {

        Long userId = authService.getAuthenticatedUserId();
        List<Order> orders = orderService.findAllByUserIdAndStatus(userId, status);

        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{username}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByClient(
            @PathVariable String username) {

        List<Order> orders = orderService.findAllByUserUsername(username);

        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        Long userId = authService.getAuthenticatedUserId();
        List<Order> orders = orderService.findAllByUserIdAndDataBetween(userId, start, end);

        List<OrderResponseDTO> response = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();

        Long userId = authService.getAuthenticatedUserId();
        if (order.getUser().getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    // Método auxiliar para converter Order -> OrderResponseDTO
    private OrderResponseDTO convertToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setData(order.getData());
        dto.setTotal(order.getTotalOrder());
        dto.setFreight(order.getFreight());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus());
        dto.setStatusDescricao(order.getStatus().getDescricao());

        if (order.getAddress() != null) {
            dto.setAddress(addressMapper.toDto(order.getAddress()));
        }

        if (order.getItems() != null) {
            List<OrderItensResponseDTO> itemsDto = order.getItems().stream().map(item -> {
                OrderItensResponseDTO iDto = new OrderItensResponseDTO();
                iDto.setId(item.getId());
                iDto.setQuantity((int) item.getQuantity());

                iDto.setUnit_price(item.getUnit_price());

                if (item.getProduct() != null) {
                    iDto.setProduct(productMapper.toDto(item.getProduct()));
                }
                return iDto;
            }).collect(Collectors.toList());
            dto.setItems(itemsDto);
        }

        return dto;
    }

    // Criar pedido
    @Override
    @PostMapping
    @Transactional
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO orderDTO) {
        User authenticatedUser = authService.getAuthenticatedUser();

        Order order = new Order();
        order.setStatus(OrderStatus.AGUARDANDO_PAGAMENTO);
        order.setData(java.time.LocalDateTime.now());
        order.setUser(authenticatedUser);

        order.setAddress(addressMapper.toEntity(orderDTO.getAddress()));
        order.setFreight(orderDTO.getFreight());
        order.setPaymentMethod(orderDTO.getPaymentMethod());

        order = orderService.save(order);

        if (orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            Order finalOrder = order;
            List<OrderItens> orderItensList = orderDTO.getItems().stream()
                    .map(itemDTO -> {
                        OrderItens item = new OrderItens();
                        item.setOrder(finalOrder);

                        Product product = new Product();
                        product.setId(itemDTO.getProductId());
                        item.setProduct(product);
                        item.setQuantity((double) itemDTO.getQuantity());

                        item.setUnit_price(itemDTO.getUnit_price());
                        return item;
                    })
                    .collect(Collectors.toList());

            orderItensService.saveAll(orderItensList);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(order));
    }

    @PostMapping("/{id}/pago")
    public ResponseEntity<OrderResponseDTO> receberPagamento(@PathVariable Long id) {
        Long userId = authService.getAuthenticatedUserId();
        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        if (order.getUser().getId() != (userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Order updated = orderService.updateStatus(id, OrderStatus.PAGO);
        return ResponseEntity.ok(convertToResponseDTO(updated));
    }

    @PostMapping("/{id}/enviado")
    public ResponseEntity<OrderResponseDTO> receberEnvio(@PathVariable Long id) {
        Long userId = authService.getAuthenticatedUserId();
        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        if (order.getUser().getId() != (userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Order updated = orderService.updateStatus(id, OrderStatus.EM_TRANSPORTE);
        return ResponseEntity.ok(convertToResponseDTO(updated));
    }

    @PostMapping("/{id}/concluido")
    public ResponseEntity<OrderResponseDTO> receberConclusao(@PathVariable Long id) {
        Long userId = authService.getAuthenticatedUserId();
        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        if (order.getUser().getId() != (userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Order updated = orderService.updateStatus(id, OrderStatus.CONCLUIDO);
        return ResponseEntity.ok(convertToResponseDTO(updated));
    }

    @PostMapping(value = "/{id}/comprovante", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderResponseDTO> uploadComprovante(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();

        if (order.getUser().getId() != authService.getAuthenticatedUserId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try {
            Order updated = orderService.uploadComprovante(id, file);
            return ResponseEntity.ok(convertToResponseDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}/comprovante")
    public ResponseEntity<InputStreamResource> downloadComprovante(@PathVariable Long id) {
        Order order = orderService.findOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();

        if (order.getUser().getId() != authService.getAuthenticatedUserId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (order.getComprovantePdf() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        InputStream stream = minioService.downloadObject(BUCKET_COMPROVANTES, order.getComprovantePdf());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"comprovante-pedido-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(stream));
    }
}