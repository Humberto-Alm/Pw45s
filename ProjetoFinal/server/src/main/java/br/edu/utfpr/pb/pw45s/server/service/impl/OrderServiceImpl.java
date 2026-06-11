package br.edu.utfpr.pb.pw45s.server.service.impl;

import br.edu.utfpr.pb.pw45s.server.model.Order;
import br.edu.utfpr.pb.pw45s.server.model.OrderStatus;
import br.edu.utfpr.pb.pw45s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw45s.server.service.IOrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends CrudServiceImpl<Order, Long> implements IOrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return orderRepository.findAllByUserId(id);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findAllByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> findAllByUserUsername(String username) {
        return orderRepository.findAllByUserUsername(username);
    }


    @Override
    public List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status) {
        return orderRepository.findAllByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Order> findAllByDataBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAllByDataBetween(start, end);
    }

    @Override
    public List<Order> findAllByUserIdAndDataBetween(Long userId, LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAllByUserIdAndDataBetween(userId, start, end);
    }

    @Override
    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }


}