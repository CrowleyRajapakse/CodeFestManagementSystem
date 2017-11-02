package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Competitor;
import com.csse.codefest.domain.Event;
import com.csse.codefest.repository.CompetitorRepository;
import com.csse.codefest.service.CompetitorService;
import com.csse.codefest.service.MailService;
import com.csse.codefest.repository.search.CompetitorSearchRepository;
import com.csse.codefest.service.dto.CompetitorDTO;
import com.csse.codefest.service.mapper.CompetitorMapper;
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
 * Test class for the CompetitorResource REST controller.
 *
 * @see CompetitorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class CompetitorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_UNIVERSITY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSITY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    @Autowired
    private CompetitorRepository competitorRepository;

    @Autowired
    private CompetitorMapper competitorMapper;

    @Autowired
    private CompetitorService competitorService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CompetitorSearchRepository competitorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompetitorMockMvc;

    private Competitor competitor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompetitorResource competitorResource = new CompetitorResource(competitorService, mailService);
        this.restCompetitorMockMvc = MockMvcBuilders.standaloneSetup(competitorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competitor createEntity(EntityManager em) {
        Competitor competitor = new Competitor()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .university(DEFAULT_UNIVERSITY)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .category(DEFAULT_CATEGORY)
            .school(DEFAULT_SCHOOL);
        // Add required entity
        Event events = EventResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        competitor.setEvents(events);
        return competitor;
    }

    @Before
    public void initTest() {
        competitorSearchRepository.deleteAll();
        competitor = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetitor() throws Exception {
        int databaseSizeBeforeCreate = competitorRepository.findAll().size();

        // Create the Competitor
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);
        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isCreated());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeCreate + 1);
        Competitor testCompetitor = competitorList.get(competitorList.size() - 1);
        assertThat(testCompetitor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetitor.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCompetitor.getUniversity()).isEqualTo(DEFAULT_UNIVERSITY);
        assertThat(testCompetitor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompetitor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCompetitor.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCompetitor.getSchool()).isEqualTo(DEFAULT_SCHOOL);

        // Validate the Competitor in Elasticsearch
        Competitor competitorEs = competitorSearchRepository.findOne(testCompetitor.getId());
        assertThat(competitorEs).isEqualToComparingFieldByField(testCompetitor);
    }

    @Test
    @Transactional
    public void createCompetitorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competitorRepository.findAll().size();

        // Create the Competitor with an existing ID
        competitor.setId(1L);
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setName(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setAge(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUniversityIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setUniversity(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setEmail(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setPhone(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setCategory(null);

        // Create the Competitor, which fails.
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetitors() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);

        // Get all the competitorList
        restCompetitorMockMvc.perform(get("/api/competitors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].university").value(hasItem(DEFAULT_UNIVERSITY.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())));
    }

    @Test
    @Transactional
    public void getCompetitor() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);

        // Get the competitor
        restCompetitorMockMvc.perform(get("/api/competitors/{id}", competitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(competitor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.university").value(DEFAULT_UNIVERSITY.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetitor() throws Exception {
        // Get the competitor
        restCompetitorMockMvc.perform(get("/api/competitors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetitor() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);
        competitorSearchRepository.save(competitor);
        int databaseSizeBeforeUpdate = competitorRepository.findAll().size();

        // Update the competitor
        Competitor updatedCompetitor = competitorRepository.findOne(competitor.getId());
        updatedCompetitor
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .university(UPDATED_UNIVERSITY)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .category(UPDATED_CATEGORY)
            .school(UPDATED_SCHOOL);
        CompetitorDTO competitorDTO = competitorMapper.toDto(updatedCompetitor);

        restCompetitorMockMvc.perform(put("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isOk());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeUpdate);
        Competitor testCompetitor = competitorList.get(competitorList.size() - 1);
        assertThat(testCompetitor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetitor.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCompetitor.getUniversity()).isEqualTo(UPDATED_UNIVERSITY);
        assertThat(testCompetitor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompetitor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCompetitor.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCompetitor.getSchool()).isEqualTo(UPDATED_SCHOOL);

        // Validate the Competitor in Elasticsearch
        Competitor competitorEs = competitorSearchRepository.findOne(testCompetitor.getId());
        assertThat(competitorEs).isEqualToComparingFieldByField(testCompetitor);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetitor() throws Exception {
        int databaseSizeBeforeUpdate = competitorRepository.findAll().size();

        // Create the Competitor
        CompetitorDTO competitorDTO = competitorMapper.toDto(competitor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompetitorMockMvc.perform(put("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitorDTO)))
            .andExpect(status().isCreated());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompetitor() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);
        competitorSearchRepository.save(competitor);
        int databaseSizeBeforeDelete = competitorRepository.findAll().size();

        // Get the competitor
        restCompetitorMockMvc.perform(delete("/api/competitors/{id}", competitor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean competitorExistsInEs = competitorSearchRepository.exists(competitor.getId());
        assertThat(competitorExistsInEs).isFalse();

        // Validate the database is empty
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompetitor() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);
        competitorSearchRepository.save(competitor);

        // Search the competitor
        restCompetitorMockMvc.perform(get("/api/_search/competitors?query=id:" + competitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].university").value(hasItem(DEFAULT_UNIVERSITY.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competitor.class);
        Competitor competitor1 = new Competitor();
        competitor1.setId(1L);
        Competitor competitor2 = new Competitor();
        competitor2.setId(competitor1.getId());
        assertThat(competitor1).isEqualTo(competitor2);
        competitor2.setId(2L);
        assertThat(competitor1).isNotEqualTo(competitor2);
        competitor1.setId(null);
        assertThat(competitor1).isNotEqualTo(competitor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitorDTO.class);
        CompetitorDTO competitorDTO1 = new CompetitorDTO();
        competitorDTO1.setId(1L);
        CompetitorDTO competitorDTO2 = new CompetitorDTO();
        assertThat(competitorDTO1).isNotEqualTo(competitorDTO2);
        competitorDTO2.setId(competitorDTO1.getId());
        assertThat(competitorDTO1).isEqualTo(competitorDTO2);
        competitorDTO2.setId(2L);
        assertThat(competitorDTO1).isNotEqualTo(competitorDTO2);
        competitorDTO1.setId(null);
        assertThat(competitorDTO1).isNotEqualTo(competitorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(competitorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(competitorMapper.fromId(null)).isNull();
    }
}
