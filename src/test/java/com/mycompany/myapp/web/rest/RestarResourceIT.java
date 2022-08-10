package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.domain.Restar;
import com.mycompany.myapp.repository.RestarRepository;
import com.mycompany.myapp.service.dto.RestarDTO;
import com.mycompany.myapp.service.mapper.RestarMapper;
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
 * Integration tests for the {@link RestarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestarResourceIT {

    private static final Integer DEFAULT_CANTIDAD_RESTAR = 1;
    private static final Integer UPDATED_CANTIDAD_RESTAR = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/restars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestarRepository restarRepository;

    @Autowired
    private RestarMapper restarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestarMockMvc;

    private Restar restar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restar createEntity(EntityManager em) {
        Restar restar = new Restar().cantidadRestar(DEFAULT_CANTIDAD_RESTAR).fecha(DEFAULT_FECHA);
        // Add required entity
        Cantidad cantidad;
        if (TestUtil.findAll(em, Cantidad.class).isEmpty()) {
            cantidad = CantidadResourceIT.createEntity(em);
            em.persist(cantidad);
            em.flush();
        } else {
            cantidad = TestUtil.findAll(em, Cantidad.class).get(0);
        }
        restar.setCantidad(cantidad);
        return restar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restar createUpdatedEntity(EntityManager em) {
        Restar restar = new Restar().cantidadRestar(UPDATED_CANTIDAD_RESTAR).fecha(UPDATED_FECHA);
        // Add required entity
        Cantidad cantidad;
        if (TestUtil.findAll(em, Cantidad.class).isEmpty()) {
            cantidad = CantidadResourceIT.createUpdatedEntity(em);
            em.persist(cantidad);
            em.flush();
        } else {
            cantidad = TestUtil.findAll(em, Cantidad.class).get(0);
        }
        restar.setCantidad(cantidad);
        return restar;
    }

    @BeforeEach
    public void initTest() {
        restar = createEntity(em);
    }

    @Test
    @Transactional
    void createRestar() throws Exception {
        int databaseSizeBeforeCreate = restarRepository.findAll().size();
        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);
        restRestarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restarDTO)))
            .andExpect(status().isCreated());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeCreate + 1);
        Restar testRestar = restarList.get(restarList.size() - 1);
        assertThat(testRestar.getCantidadRestar()).isEqualTo(DEFAULT_CANTIDAD_RESTAR);
        assertThat(testRestar.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createRestarWithExistingId() throws Exception {
        // Create the Restar with an existing ID
        restar.setId(1L);
        RestarDTO restarDTO = restarMapper.toDto(restar);

        int databaseSizeBeforeCreate = restarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadRestarIsRequired() throws Exception {
        int databaseSizeBeforeTest = restarRepository.findAll().size();
        // set the field null
        restar.setCantidadRestar(null);

        // Create the Restar, which fails.
        RestarDTO restarDTO = restarMapper.toDto(restar);

        restRestarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restarDTO)))
            .andExpect(status().isBadRequest());

        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = restarRepository.findAll().size();
        // set the field null
        restar.setFecha(null);

        // Create the Restar, which fails.
        RestarDTO restarDTO = restarMapper.toDto(restar);

        restRestarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restarDTO)))
            .andExpect(status().isBadRequest());

        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestars() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        // Get all the restarList
        restRestarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restar.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidadRestar").value(hasItem(DEFAULT_CANTIDAD_RESTAR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getRestar() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        // Get the restar
        restRestarMockMvc
            .perform(get(ENTITY_API_URL_ID, restar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restar.getId().intValue()))
            .andExpect(jsonPath("$.cantidadRestar").value(DEFAULT_CANTIDAD_RESTAR))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRestar() throws Exception {
        // Get the restar
        restRestarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestar() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        int databaseSizeBeforeUpdate = restarRepository.findAll().size();

        // Update the restar
        Restar updatedRestar = restarRepository.findById(restar.getId()).get();
        // Disconnect from session so that the updates on updatedRestar are not directly saved in db
        em.detach(updatedRestar);
        updatedRestar.cantidadRestar(UPDATED_CANTIDAD_RESTAR).fecha(UPDATED_FECHA);
        RestarDTO restarDTO = restarMapper.toDto(updatedRestar);

        restRestarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
        Restar testRestar = restarList.get(restarList.size() - 1);
        assertThat(testRestar.getCantidadRestar()).isEqualTo(UPDATED_CANTIDAD_RESTAR);
        assertThat(testRestar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestarWithPatch() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        int databaseSizeBeforeUpdate = restarRepository.findAll().size();

        // Update the restar using partial update
        Restar partialUpdatedRestar = new Restar();
        partialUpdatedRestar.setId(restar.getId());

        partialUpdatedRestar.cantidadRestar(UPDATED_CANTIDAD_RESTAR).fecha(UPDATED_FECHA);

        restRestarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestar))
            )
            .andExpect(status().isOk());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
        Restar testRestar = restarList.get(restarList.size() - 1);
        assertThat(testRestar.getCantidadRestar()).isEqualTo(UPDATED_CANTIDAD_RESTAR);
        assertThat(testRestar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateRestarWithPatch() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        int databaseSizeBeforeUpdate = restarRepository.findAll().size();

        // Update the restar using partial update
        Restar partialUpdatedRestar = new Restar();
        partialUpdatedRestar.setId(restar.getId());

        partialUpdatedRestar.cantidadRestar(UPDATED_CANTIDAD_RESTAR).fecha(UPDATED_FECHA);

        restRestarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestar))
            )
            .andExpect(status().isOk());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
        Restar testRestar = restarList.get(restarList.size() - 1);
        assertThat(testRestar.getCantidadRestar()).isEqualTo(UPDATED_CANTIDAD_RESTAR);
        assertThat(testRestar.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestar() throws Exception {
        int databaseSizeBeforeUpdate = restarRepository.findAll().size();
        restar.setId(count.incrementAndGet());

        // Create the Restar
        RestarDTO restarDTO = restarMapper.toDto(restar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestarMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restar in the database
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestar() throws Exception {
        // Initialize the database
        restarRepository.saveAndFlush(restar);

        int databaseSizeBeforeDelete = restarRepository.findAll().size();

        // Delete the restar
        restRestarMockMvc
            .perform(delete(ENTITY_API_URL_ID, restar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restar> restarList = restarRepository.findAll();
        assertThat(restarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
