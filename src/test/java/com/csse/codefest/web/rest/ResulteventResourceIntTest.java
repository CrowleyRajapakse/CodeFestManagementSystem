package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Resultevent;
import com.csse.codefest.domain.Event;
import com.csse.codefest.repository.ResulteventRepository;
import com.csse.codefest.service.ResulteventService;
import com.csse.codefest.repository.search.ResulteventSearchRepository;
import com.csse.codefest.service.dto.ResulteventDTO;
import com.csse.codefest.service.mapper.ResulteventMapper;
import com.csse.codefest.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResulteventResource REST controller.
 *
 * @see ResulteventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class ResulteventResourceIntTest {

    private static final String DEFAULT_WINNER = "AAAAAAAAAA";
    private static final String UPDATED_WINNER = "BBBBBBBBBB";

    private static final String DEFAULT_RUNNER_UP_1 = "AAAAAAAAAA";
    private static final String UPDATED_RUNNER_UP_1 = "BBBBBBBBBB";

    private static final String DEFAULT_RUNNER_UP_2 = "AAAAAAAAAA";
    private static final String UPDATED_RUNNER_UP_2 = "BBBBBBBBBB";

    private static final String DEFAULT_MERIT_1 = "AAAAAAAAAA";
    private static final String UPDATED_MERIT_1 = "BBBBBBBBBB";

    private static final String DEFAULT_MERIT_2 = "AAAAAAAAAA";
    private static final String UPDATED_MERIT_2 = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ResulteventRepository resulteventRepository;

    @Autowired
    private ResulteventMapper resulteventMapper;

    @Autowired
    private ResulteventService resulteventService;

    @Autowired
    private ResulteventSearchRepository resulteventSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResulteventMockMvc;

    private Resultevent resultevent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResulteventResource resulteventResource = new ResulteventResource(resulteventService);
        this.restResulteventMockMvc = MockMvcBuilders.standaloneSetup(resulteventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultevent createEntity(EntityManager em) {
        Resultevent resultevent = new Resultevent()
            .winner(DEFAULT_WINNER)
            .runner_up1(DEFAULT_RUNNER_UP_1)
            .runner_up2(DEFAULT_RUNNER_UP_2)
            .merit1(DEFAULT_MERIT_1)
            .merit2(DEFAULT_MERIT_2)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Event eventresult = EventResourceIntTest.createEntity(em);
        em.persist(eventresult);
        em.flush();
        resultevent.setEventresult(eventresult);
        return resultevent;
    }

    @Before
    public void initTest() {
        resulteventSearchRepository.deleteAll();
        resultevent = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultevent() throws Exception {
        int databaseSizeBeforeCreate = resulteventRepository.findAll().size();

        // Create the Resultevent
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);
        restResulteventMockMvc.perform(post("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isCreated());

        // Validate the Resultevent in the database
        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeCreate + 1);
        Resultevent testResultevent = resulteventList.get(resulteventList.size() - 1);
        assertThat(testResultevent.getWinner()).isEqualTo(DEFAULT_WINNER);
        assertThat(testResultevent.getRunner_up1()).isEqualTo(DEFAULT_RUNNER_UP_1);
        assertThat(testResultevent.getRunner_up2()).isEqualTo(DEFAULT_RUNNER_UP_2);
        assertThat(testResultevent.getMerit1()).isEqualTo(DEFAULT_MERIT_1);
        assertThat(testResultevent.getMerit2()).isEqualTo(DEFAULT_MERIT_2);
        assertThat(testResultevent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Resultevent in Elasticsearch
        Resultevent resulteventEs = resulteventSearchRepository.findOne(testResultevent.getId());
        assertThat(resulteventEs).isEqualToComparingFieldByField(testResultevent);
    }

    @Test
    @Transactional
    public void createResulteventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resulteventRepository.findAll().size();

        // Create the Resultevent with an existing ID
        resultevent.setId(1L);
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResulteventMockMvc.perform(post("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resultevent in the database
        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWinnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = resulteventRepository.findAll().size();
        // set the field null
        resultevent.setWinner(null);

        // Create the Resultevent, which fails.
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);

        restResulteventMockMvc.perform(post("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isBadRequest());

        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRunner_up1IsRequired() throws Exception {
        int databaseSizeBeforeTest = resulteventRepository.findAll().size();
        // set the field null
        resultevent.setRunner_up1(null);

        // Create the Resultevent, which fails.
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);

        restResulteventMockMvc.perform(post("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isBadRequest());

        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRunner_up2IsRequired() throws Exception {
        int databaseSizeBeforeTest = resulteventRepository.findAll().size();
        // set the field null
        resultevent.setRunner_up2(null);

        // Create the Resultevent, which fails.
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);

        restResulteventMockMvc.perform(post("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isBadRequest());

        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResultevents() throws Exception {
        // Initialize the database
        resulteventRepository.saveAndFlush(resultevent);

        // Get all the resulteventList
        restResulteventMockMvc.perform(get("/api/resultevents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultevent.getId().intValue())))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.toString())))
            .andExpect(jsonPath("$.[*].runner_up1").value(hasItem(DEFAULT_RUNNER_UP_1.toString())))
            .andExpect(jsonPath("$.[*].runner_up2").value(hasItem(DEFAULT_RUNNER_UP_2.toString())))
            .andExpect(jsonPath("$.[*].merit1").value(hasItem(DEFAULT_MERIT_1.toString())))
            .andExpect(jsonPath("$.[*].merit2").value(hasItem(DEFAULT_MERIT_2.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getResultevent() throws Exception {
        // Initialize the database
        resulteventRepository.saveAndFlush(resultevent);

        // Get the resultevent
        restResulteventMockMvc.perform(get("/api/resultevents/{id}", resultevent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultevent.getId().intValue()))
            .andExpect(jsonPath("$.winner").value(DEFAULT_WINNER.toString()))
            .andExpect(jsonPath("$.runner_up1").value(DEFAULT_RUNNER_UP_1.toString()))
            .andExpect(jsonPath("$.runner_up2").value(DEFAULT_RUNNER_UP_2.toString()))
            .andExpect(jsonPath("$.merit1").value(DEFAULT_MERIT_1.toString()))
            .andExpect(jsonPath("$.merit2").value(DEFAULT_MERIT_2.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResultevent() throws Exception {
        // Get the resultevent
        restResulteventMockMvc.perform(get("/api/resultevents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultevent() throws Exception {
        // Initialize the database
        resulteventRepository.saveAndFlush(resultevent);
        resulteventSearchRepository.save(resultevent);
        int databaseSizeBeforeUpdate = resulteventRepository.findAll().size();

        // Update the resultevent
        Resultevent updatedResultevent = resulteventRepository.findOne(resultevent.getId());
        updatedResultevent
            .winner(UPDATED_WINNER)
            .runner_up1(UPDATED_RUNNER_UP_1)
            .runner_up2(UPDATED_RUNNER_UP_2)
            .merit1(UPDATED_MERIT_1)
            .merit2(UPDATED_MERIT_2)
            .description(UPDATED_DESCRIPTION);
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(updatedResultevent);

        restResulteventMockMvc.perform(put("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isOk());

        // Validate the Resultevent in the database
        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeUpdate);
        Resultevent testResultevent = resulteventList.get(resulteventList.size() - 1);
        assertThat(testResultevent.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testResultevent.getRunner_up1()).isEqualTo(UPDATED_RUNNER_UP_1);
        assertThat(testResultevent.getRunner_up2()).isEqualTo(UPDATED_RUNNER_UP_2);
        assertThat(testResultevent.getMerit1()).isEqualTo(UPDATED_MERIT_1);
        assertThat(testResultevent.getMerit2()).isEqualTo(UPDATED_MERIT_2);
        assertThat(testResultevent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Resultevent in Elasticsearch
        Resultevent resulteventEs = resulteventSearchRepository.findOne(testResultevent.getId());
        assertThat(resulteventEs).isEqualToComparingFieldByField(testResultevent);
    }

    @Test
    @Transactional
    public void updateNonExistingResultevent() throws Exception {
        int databaseSizeBeforeUpdate = resulteventRepository.findAll().size();

        // Create the Resultevent
        ResulteventDTO resulteventDTO = resulteventMapper.toDto(resultevent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResulteventMockMvc.perform(put("/api/resultevents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resulteventDTO)))
            .andExpect(status().isCreated());

        // Validate the Resultevent in the database
        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResultevent() throws Exception {
        // Initialize the database
        resulteventRepository.saveAndFlush(resultevent);
        resulteventSearchRepository.save(resultevent);
        int databaseSizeBeforeDelete = resulteventRepository.findAll().size();

        // Get the resultevent
        restResulteventMockMvc.perform(delete("/api/resultevents/{id}", resultevent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resulteventExistsInEs = resulteventSearchRepository.exists(resultevent.getId());
        assertThat(resulteventExistsInEs).isFalse();

        // Validate the database is empty
        List<Resultevent> resulteventList = resulteventRepository.findAll();
        assertThat(resulteventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResultevent() throws Exception {
        // Initialize the database
        resulteventRepository.saveAndFlush(resultevent);
        resulteventSearchRepository.save(resultevent);

        // Search the resultevent
        restResulteventMockMvc.perform(get("/api/_search/resultevents?query=id:" + resultevent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultevent.getId().intValue())))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.toString())))
            .andExpect(jsonPath("$.[*].runner_up1").value(hasItem(DEFAULT_RUNNER_UP_1.toString())))
            .andExpect(jsonPath("$.[*].runner_up2").value(hasItem(DEFAULT_RUNNER_UP_2.toString())))
            .andExpect(jsonPath("$.[*].merit1").value(hasItem(DEFAULT_MERIT_1.toString())))
            .andExpect(jsonPath("$.[*].merit2").value(hasItem(DEFAULT_MERIT_2.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resultevent.class);
        Resultevent resultevent1 = new Resultevent();
        resultevent1.setId(1L);
        Resultevent resultevent2 = new Resultevent();
        resultevent2.setId(resultevent1.getId());
        assertThat(resultevent1).isEqualTo(resultevent2);
        resultevent2.setId(2L);
        assertThat(resultevent1).isNotEqualTo(resultevent2);
        resultevent1.setId(null);
        assertThat(resultevent1).isNotEqualTo(resultevent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResulteventDTO.class);
        ResulteventDTO resulteventDTO1 = new ResulteventDTO();
        resulteventDTO1.setId(1L);
        ResulteventDTO resulteventDTO2 = new ResulteventDTO();
        assertThat(resulteventDTO1).isNotEqualTo(resulteventDTO2);
        resulteventDTO2.setId(resulteventDTO1.getId());
        assertThat(resulteventDTO1).isEqualTo(resulteventDTO2);
        resulteventDTO2.setId(2L);
        assertThat(resulteventDTO1).isNotEqualTo(resulteventDTO2);
        resulteventDTO1.setId(null);
        assertThat(resulteventDTO1).isNotEqualTo(resulteventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resulteventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resulteventMapper.fromId(null)).isNull();
    }
}
