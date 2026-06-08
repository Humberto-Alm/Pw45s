package br.edu.utfpr.pb.pw45s.server.repository;

import br.edu.utfpr.pb.pw45s.server.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUserId(Long userId);
    Address findAddressById(Long id);
}