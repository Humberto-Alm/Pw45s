package br.edu.utfpr.pb.pw45s.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    private String installmentInfo;
    private List<String> specifications;
    private List<String> gallery;
    private CategoryDTO category;
    private String contentType;
}