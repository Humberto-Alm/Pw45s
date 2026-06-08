package br.edu.utfpr.pb.pw45s.server.controller;

import br.edu.utfpr.pb.pw45s.server.dto.FreightRequestDTO;
import br.edu.utfpr.pb.pw45s.server.dto.FreightResponseDTO;
import br.edu.utfpr.pb.pw45s.server.service.FreightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("freight")
public class FreightController {

    @Autowired
    private FreightService freightService;

    @PostMapping("/calculate")
    public ResponseEntity<FreightResponseDTO> calculateFreight(@RequestBody FreightRequestDTO freightRequest) {
        if (freightRequest.getCep() == null || freightRequest.getCep().length() < 8) {
            return ResponseEntity.badRequest().build();
        }

        String cleanCep = freightRequest.getCep().replaceAll("\\D", "");

        // Passa o CEP e o Valor Total para o serviço
        FreightResponseDTO response = freightService.calculateCheapestFreight(
                cleanCep,
                freightRequest.getTotalValue()
        );

        return ResponseEntity.ok(response);
    }
}