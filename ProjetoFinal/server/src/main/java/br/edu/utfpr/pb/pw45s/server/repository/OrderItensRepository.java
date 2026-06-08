package br.edu.utfpr.pb.pw45s.server.repository;


import br.edu.utfpr.pb.pw45s.server.model.OrderItens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItensRepository extends JpaRepository<OrderItens, Long> {
    List<OrderItens> findAllByOrderId(Long orderId);
}