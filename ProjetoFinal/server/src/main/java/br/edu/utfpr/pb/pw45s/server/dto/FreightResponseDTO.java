package br.edu.utfpr.pb.pw45s.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FreightResponseDTO {
    private Double value;        // Preço do frete
    private Integer deliveryDays; // Dias para entrega
}