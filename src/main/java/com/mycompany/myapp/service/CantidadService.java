package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.repository.CantidadRepository;
import com.mycompany.myapp.service.dto.CantidadDTO;
import com.mycompany.myapp.service.mapper.CantidadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cantidad}.
 */
@Service
@Transactional
public class CantidadService {

    private final Logger log = LoggerFactory.getLogger(CantidadService.class);

    private final CantidadRepository cantidadRepository;

    private final CantidadMapper cantidadMapper;

    public CantidadService(CantidadRepository cantidadRepository, CantidadMapper cantidadMapper) {
        this.cantidadRepository = cantidadRepository;
        this.cantidadMapper = cantidadMapper;
    }

    /**
     * Save a cantidad.
     *
     * @param cantidadDTO the entity to save.
     * @return the persisted entity.
     */
    public CantidadDTO save(CantidadDTO cantidadDTO) {
        log.debug("Request to save Cantidad : {}", cantidadDTO);
        Cantidad cantidad = cantidadMapper.toEntity(cantidadDTO);
        cantidad = cantidadRepository.save(cantidad);
        return cantidadMapper.toDto(cantidad);
    }

    /**
     * Update a cantidad.
     *
     * @param cantidadDTO the entity to save.
     * @return the persisted entity.
     */
    public CantidadDTO update(CantidadDTO cantidadDTO) {
        log.debug("Request to save Cantidad : {}", cantidadDTO);
        Cantidad cantidad = cantidadMapper.toEntity(cantidadDTO);
        cantidad = cantidadRepository.save(cantidad);
        return cantidadMapper.toDto(cantidad);
    }

    /**
     * Partially update a cantidad.
     *
     * @param cantidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CantidadDTO> partialUpdate(CantidadDTO cantidadDTO) {
        log.debug("Request to partially update Cantidad : {}", cantidadDTO);

        return cantidadRepository
            .findById(cantidadDTO.getId())
            .map(existingCantidad -> {
                cantidadMapper.partialUpdate(existingCantidad, cantidadDTO);

                return existingCantidad;
            })
            .map(cantidadRepository::save)
            .map(cantidadMapper::toDto);
    }

    /**
     * Get all the cantidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CantidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cantidads");
        return cantidadRepository.findAll(pageable).map(cantidadMapper::toDto);
    }

    /**
     * Get one cantidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CantidadDTO> findOne(Long id) {
        log.debug("Request to get Cantidad : {}", id);
        return cantidadRepository.findById(id).map(cantidadMapper::toDto);
    }

    /**
     * Delete the cantidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cantidad : {}", id);
        cantidadRepository.deleteById(id);
    }
}
