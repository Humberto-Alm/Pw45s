package br.edu.utfpr.pb.pw45s.server.service;

import br.edu.utfpr.pb.pw45s.server.model.Order;

import java.util.List;

public interface IOrderService extends ICrudService<Order, Long> {

    List<Order> findAllByUserId(Long userId);
    Order findOrderById(Long id);
}