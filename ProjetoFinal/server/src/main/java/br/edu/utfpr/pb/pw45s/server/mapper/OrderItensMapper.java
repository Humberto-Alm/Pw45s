package br.edu.utfpr.pb.pw45s.server.mapper;

import br.edu.utfpr.pb.pw45s.server.dto.OrderItensDTO;
import br.edu.utfpr.pb.pw45s.server.model.OrderItens;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = OrderMapper.class)
public interface OrderItensMapper {

    OrderItensDTO toDto(OrderItens entity);

    OrderItens toEntity(OrderItensDTO dto);
}
