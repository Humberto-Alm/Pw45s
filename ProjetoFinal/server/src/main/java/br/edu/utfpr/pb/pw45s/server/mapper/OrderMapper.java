package br.edu.utfpr.pb.pw45s.server.mapper;

import br.edu.utfpr.pb.pw45s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw45s.server.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderDTO toDto(Order entity);

    Order toEntity(OrderDTO dto);
}
