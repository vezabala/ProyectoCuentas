package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RestarRepository;
import com.mycompany.myapp.service.RestarService;
import com.mycompany.myapp.service.dto.RestarDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Restar}.
 */
@RestController
@RequestMapping("/api")
public class RestarResource {

    private final Logger log = LoggerFactory.getLogger(RestarResource.class);

    private static final String ENTITY_NAME = "restar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestarService restarService;

    private final RestarRepository restarRepository;

    public RestarResource(RestarService restarService, RestarRepository restarRepository) {
        this.restarService = restarService;
        this.restarRepository = restarRepository;
    }

    /**
     * {@code POST  /restars} : Create a new restar.
     *
     * @param restarDTO the restarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restarDTO, or with status {@code 400 (Bad Request)} if the restar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restars")
    public ResponseEntity<RestarDTO> createRestar(@Valid @RequestBody RestarDTO restarDTO) throws URISyntaxException {
        log.debug("REST request to save Restar : {}", restarDTO);
        if (restarDTO.getId() != null) {
            throw new BadRequestAlertException("A new restar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestarDTO result = restarService.save(restarDTO);
        return ResponseEntity
            .created(new URI("/api/restars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restars/:id} : Updates an existing restar.
     *
     * @param id the id of the restarDTO to save.
     * @param restarDTO the restarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restarDTO,
     * or with status {@code 400 (Bad Request)} if the restarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restars/{id}")
    public ResponseEntity<RestarDTO> updateRestar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RestarDTO restarDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Restar : {}, {}", id, restarDTO);
        if (restarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestarDTO result = restarService.update(restarDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, restarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restars/:id} : Partial updates given fields of an existing restar, field will ignore if it is null
     *
     * @param id the id of the restarDTO to save.
     * @param restarDTO the restarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restarDTO,
     * or with status {@code 400 (Bad Request)} if the restarDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restarDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestarDTO> partialUpdateRestar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RestarDTO restarDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restar partially : {}, {}", id, restarDTO);
        if (restarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestarDTO> result = restarService.partialUpdate(restarDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, restarDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restars} : get all the restars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restars in body.
     */
    @GetMapping("/restars")
    public ResponseEntity<List<RestarDTO>> getAllRestars(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Restars");
        Page<RestarDTO> page = restarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restars/:id} : get the "id" restar.
     *
     * @param id the id of the restarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restars/{id}")
    public ResponseEntity<RestarDTO> getRestar(@PathVariable Long id) {
        log.debug("REST request to get Restar : {}", id);
        Optional<RestarDTO> restarDTO = restarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restarDTO);
    }

    /**
     * {@code DELETE  /restars/:id} : delete the "id" restar.
     *
     * @param id the id of the restarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restars/{id}")
    public ResponseEntity<Void> deleteRestar(@PathVariable Long id) {
        log.debug("REST request to delete Restar : {}", id);
        restarService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
