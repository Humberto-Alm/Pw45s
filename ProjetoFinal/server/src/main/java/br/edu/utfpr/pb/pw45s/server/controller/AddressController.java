package br.edu.utfpr.pb.pw45s.server.controller;

import br.edu.utfpr.pb.pw45s.server.dto.AddressDTO;
import br.edu.utfpr.pb.pw45s.server.dto.CategoryDTO;
import br.edu.utfpr.pb.pw45s.server.mapper.AddressMapper;
import br.edu.utfpr.pb.pw45s.server.model.Address;
import br.edu.utfpr.pb.pw45s.server.model.Category;
import br.edu.utfpr.pb.pw45s.server.model.User;
import br.edu.utfpr.pb.pw45s.server.service.AuthService;
import br.edu.utfpr.pb.pw45s.server.service.IAddressService;
import br.edu.utfpr.pb.pw45s.server.service.ICrudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("addresses")
public class AddressController extends CrudController<Address, AddressDTO, Long> {
    private final IAddressService addressService;
    private final AddressMapper addressMapper;
    private final AuthService authService;

    public AddressController(IAddressService addressService, AddressMapper addressMapper, AuthService authService) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.authService = authService;
    }

    @Override
    protected ICrudService<Address, Long> getService() {
        return addressService;
    }

    @Override
    protected AddressDTO toDto(Address entity) {
        return addressMapper.toDto(entity);
    }

    @Override
    protected Address toEntity(AddressDTO dto) {
        return addressMapper.toEntity(dto);
    }

    @Override
    @PostMapping
    public ResponseEntity<AddressDTO> create(@RequestBody @Valid AddressDTO dto) {
        // 1. Pega o usuário logado
        User authenticatedUser = authService.getAuthenticatedUser();

        // 2. Converte DTO para Entidade
        Address address = addressMapper.toEntity(dto);

        // 3. Força o vínculo com o usuário
        address.setUser(authenticatedUser);

        // 4. Salva
        Address saved = addressService.save(address);

        // 5. Retorna
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressMapper.toDto(saved));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressByUser(@PathVariable Long userId) {
        List<Address> addresses = addressService.findAllByUserId(userId);
        List<AddressDTO> addressDTO = addresses.stream()
                .map(address -> addressMapper.toDto(address))
                .collect(Collectors.toList());
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping("/my_address")
    public ResponseEntity<List<AddressDTO>> getMyAddress() {
        Long userId = authService.getAuthenticatedUserId();
        List<Address> addresses = addressService.findAllByUserId(userId);
        List<AddressDTO> addressDTOs = addresses.stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(addressDTOs);
    }
}