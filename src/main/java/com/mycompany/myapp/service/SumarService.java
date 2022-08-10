package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sumar;
import com.mycompany.myapp.repository.SumarRepository;
import com.mycompany.myapp.service.dto.SumarDTO;
import com.mycompany.myapp.service.mapper.SumarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sumar}.
 */
@Service
@Transactional
public class SumarService {

    private final Logger log = LoggerFactory.getLogger(SumarService.class);

    private final SumarRepository sumarRepository;

    private final SumarMapper sumarMapper;

    public SumarService(SumarRepository sumarRepository, SumarMapper sumarMapper) {
        this.sumarRepository = sumarRepository;
        this.sumarMapper = sumarMapper;
    }

    /**
     * Save a sumar.
     *
     * @param sumarDTO the entity to save.
     * @return the persisted entity.
     */
    public SumarDTO save(SumarDTO sumarDTO) {
        log.debug("Request to save Sumar : {}", sumarDTO);
        Sumar sumar = sumarMapper.toEntity(sumarDTO);
        sumar = sumarRepository.save(sumar);
        return sumarMapper.toDto(sumar);
    }

    /**
     * Update a sumar.
     *
     * @param sumarDTO the entity to save.
     * @return the persisted entity.
     */
    public SumarDTO update(SumarDTO sumarDTO) {
        log.debug("Request to save Sumar : {}", sumarDTO);
        Sumar sumar = sumarMapper.toEntity(sumarDTO);
        sumar = sumarRepository.save(sumar);
        return sumarMapper.toDto(sumar);
    }

    /**
     * Partially update a sumar.
     *
     * @param sumarDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SumarDTO> partialUpdate(SumarDTO sumarDTO) {
        log.debug("Request to partially update Sumar : {}", sumarDTO);

        return sumarRepository
            .findById(sumarDTO.getId())
            .map(existingSumar -> {
                sumarMapper.partialUpdate(existingSumar, sumarDTO);

                return existingSumar;
            })
            .map(sumarRepository::save)
            .map(sumarMapper::toDto);
    }

    /**
     * Get all the sumars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SumarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sumars");
        return sumarRepository.findAll(pageable).map(sumarMapper::toDto);
    }

    /**
     * Get one sumar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SumarDTO> findOne(Long id) {
        log.debug("Request to get Sumar : {}", id);
        return sumarRepository.findById(id).map(sumarMapper::toDto);
    }

    /**
     * Delete the sumar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sumar : {}", id);
        sumarRepository.deleteById(id);
    }
}
