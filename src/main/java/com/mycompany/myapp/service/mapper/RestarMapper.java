package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.domain.Restar;
import com.mycompany.myapp.service.dto.CantidadDTO;
import com.mycompany.myapp.service.dto.RestarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restar} and its DTO {@link RestarDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestarMapper extends EntityMapper<RestarDTO, Restar> {
    @Mapping(target = "cantidad", source = "cantidad", qualifiedByName = "cantidadId")
    RestarDTO toDto(Restar s);

    @Named("cantidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CantidadDTO toDtoCantidadId(Cantidad cantidad);
}
