package br.edu.utfpr.pb.pw45s.server.mapper;

import br.edu.utfpr.pb.pw45s.server.dto.AddressDTO;
import br.edu.utfpr.pb.pw45s.server.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address toEntity(AddressDTO dto);

    AddressDTO toDto(Address entity);
}
