package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Judges;
import com.csse.codefest.domain.Event;
import com.csse.codefest.repository.JudgesRepository;
import com.csse.codefest.service.JudgesService;
import com.csse.codefest.repository.search.JudgesSearchRepository;
import com.csse.codefest.service.dto.JudgesDTO;
import com.csse.codefest.service.mapper.JudgesMapper;
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
 * Test class for the JudgesResource REST controller.
 *
 * @see JudgesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class JudgesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYER = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER = "BBBBBBBBBB";

    @Autowired
    private JudgesRepository judgesRepository;

    @Autowired
    private JudgesMapper judgesMapper;

    @Autowired
    private JudgesService judgesService;

    @Autowired
    private JudgesSearchRepository judgesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJudgesMockMvc;

    private Judges judges;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JudgesResource judgesResource = new JudgesResource(judgesService);
        this.restJudgesMockMvc = MockMvcBuilders.standaloneSetup(judgesResource)
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
    public static Judges createEntity(EntityManager em) {
        Judges judges = new Judges()
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .job_title(DEFAULT_JOB_TITLE)
            .employer(DEFAULT_EMPLOYER);
        // Add required entity
        Event events = EventResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        judges.setEvents(events);
        return judges;
    }

    @Before
    public void initTest() {
        judgesSearchRepository.deleteAll();
        judges = createEntity(em);
    }

    @Test
    @Transactional
    public void createJudges() throws Exception {
        int databaseSizeBeforeCreate = judgesRepository.findAll().size();

        // Create the Judges
        JudgesDTO judgesDTO = judgesMapper.toDto(judges);
        restJudgesMockMvc.perform(post("/api/judges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judgesDTO)))
            .andExpect(status().isCreated());

        // Validate the Judges in the database
        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeCreate + 1);
        Judges testJudges = judgesList.get(judgesList.size() - 1);
        assertThat(testJudges.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJudges.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testJudges.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testJudges.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testJudges.getJob_title()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testJudges.getEmployer()).isEqualTo(DEFAULT_EMPLOYER);

        // Validate the Judges in Elasticsearch
        Judges judgesEs = judgesSearchRepository.findOne(testJudges.getId());
        assertThat(judgesEs).isEqualToComparingFieldByField(testJudges);
    }

    @Test
    @Transactional
    public void createJudgesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = judgesRepository.findAll().size();

        // Create the Judges with an existing ID
        judges.setId(1L);
        JudgesDTO judgesDTO = judgesMapper.toDto(judges);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJudgesMockMvc.perform(post("/api/judges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judgesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Judges in the database
        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = judgesRepository.findAll().size();
        // set the field null
        judges.setName(null);

        // Create the Judges, which fails.
        JudgesDTO judgesDTO = judgesMapper.toDto(judges);

        restJudgesMockMvc.perform(post("/api/judges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judgesDTO)))
            .andExpect(status().isBadRequest());

        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJudges() throws Exception {
        // Initialize the database
        judgesRepository.saveAndFlush(judges);

        // Get all the judgesList
        restJudgesMockMvc.perform(get("/api/judges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(judges.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].job_title").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].employer").value(hasItem(DEFAULT_EMPLOYER.toString())));
    }

    @Test
    @Transactional
    public void getJudges() throws Exception {
        // Initialize the database
        judgesRepository.saveAndFlush(judges);

        // Get the judges
        restJudgesMockMvc.perform(get("/api/judges/{id}", judges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(judges.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.job_title").value(DEFAULT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.employer").value(DEFAULT_EMPLOYER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJudges() throws Exception {
        // Get the judges
        restJudgesMockMvc.perform(get("/api/judges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJudges() throws Exception {
        // Initialize the database
        judgesRepository.saveAndFlush(judges);
        judgesSearchRepository.save(judges);
        int databaseSizeBeforeUpdate = judgesRepository.findAll().size();

        // Update the judges
        Judges updatedJudges = judgesRepository.findOne(judges.getId());
        updatedJudges
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .job_title(UPDATED_JOB_TITLE)
            .employer(UPDATED_EMPLOYER);
        JudgesDTO judgesDTO = judgesMapper.toDto(updatedJudges);

        restJudgesMockMvc.perform(put("/api/judges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judgesDTO)))
            .andExpect(status().isOk());

        // Validate the Judges in the database
        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeUpdate);
        Judges testJudges = judgesList.get(judgesList.size() - 1);
        assertThat(testJudges.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJudges.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testJudges.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testJudges.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testJudges.getJob_title()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testJudges.getEmployer()).isEqualTo(UPDATED_EMPLOYER);

        // Validate the Judges in Elasticsearch
        Judges judgesEs = judgesSearchRepository.findOne(testJudges.getId());
        assertThat(judgesEs).isEqualToComparingFieldByField(testJudges);
    }

    @Test
    @Transactional
    public void updateNonExistingJudges() throws Exception {
        int databaseSizeBeforeUpdate = judgesRepository.findAll().size();

        // Create the Judges
        JudgesDTO judgesDTO = judgesMapper.toDto(judges);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJudgesMockMvc.perform(put("/api/judges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(judgesDTO)))
            .andExpect(status().isCreated());

        // Validate the Judges in the database
        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJudges() throws Exception {
        // Initialize the database
        judgesRepository.saveAndFlush(judges);
        judgesSearchRepository.save(judges);
        int databaseSizeBeforeDelete = judgesRepository.findAll().size();

        // Get the judges
        restJudgesMockMvc.perform(delete("/api/judges/{id}", judges.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean judgesExistsInEs = judgesSearchRepository.exists(judges.getId());
        assertThat(judgesExistsInEs).isFalse();

        // Validate the database is empty
        List<Judges> judgesList = judgesRepository.findAll();
        assertThat(judgesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJudges() throws Exception {
        // Initialize the database
        judgesRepository.saveAndFlush(judges);
        judgesSearchRepository.save(judges);

        // Search the judges
        restJudgesMockMvc.perform(get("/api/_search/judges?query=id:" + judges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(judges.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].job_title").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].employer").value(hasItem(DEFAULT_EMPLOYER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Judges.class);
        Judges judges1 = new Judges();
        judges1.setId(1L);
        Judges judges2 = new Judges();
        judges2.setId(judges1.getId());
        assertThat(judges1).isEqualTo(judges2);
        judges2.setId(2L);
        assertThat(judges1).isNotEqualTo(judges2);
        judges1.setId(null);
        assertThat(judges1).isNotEqualTo(judges2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JudgesDTO.class);
        JudgesDTO judgesDTO1 = new JudgesDTO();
        judgesDTO1.setId(1L);
        JudgesDTO judgesDTO2 = new JudgesDTO();
        assertThat(judgesDTO1).isNotEqualTo(judgesDTO2);
        judgesDTO2.setId(judgesDTO1.getId());
        assertThat(judgesDTO1).isEqualTo(judgesDTO2);
        judgesDTO2.setId(2L);
        assertThat(judgesDTO1).isNotEqualTo(judgesDTO2);
        judgesDTO1.setId(null);
        assertThat(judgesDTO1).isNotEqualTo(judgesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(judgesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(judgesMapper.fromId(null)).isNull();
    }
}
