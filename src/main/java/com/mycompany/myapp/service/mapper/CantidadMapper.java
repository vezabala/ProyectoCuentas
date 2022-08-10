package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.service.dto.CantidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cantidad} and its DTO {@link CantidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface CantidadMapper extends EntityMapper<CantidadDTO, Cantidad> {}
