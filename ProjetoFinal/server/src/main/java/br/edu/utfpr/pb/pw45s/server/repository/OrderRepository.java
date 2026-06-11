package br.edu.utfpr.pb.pw45s.server.repository;

import br.edu.utfpr.pb.pw45s.server.model.Order;
import br.edu.utfpr.pb.pw45s.server.model.OrderStatus;
import br.edu.utfpr.pb.pw45s.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findAllByUserId(Long id);

    List<Order> findAllByStatus(OrderStatus status);

    List<Order> findAllByUserUsername(String username);

    List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status);

    List<Order> findAllByDataBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findAllByUserIdAndDataBetween(Long userId, LocalDateTime start, LocalDateTime end);
}