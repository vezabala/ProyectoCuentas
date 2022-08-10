package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.domain.Sumar;
import com.mycompany.myapp.repository.SumarRepository;
import com.mycompany.myapp.service.dto.SumarDTO;
import com.mycompany.myapp.service.mapper.SumarMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SumarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SumarResourceIT {

    private static final Integer DEFAULT_CANTIDAD_SUMAR = 1;
    private static final Integer UPDATED_CANTIDAD_SUMAR = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/sumars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SumarRepository sumarRepository;

    @Autowired
    private SumarMapper sumarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSumarMockMvc;

    private Sumar sumar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sumar createEntity(EntityManager em) {
        Sumar sumar = new Sumar().cantidadSumar(DEFAULT_CANTIDAD_SUMAR).fecha(DEFAULT_FECHA);
        // Add required entity
        Cantidad cantidad;
        if (TestUtil.findAll(em, Cantidad.class).isEmpty()) {
            cantidad = CantidadResourceIT.createEntity(em);
            em.persist(cantidad);
            em.flush();
        } else {
            cantidad = TestUtil.findAll(em, Cantidad.class).get(0);
        }
        sumar.setCantidad(cantidad);
        return sumar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sumar createUpdatedEntity(EntityManager em) {
        Sumar sumar = new Sumar().cantidadSumar(UPDATED_CANTIDAD_SUMAR).fecha(UPDATED_FECHA);
        // Add required entity
        Cantidad cantidad;
        if (TestUtil.findAll(em, Cantidad.class).isEmpty()) {
            cantidad = CantidadResourceIT.createUpdatedEntity(em);
            em.persist(cantidad);
            em.flush();
        } else {
            cantidad = TestUtil.findAll(em, Cantidad.class).get(0);
        }
        sumar.setCantidad(cantidad);
        return sumar;
    }

    @BeforeEach
    public void initTest() {
        sumar = createEntity(em);
    }

    @Test
    @Transactional
    void createSumar() throws Exception {
        int databaseSizeBeforeCreate = sumarRepository.findAll().size();
        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);
        restSumarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isCreated());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeCreate + 1);
        Sumar testSumar = sumarList.get(sumarList.size() - 1);
        assertThat(testSumar.getCantidadSumar()).isEqualTo(DEFAULT_CANTIDAD_SUMAR);
        assertThat(testSumar.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createSumarWithExistingId() throws Exception {
        // Create the Sumar with an existing ID
        sumar.setId(1L);
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        int databaseSizeBeforeCreate = sumarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSumarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadSumarIsRequired() throws Exception {
        int databaseSizeBeforeTest = sumarRepository.findAll().size();
        // set the field null
        sumar.setCantidadSumar(null);

        // Create the Sumar, which fails.
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        restSumarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isBadRequest());

        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = sumarRepository.findAll().size();
        // set the field null
        sumar.setFecha(null);

        // Create the Sumar, which fails.
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        restSumarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isBadRequest());

        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSumars() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        // Get all the sumarList
        restSumarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sumar.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidadSumar").value(hasItem(DEFAULT_CANTIDAD_SUMAR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getSumar() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        // Get the sumar
        restSumarMockMvc
            .perform(get(ENTITY_API_URL_ID, sumar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sumar.getId().intValue()))
            .andExpect(jsonPath("$.cantidadSumar").value(DEFAULT_CANTIDAD_SUMAR))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSumar() throws Exception {
        // Get the sumar
        restSumarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSumar() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();

        // Update the sumar
        Sumar updatedSumar = sumarRepository.findById(sumar.getId()).get();
        // Disconnect from session so that the updates on updatedSumar are not directly saved in db
        em.detach(updatedSumar);
        updatedSumar.cantidadSumar(UPDATED_CANTIDAD_SUMAR).fecha(UPDATED_FECHA);
        SumarDTO sumarDTO = sumarMapper.toDto(updatedSumar);

        restSumarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sumarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sumarDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
        Sumar testSumar = sumarList.get(sumarList.size() - 1);
        assertThat(testSumar.getCantidadSumar()).isEqualTo(UPDATED_CANTIDAD_SUMAR);
        assertThat(testSumar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sumarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sumarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sumarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSumarWithPatch() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();

        // Update the sumar using partial update
        Sumar partialUpdatedSumar = new Sumar();
        partialUpdatedSumar.setId(sumar.getId());

        partialUpdatedSumar.fecha(UPDATED_FECHA);

        restSumarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSumar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSumar))
            )
            .andExpect(status().isOk());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
        Sumar testSumar = sumarList.get(sumarList.size() - 1);
        assertThat(testSumar.getCantidadSumar()).isEqualTo(DEFAULT_CANTIDAD_SUMAR);
        assertThat(testSumar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateSumarWithPatch() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();

        // Update the sumar using partial update
        Sumar partialUpdatedSumar = new Sumar();
        partialUpdatedSumar.setId(sumar.getId());

        partialUpdatedSumar.cantidadSumar(UPDATED_CANTIDAD_SUMAR).fecha(UPDATED_FECHA);

        restSumarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSumar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSumar))
            )
            .andExpect(status().isOk());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
        Sumar testSumar = sumarList.get(sumarList.size() - 1);
        assertThat(testSumar.getCantidadSumar()).isEqualTo(UPDATED_CANTIDAD_SUMAR);
        assertThat(testSumar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sumarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sumarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sumarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSumar() throws Exception {
        int databaseSizeBeforeUpdate = sumarRepository.findAll().size();
        sumar.setId(count.incrementAndGet());

        // Create the Sumar
        SumarDTO sumarDTO = sumarMapper.toDto(sumar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSumarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sumarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sumar in the database
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSumar() throws Exception {
        // Initialize the database
        sumarRepository.saveAndFlush(sumar);

        int databaseSizeBeforeDelete = sumarRepository.findAll().size();

        // Delete the sumar
        restSumarMockMvc
            .perform(delete(ENTITY_API_URL_ID, sumar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sumar> sumarList = sumarRepository.findAll();
        assertThat(sumarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
