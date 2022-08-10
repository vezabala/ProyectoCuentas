package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SumarRepository;
import com.mycompany.myapp.service.SumarService;
import com.mycompany.myapp.service.dto.SumarDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Sumar}.
 */
@RestController
@RequestMapping("/api")
public class SumarResource {

    private final Logger log = LoggerFactory.getLogger(SumarResource.class);

    private static final String ENTITY_NAME = "sumar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SumarService sumarService;

    private final SumarRepository sumarRepository;

    public SumarResource(SumarService sumarService, SumarRepository sumarRepository) {
        this.sumarService = sumarService;
        this.sumarRepository = sumarRepository;
    }

    /**
     * {@code POST  /sumars} : Create a new sumar.
     *
     * @param sumarDTO the sumarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sumarDTO, or with status {@code 400 (Bad Request)} if the sumar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sumars")
    public ResponseEntity<SumarDTO> createSumar(@Valid @RequestBody SumarDTO sumarDTO) throws URISyntaxException {
        log.debug("REST request to save Sumar : {}", sumarDTO);
        if (sumarDTO.getId() != null) {
            throw new BadRequestAlertException("A new sumar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SumarDTO result = sumarService.save(sumarDTO);
        return ResponseEntity
            .created(new URI("/api/sumars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sumars/:id} : Updates an existing sumar.
     *
     * @param id the id of the sumarDTO to save.
     * @param sumarDTO the sumarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sumarDTO,
     * or with status {@code 400 (Bad Request)} if the sumarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sumarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sumars/{id}")
    public ResponseEntity<SumarDTO> updateSumar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SumarDTO sumarDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sumar : {}, {}", id, sumarDTO);
        if (sumarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sumarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sumarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SumarDTO result = sumarService.update(sumarDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sumarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sumars/:id} : Partial updates given fields of an existing sumar, field will ignore if it is null
     *
     * @param id the id of the sumarDTO to save.
     * @param sumarDTO the sumarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sumarDTO,
     * or with status {@code 400 (Bad Request)} if the sumarDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sumarDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sumarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sumars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SumarDTO> partialUpdateSumar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SumarDTO sumarDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sumar partially : {}, {}", id, sumarDTO);
        if (sumarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sumarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sumarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SumarDTO> result = sumarService.partialUpdate(sumarDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sumarDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sumars} : get all the sumars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sumars in body.
     */
    @GetMapping("/sumars")
    public ResponseEntity<List<SumarDTO>> getAllSumars(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Sumars");
        Page<SumarDTO> page = sumarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sumars/:id} : get the "id" sumar.
     *
     * @param id the id of the sumarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sumarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sumars/{id}")
    public ResponseEntity<SumarDTO> getSumar(@PathVariable Long id) {
        log.debug("REST request to get Sumar : {}", id);
        Optional<SumarDTO> sumarDTO = sumarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sumarDTO);
    }

    /**
     * {@code DELETE  /sumars/:id} : delete the "id" sumar.
     *
     * @param id the id of the sumarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sumars/{id}")
    public ResponseEntity<Void> deleteSumar(@PathVariable Long id) {
        log.debug("REST request to delete Sumar : {}", id);
        sumarService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
