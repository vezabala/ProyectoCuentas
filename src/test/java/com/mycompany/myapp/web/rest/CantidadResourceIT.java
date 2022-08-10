package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cantidad;
import com.mycompany.myapp.repository.CantidadRepository;
import com.mycompany.myapp.service.dto.CantidadDTO;
import com.mycompany.myapp.service.mapper.CantidadMapper;
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
 * Integration tests for the {@link CantidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CantidadResourceIT {

    private static final String DEFAULT_NOMBRE_CUENTA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CUENTA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_ACTUAL = 1;
    private static final Integer UPDATED_CANTIDAD_ACTUAL = 2;

    private static final String ENTITY_API_URL = "/api/cantidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CantidadRepository cantidadRepository;

    @Autowired
    private CantidadMapper cantidadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCantidadMockMvc;

    private Cantidad cantidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cantidad createEntity(EntityManager em) {
        Cantidad cantidad = new Cantidad().nombreCuenta(DEFAULT_NOMBRE_CUENTA).cantidadActual(DEFAULT_CANTIDAD_ACTUAL);
        return cantidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cantidad createUpdatedEntity(EntityManager em) {
        Cantidad cantidad = new Cantidad().nombreCuenta(UPDATED_NOMBRE_CUENTA).cantidadActual(UPDATED_CANTIDAD_ACTUAL);
        return cantidad;
    }

    @BeforeEach
    public void initTest() {
        cantidad = createEntity(em);
    }

    @Test
    @Transactional
    void createCantidad() throws Exception {
        int databaseSizeBeforeCreate = cantidadRepository.findAll().size();
        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);
        restCantidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cantidadDTO)))
            .andExpect(status().isCreated());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeCreate + 1);
        Cantidad testCantidad = cantidadList.get(cantidadList.size() - 1);
        assertThat(testCantidad.getNombreCuenta()).isEqualTo(DEFAULT_NOMBRE_CUENTA);
        assertThat(testCantidad.getCantidadActual()).isEqualTo(DEFAULT_CANTIDAD_ACTUAL);
    }

    @Test
    @Transactional
    void createCantidadWithExistingId() throws Exception {
        // Create the Cantidad with an existing ID
        cantidad.setId(1L);
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        int databaseSizeBeforeCreate = cantidadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCantidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cantidadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreCuentaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cantidadRepository.findAll().size();
        // set the field null
        cantidad.setNombreCuenta(null);

        // Create the Cantidad, which fails.
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        restCantidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cantidadDTO)))
            .andExpect(status().isBadRequest());

        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadActualIsRequired() throws Exception {
        int databaseSizeBeforeTest = cantidadRepository.findAll().size();
        // set the field null
        cantidad.setCantidadActual(null);

        // Create the Cantidad, which fails.
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        restCantidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cantidadDTO)))
            .andExpect(status().isBadRequest());

        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCantidads() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        // Get all the cantidadList
        restCantidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cantidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCuenta").value(hasItem(DEFAULT_NOMBRE_CUENTA)))
            .andExpect(jsonPath("$.[*].cantidadActual").value(hasItem(DEFAULT_CANTIDAD_ACTUAL)));
    }

    @Test
    @Transactional
    void getCantidad() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        // Get the cantidad
        restCantidadMockMvc
            .perform(get(ENTITY_API_URL_ID, cantidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cantidad.getId().intValue()))
            .andExpect(jsonPath("$.nombreCuenta").value(DEFAULT_NOMBRE_CUENTA))
            .andExpect(jsonPath("$.cantidadActual").value(DEFAULT_CANTIDAD_ACTUAL));
    }

    @Test
    @Transactional
    void getNonExistingCantidad() throws Exception {
        // Get the cantidad
        restCantidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCantidad() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();

        // Update the cantidad
        Cantidad updatedCantidad = cantidadRepository.findById(cantidad.getId()).get();
        // Disconnect from session so that the updates on updatedCantidad are not directly saved in db
        em.detach(updatedCantidad);
        updatedCantidad.nombreCuenta(UPDATED_NOMBRE_CUENTA).cantidadActual(UPDATED_CANTIDAD_ACTUAL);
        CantidadDTO cantidadDTO = cantidadMapper.toDto(updatedCantidad);

        restCantidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cantidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
        Cantidad testCantidad = cantidadList.get(cantidadList.size() - 1);
        assertThat(testCantidad.getNombreCuenta()).isEqualTo(UPDATED_NOMBRE_CUENTA);
        assertThat(testCantidad.getCantidadActual()).isEqualTo(UPDATED_CANTIDAD_ACTUAL);
    }

    @Test
    @Transactional
    void putNonExistingCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cantidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cantidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCantidadWithPatch() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();

        // Update the cantidad using partial update
        Cantidad partialUpdatedCantidad = new Cantidad();
        partialUpdatedCantidad.setId(cantidad.getId());

        partialUpdatedCantidad.nombreCuenta(UPDATED_NOMBRE_CUENTA);

        restCantidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCantidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCantidad))
            )
            .andExpect(status().isOk());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
        Cantidad testCantidad = cantidadList.get(cantidadList.size() - 1);
        assertThat(testCantidad.getNombreCuenta()).isEqualTo(UPDATED_NOMBRE_CUENTA);
        assertThat(testCantidad.getCantidadActual()).isEqualTo(DEFAULT_CANTIDAD_ACTUAL);
    }

    @Test
    @Transactional
    void fullUpdateCantidadWithPatch() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();

        // Update the cantidad using partial update
        Cantidad partialUpdatedCantidad = new Cantidad();
        partialUpdatedCantidad.setId(cantidad.getId());

        partialUpdatedCantidad.nombreCuenta(UPDATED_NOMBRE_CUENTA).cantidadActual(UPDATED_CANTIDAD_ACTUAL);

        restCantidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCantidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCantidad))
            )
            .andExpect(status().isOk());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
        Cantidad testCantidad = cantidadList.get(cantidadList.size() - 1);
        assertThat(testCantidad.getNombreCuenta()).isEqualTo(UPDATED_NOMBRE_CUENTA);
        assertThat(testCantidad.getCantidadActual()).isEqualTo(UPDATED_CANTIDAD_ACTUAL);
    }

    @Test
    @Transactional
    void patchNonExistingCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cantidadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCantidad() throws Exception {
        int databaseSizeBeforeUpdate = cantidadRepository.findAll().size();
        cantidad.setId(count.incrementAndGet());

        // Create the Cantidad
        CantidadDTO cantidadDTO = cantidadMapper.toDto(cantidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantidadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cantidadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cantidad in the database
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCantidad() throws Exception {
        // Initialize the database
        cantidadRepository.saveAndFlush(cantidad);

        int databaseSizeBeforeDelete = cantidadRepository.findAll().size();

        // Delete the cantidad
        restCantidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, cantidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cantidad> cantidadList = cantidadRepository.findAll();
        assertThat(cantidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
