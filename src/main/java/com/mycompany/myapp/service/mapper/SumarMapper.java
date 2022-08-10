package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.domain.Sumar;
import com.mycompany.myapp.service.dto.CantidadDTO;
import com.mycompany.myapp.service.dto.SumarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sumar} and its DTO {@link SumarDTO}.
 */
@Mapper(componentModel = "spring")
public interface SumarMapper extends EntityMapper<SumarDTO, Sumar> {
    @Mapping(target = "cantidad", source = "cantidad", qualifiedByName = "cantidadId")
    SumarDTO toDto(Sumar s);

    @Named("cantidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CantidadDTO toDtoCantidadId(Cantidad cantidad);
}
