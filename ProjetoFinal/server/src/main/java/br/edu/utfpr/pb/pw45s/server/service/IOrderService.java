package br.edu.utfpr.pb.pw45s.server.service;

import br.edu.utfpr.pb.pw45s.server.model.Order;
import br.edu.utfpr.pb.pw45s.server.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService extends ICrudService<Order, Long> {

    List<Order> findAllByUserId(Long userId);
    Order findOrderById(Long id);
    Order updateStatus(Long orderId, OrderStatus status);
    List<Order> findAllByStatus(OrderStatus status);
    List<Order> findAllByUserUsername(String username);
    List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status);
    List<Order> findAllByDataBetween(LocalDateTime start, LocalDateTime end);
    List<Order> findAllByUserIdAndDataBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
