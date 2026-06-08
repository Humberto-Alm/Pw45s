package br.edu.utfpr.pb.pw45s.server.dto;

import br.edu.utfpr.pb.pw45s.server.model.Address;
import br.edu.utfpr.pb.pw45s.server.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private long id;
    private LocalDateTime data;
    private User user;
    private AddressDTO address;
    private List<OrderItemCreateDTO> items;
    private BigDecimal total;
    private BigDecimal freight;
    private String paymentMethod;
}