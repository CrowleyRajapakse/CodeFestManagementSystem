package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Competition;
import com.csse.codefest.repository.CompetitionRepository;
import com.csse.codefest.service.CompetitionService;
import com.csse.codefest.repository.search.CompetitionSearchRepository;
import com.csse.codefest.service.dto.CompetitionDTO;
import com.csse.codefest.service.mapper.CompetitionMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompetitionResource REST controller.
 *
 * @see CompetitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class CompetitionResourceIntTest {

    private static final String DEFAULT_TITLE = "Test Title";
    private static final String UPDATED_TITLE = "New Title";

    private static final String DEFAULT_THEME = "Test Theme";
    private static final String UPDATED_THEME = "New Theme";

    private static final String DEFAULT_DESCRIPTION = "Test Description";
    private static final String UPDATED_DESCRIPTION = "New Description";

    private static final String DEFAULT_VENUE = "Test Venue";
    private static final String UPDATED_VENUE = "New Venue";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SPONSOR = "Test Sponsor";
    private static final String UPDATED_SPONSOR = "New Sponsor";

    private static final String DEFAULT_OBJECTIVE = "Test Objective";
    private static final String UPDATED_OBJECTIVE = "New Objective";

    private static final String DEFAULT_COORDINATOR = "Test Coordinator";
    private static final String UPDATED_COORDINATOR = "New Coordinator";

    private static final String DEFAULT_ABOUT_US = "Test AboutUs";
    private static final String UPDATED_ABOUT_US = "New AboutUs";

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CompetitionMapper competitionMapper;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private CompetitionSearchRepository competitionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompetitionMockMvc;

    private Competition competition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompetitionResource competitionResource = new CompetitionResource(competitionService);
        this.restCompetitionMockMvc = MockMvcBuilders.standaloneSetup(competitionResource)
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
    public static Competition createEntity(EntityManager em) {
        Competition competition = new Competition()
            .title(DEFAULT_TITLE)
            .theme(DEFAULT_THEME)
            .description(DEFAULT_DESCRIPTION)
            .venue(DEFAULT_VENUE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .sponsor(DEFAULT_SPONSOR)
            .objective(DEFAULT_OBJECTIVE)
            .coordinator(DEFAULT_COORDINATOR)
            .aboutUs(DEFAULT_ABOUT_US);
        return competition;
    }

    @Before
    public void initTest() {
        competitionSearchRepository.deleteAll();
        competition = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetition() throws Exception {
        int databaseSizeBeforeCreate = competitionRepository.findAll().size();

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);
        restCompetitionMockMvc.perform(post("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isCreated());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeCreate + 1);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCompetition.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testCompetition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompetition.getVenue()).isEqualTo(DEFAULT_VENUE);
        assertThat(testCompetition.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCompetition.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCompetition.getSponsor()).isEqualTo(DEFAULT_SPONSOR);
        assertThat(testCompetition.getObjective()).isEqualTo(DEFAULT_OBJECTIVE);
        assertThat(testCompetition.getCoordinator()).isEqualTo(DEFAULT_COORDINATOR);
        assertThat(testCompetition.getAboutUs()).isEqualTo(DEFAULT_ABOUT_US);

        // Validate the Competition in Elasticsearch
        Competition competitionEs = competitionSearchRepository.findOne(testCompetition.getId());
        assertThat(competitionEs).isEqualToComparingFieldByField(testCompetition);
    }

    @Test
    @Transactional
    public void createCompetitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competitionRepository.findAll().size();

        // Create the Competition with an existing ID
        competition.setId(1L);
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitionMockMvc.perform(post("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setTitle(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc.perform(post("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVenueIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setVenue(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc.perform(post("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordinatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setCoordinator(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc.perform(post("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetitions() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get all the competitionList
        restCompetitionMockMvc.perform(get("/api/competitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competition.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].sponsor").value(hasItem(DEFAULT_SPONSOR.toString())))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE.toString())))
            .andExpect(jsonPath("$.[*].coordinator").value(hasItem(DEFAULT_COORDINATOR.toString())))
            .andExpect(jsonPath("$.[*].aboutUs").value(hasItem(DEFAULT_ABOUT_US.toString())));
    }

    @Test
    @Transactional
    public void getCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get the competition
        restCompetitionMockMvc.perform(get("/api/competitions/{id}", competition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(competition.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.venue").value(DEFAULT_VENUE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.sponsor").value(DEFAULT_SPONSOR.toString()))
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE.toString()))
            .andExpect(jsonPath("$.coordinator").value(DEFAULT_COORDINATOR.toString()))
            .andExpect(jsonPath("$.aboutUs").value(DEFAULT_ABOUT_US.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetition() throws Exception {
        // Get the competition
        restCompetitionMockMvc.perform(get("/api/competitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);
        competitionSearchRepository.save(competition);
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Update the competition
        Competition updatedCompetition = competitionRepository.findOne(competition.getId());
        updatedCompetition
            .title(UPDATED_TITLE)
            .theme(UPDATED_THEME)
            .description(UPDATED_DESCRIPTION)
            .venue(UPDATED_VENUE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .sponsor(UPDATED_SPONSOR)
            .objective(UPDATED_OBJECTIVE)
            .coordinator(UPDATED_COORDINATOR)
            .aboutUs(UPDATED_ABOUT_US);
        CompetitionDTO competitionDTO = competitionMapper.toDto(updatedCompetition);

        restCompetitionMockMvc.perform(put("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isOk());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCompetition.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testCompetition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompetition.getVenue()).isEqualTo(UPDATED_VENUE);
        assertThat(testCompetition.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCompetition.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCompetition.getSponsor()).isEqualTo(UPDATED_SPONSOR);
        assertThat(testCompetition.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
        assertThat(testCompetition.getCoordinator()).isEqualTo(UPDATED_COORDINATOR);
        assertThat(testCompetition.getAboutUs()).isEqualTo(UPDATED_ABOUT_US);

        // Validate the Competition in Elasticsearch
        Competition competitionEs = competitionSearchRepository.findOne(testCompetition.getId());
        assertThat(competitionEs).isEqualToComparingFieldByField(testCompetition);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompetitionMockMvc.perform(put("/api/competitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO)))
            .andExpect(status().isCreated());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);
        competitionSearchRepository.save(competition);
        int databaseSizeBeforeDelete = competitionRepository.findAll().size();

        // Get the competition
        restCompetitionMockMvc.perform(delete("/api/competitions/{id}", competition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean competitionExistsInEs = competitionSearchRepository.exists(competition.getId());
        assertThat(competitionExistsInEs).isFalse();

        // Validate the database is empty
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);
        competitionSearchRepository.save(competition);

        // Search the competition
        restCompetitionMockMvc.perform(get("/api/_search/competitions?query=id:" + competition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competition.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].sponsor").value(hasItem(DEFAULT_SPONSOR.toString())))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE.toString())))
            .andExpect(jsonPath("$.[*].coordinator").value(hasItem(DEFAULT_COORDINATOR.toString())))
            .andExpect(jsonPath("$.[*].aboutUs").value(hasItem(DEFAULT_ABOUT_US.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competition.class);
        Competition competition1 = new Competition();
        competition1.setId(1L);
        Competition competition2 = new Competition();
        competition2.setId(competition1.getId());
        assertThat(competition1).isEqualTo(competition2);
        competition2.setId(2L);
        assertThat(competition1).isNotEqualTo(competition2);
        competition1.setId(null);
        assertThat(competition1).isNotEqualTo(competition2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitionDTO.class);
        CompetitionDTO competitionDTO1 = new CompetitionDTO();
        competitionDTO1.setId(1L);
        CompetitionDTO competitionDTO2 = new CompetitionDTO();
        assertThat(competitionDTO1).isNotEqualTo(competitionDTO2);
        competitionDTO2.setId(competitionDTO1.getId());
        assertThat(competitionDTO1).isEqualTo(competitionDTO2);
        competitionDTO2.setId(2L);
        assertThat(competitionDTO1).isNotEqualTo(competitionDTO2);
        competitionDTO1.setId(null);
        assertThat(competitionDTO1).isNotEqualTo(competitionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(competitionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(competitionMapper.fromId(null)).isNull();
    }
}
