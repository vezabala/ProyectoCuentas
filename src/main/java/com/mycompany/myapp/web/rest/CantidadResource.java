package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CantidadRepository;
import com.mycompany.myapp.service.CantidadService;
import com.mycompany.myapp.service.dto.CantidadDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Cantidad}.
 */
@RestController
@RequestMapping("/api")
public class CantidadResource {

    private final Logger log = LoggerFactory.getLogger(CantidadResource.class);

    private static final String ENTITY_NAME = "cantidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CantidadService cantidadService;

    private final CantidadRepository cantidadRepository;

    public CantidadResource(CantidadService cantidadService, CantidadRepository cantidadRepository) {
        this.cantidadService = cantidadService;
        this.cantidadRepository = cantidadRepository;
    }

    /**
     * {@code POST  /cantidads} : Create a new cantidad.
     *
     * @param cantidadDTO the cantidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cantidadDTO, or with status {@code 400 (Bad Request)} if the cantidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cantidads")
    public ResponseEntity<CantidadDTO> createCantidad(@Valid @RequestBody CantidadDTO cantidadDTO) throws URISyntaxException {
        log.debug("REST request to save Cantidad : {}", cantidadDTO);
        if (cantidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new cantidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CantidadDTO result = cantidadService.save(cantidadDTO);
        return ResponseEntity
            .created(new URI("/api/cantidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cantidads/:id} : Updates an existing cantidad.
     *
     * @param id the id of the cantidadDTO to save.
     * @param cantidadDTO the cantidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cantidadDTO,
     * or with status {@code 400 (Bad Request)} if the cantidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cantidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cantidads/{id}")
    public ResponseEntity<CantidadDTO> updateCantidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CantidadDTO cantidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cantidad : {}, {}", id, cantidadDTO);
        if (cantidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cantidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cantidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CantidadDTO result = cantidadService.update(cantidadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cantidadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cantidads/:id} : Partial updates given fields of an existing cantidad, field will ignore if it is null
     *
     * @param id the id of the cantidadDTO to save.
     * @param cantidadDTO the cantidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cantidadDTO,
     * or with status {@code 400 (Bad Request)} if the cantidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cantidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cantidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cantidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CantidadDTO> partialUpdateCantidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CantidadDTO cantidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cantidad partially : {}, {}", id, cantidadDTO);
        if (cantidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cantidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cantidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CantidadDTO> result = cantidadService.partialUpdate(cantidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cantidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cantidads} : get all the cantidads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cantidads in body.
     */
    @GetMapping("/cantidads")
    public ResponseEntity<List<CantidadDTO>> getAllCantidads(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cantidads");
        Page<CantidadDTO> page = cantidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cantidads/:id} : get the "id" cantidad.
     *
     * @param id the id of the cantidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cantidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cantidads/{id}")
    public ResponseEntity<CantidadDTO> getCantidad(@PathVariable Long id) {
        log.debug("REST request to get Cantidad : {}", id);
        Optional<CantidadDTO> cantidadDTO = cantidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cantidadDTO);
    }

    /**
     * {@code DELETE  /cantidads/:id} : delete the "id" cantidad.
     *
     * @param id the id of the cantidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cantidads/{id}")
    public ResponseEntity<Void> deleteCantidad(@PathVariable Long id) {
        log.debug("REST request to delete Cantidad : {}", id);
        cantidadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
