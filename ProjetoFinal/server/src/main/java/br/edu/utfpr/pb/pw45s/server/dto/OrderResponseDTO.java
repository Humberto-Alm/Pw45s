package br.edu.utfpr.pb.pw45s.server.dto;

import br.edu.utfpr.pb.pw45s.server.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime data;
    private UserDTO user;
    private AddressDTO address;
    private List<OrderItensResponseDTO> items;
    private BigDecimal total;
    private BigDecimal freight;
    private String paymentMethod;
    private OrderStatus status;
    private String statusDescricao;
    private String comprovantePdfUrl;
}