package br.edu.utfpr.pb.pw45s.server.dto;

import br.edu.utfpr.pb.pw45s.server.model.Order;
import br.edu.utfpr.pb.pw45s.server.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItensDTO {
    private Long id;
    private Order order;
    private Product product;
    private double quantity;
    private BigDecimal unit_price;
}
