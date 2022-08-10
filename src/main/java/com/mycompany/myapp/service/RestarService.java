package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Restar;
import com.mycompany.myapp.repository.RestarRepository;
import com.mycompany.myapp.service.dto.RestarDTO;
import com.mycompany.myapp.service.mapper.RestarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restar}.
 */
@Service
@Transactional
public class RestarService {

    private final Logger log = LoggerFactory.getLogger(RestarService.class);

    private final RestarRepository restarRepository;

    private final RestarMapper restarMapper;

    public RestarService(RestarRepository restarRepository, RestarMapper restarMapper) {
        this.restarRepository = restarRepository;
        this.restarMapper = restarMapper;
    }

    /**
     * Save a restar.
     *
     * @param restarDTO the entity to save.
     * @return the persisted entity.
     */
    public RestarDTO save(RestarDTO restarDTO) {
        log.debug("Request to save Restar : {}", restarDTO);
        Restar restar = restarMapper.toEntity(restarDTO);
        restar = restarRepository.save(restar);
        return restarMapper.toDto(restar);
    }

    /**
     * Update a restar.
     *
     * @param restarDTO the entity to save.
     * @return the persisted entity.
     */
    public RestarDTO update(RestarDTO restarDTO) {
        log.debug("Request to save Restar : {}", restarDTO);
        Restar restar = restarMapper.toEntity(restarDTO);
        restar = restarRepository.save(restar);
        return restarMapper.toDto(restar);
    }

    /**
     * Partially update a restar.
     *
     * @param restarDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestarDTO> partialUpdate(RestarDTO restarDTO) {
        log.debug("Request to partially update Restar : {}", restarDTO);

        return restarRepository
            .findById(restarDTO.getId())
            .map(existingRestar -> {
                restarMapper.partialUpdate(existingRestar, restarDTO);

                return existingRestar;
            })
            .map(restarRepository::save)
            .map(restarMapper::toDto);
    }

    /**
     * Get all the restars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RestarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restars");
        return restarRepository.findAll(pageable).map(restarMapper::toDto);
    }

    /**
     * Get one restar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestarDTO> findOne(Long id) {
        log.debug("Request to get Restar : {}", id);
        return restarRepository.findById(id).map(restarMapper::toDto);
    }

    /**
     * Delete the restar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restar : {}", id);
        restarRepository.deleteById(id);
    }
}
