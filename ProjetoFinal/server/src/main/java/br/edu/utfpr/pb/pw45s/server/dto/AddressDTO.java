package br.edu.utfpr.pb.pw45s.server.dto;

import br.edu.utfpr.pb.pw45s.server.model.User;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private User user;
    private String city;
    private String logradouro;
    private String numero;
    private String bairro;
    private String complemento;
    private String cep;
    private String title;
}