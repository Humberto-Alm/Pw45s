package br.edu.utfpr.pb.pw45s.server.controller;

import br.edu.utfpr.pb.pw45s.server.dto.OrderItensDTO;
import br.edu.utfpr.pb.pw45s.server.mapper.OrderItensMapper;
import br.edu.utfpr.pb.pw45s.server.model.OrderItens;
import br.edu.utfpr.pb.pw45s.server.service.AuthService;
import br.edu.utfpr.pb.pw45s.server.service.ICrudService;
import br.edu.utfpr.pb.pw45s.server.service.IOrderItensService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("orders_itens")
public class OrderItensController extends CrudController<OrderItens, OrderItensDTO, Long> {
    private final IOrderItensService orderItensService;
    private final OrderItensMapper itensMapper;


    public OrderItensController(IOrderItensService orderItensService, OrderItensMapper itensMapper, AuthService authService) {
        this.orderItensService = orderItensService;
        this.itensMapper = itensMapper;
    }

    @Override
    protected ICrudService<OrderItens, Long> getService() {
        return orderItensService;
    }

    @Override
    protected OrderItensDTO toDto(OrderItens entity) {
        return itensMapper.toDto(entity);
    }

    @Override
    protected OrderItens toEntity(OrderItensDTO dto) {
        return itensMapper.toEntity(dto);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItensDTO>> getItensByOrder(@PathVariable Long orderId) {
        List<OrderItens> orderItens = orderItensService.findAllByOrderId(orderId);
        List<OrderItensDTO> orderItensDTO = orderItens.stream()
                .map(orderItem -> itensMapper.toDto(orderItem))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderItensDTO);
    }

}