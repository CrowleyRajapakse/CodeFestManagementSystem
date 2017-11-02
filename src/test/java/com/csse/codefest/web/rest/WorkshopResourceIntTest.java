package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Workshop;
import com.csse.codefest.repository.WorkshopRepository;
import com.csse.codefest.service.WorkshopService;
import com.csse.codefest.repository.search.WorkshopSearchRepository;
import com.csse.codefest.service.dto.WorkshopDTO;
import com.csse.codefest.service.mapper.WorkshopMapper;
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
 * Test class for the WorkshopResource REST controller.
 *
 * @see WorkshopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class WorkshopResourceIntTest {

    private static final String DEFAULT_TITLE = "Test Title";
    private static final String UPDATED_TITLE = "New Title";

    private static final String DEFAULT_DESCRIPTION = "Test Description";
    private static final String UPDATED_DESCRIPTION = "New Description";

    private static final String DEFAULT_VENUE = "Test Venue";
    private static final String UPDATED_VENUE = "New Venue";

    private static final String DEFAULT_WCOORDINATOR = "Test Coordinator";
    private static final String UPDATED_WCOORDINATOR = "New Coordinator";

    private static final LocalDate DEFAULT_SW_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SW_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SW_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private WorkshopMapper workshopMapper;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private WorkshopSearchRepository workshopSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkshopMockMvc;

    private Workshop workshop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkshopResource workshopResource = new WorkshopResource(workshopService);
        this.restWorkshopMockMvc = MockMvcBuilders.standaloneSetup(workshopResource)
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
    public static Workshop createEntity(EntityManager em) {
        Workshop workshop = new Workshop()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .venue(DEFAULT_VENUE)
            .wcoordinator(DEFAULT_WCOORDINATOR)
            .swTime(DEFAULT_SW_TIME)
            .swDate(DEFAULT_SW_DATE);
        return workshop;
    }

    @Before
    public void initTest() {
        workshopSearchRepository.deleteAll();
        workshop = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkshop() throws Exception {
        int databaseSizeBeforeCreate = workshopRepository.findAll().size();

        // Create the Workshop
        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        restWorkshopMockMvc.perform(post("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isCreated());

        // Validate the Workshop in the database
        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeCreate + 1);
        Workshop testWorkshop = workshopList.get(workshopList.size() - 1);
        assertThat(testWorkshop.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkshop.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkshop.getVenue()).isEqualTo(DEFAULT_VENUE);
        assertThat(testWorkshop.getWcoordinator()).isEqualTo(DEFAULT_WCOORDINATOR);
        assertThat(testWorkshop.getSwTime()).isEqualTo(DEFAULT_SW_TIME);
        assertThat(testWorkshop.getSwDate()).isEqualTo(DEFAULT_SW_DATE);

        // Validate the Workshop in Elasticsearch
        Workshop workshopEs = workshopSearchRepository.findOne(testWorkshop.getId());
        assertThat(workshopEs).isEqualToComparingFieldByField(testWorkshop);
    }

    @Test
    @Transactional
    public void createWorkshopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workshopRepository.findAll().size();

        // Create the Workshop with an existing ID
        workshop.setId(1L);
        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkshopMockMvc.perform(post("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Workshop in the database
        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = workshopRepository.findAll().size();
        // set the field null
        workshop.setTitle(null);

        // Create the Workshop, which fails.
        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);

        restWorkshopMockMvc.perform(post("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isBadRequest());

        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWcoordinatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = workshopRepository.findAll().size();
        // set the field null
        workshop.setWcoordinator(null);

        // Create the Workshop, which fails.
        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);

        restWorkshopMockMvc.perform(post("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isBadRequest());

        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkshops() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush(workshop);

        // Get all the workshopList
        restWorkshopMockMvc.perform(get("/api/workshops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workshop.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE.toString())))
            .andExpect(jsonPath("$.[*].wcoordinator").value(hasItem(DEFAULT_WCOORDINATOR.toString())))
            .andExpect(jsonPath("$.[*].swTime").value(hasItem(DEFAULT_SW_TIME.toString())))
            .andExpect(jsonPath("$.[*].swDate").value(hasItem(DEFAULT_SW_DATE.toString())));
    }

    @Test
    @Transactional
    public void getWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush(workshop);

        // Get the workshop
        restWorkshopMockMvc.perform(get("/api/workshops/{id}", workshop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workshop.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.venue").value(DEFAULT_VENUE.toString()))
            .andExpect(jsonPath("$.wcoordinator").value(DEFAULT_WCOORDINATOR.toString()))
            .andExpect(jsonPath("$.swTime").value(DEFAULT_SW_TIME.toString()))
            .andExpect(jsonPath("$.swDate").value(DEFAULT_SW_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkshop() throws Exception {
        // Get the workshop
        restWorkshopMockMvc.perform(get("/api/workshops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush(workshop);
        workshopSearchRepository.save(workshop);
        int databaseSizeBeforeUpdate = workshopRepository.findAll().size();

        // Update the workshop
        Workshop updatedWorkshop = workshopRepository.findOne(workshop.getId());
        updatedWorkshop
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .venue(UPDATED_VENUE)
            .wcoordinator(UPDATED_WCOORDINATOR)
            .swTime(UPDATED_SW_TIME)
            .swDate(UPDATED_SW_DATE);
        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);

        restWorkshopMockMvc.perform(put("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isOk());

        // Validate the Workshop in the database
        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeUpdate);
        Workshop testWorkshop = workshopList.get(workshopList.size() - 1);
        assertThat(testWorkshop.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkshop.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkshop.getVenue()).isEqualTo(UPDATED_VENUE);
        assertThat(testWorkshop.getWcoordinator()).isEqualTo(UPDATED_WCOORDINATOR);
        assertThat(testWorkshop.getSwTime()).isEqualTo(UPDATED_SW_TIME);
        assertThat(testWorkshop.getSwDate()).isEqualTo(UPDATED_SW_DATE);

        // Validate the Workshop in Elasticsearch
        Workshop workshopEs = workshopSearchRepository.findOne(testWorkshop.getId());
        assertThat(workshopEs).isEqualToComparingFieldByField(testWorkshop);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkshop() throws Exception {
        int databaseSizeBeforeUpdate = workshopRepository.findAll().size();

        // Create the Workshop
        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkshopMockMvc.perform(put("/api/workshops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workshopDTO)))
            .andExpect(status().isCreated());

        // Validate the Workshop in the database
        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush(workshop);
        workshopSearchRepository.save(workshop);
        int databaseSizeBeforeDelete = workshopRepository.findAll().size();

        // Get the workshop
        restWorkshopMockMvc.perform(delete("/api/workshops/{id}", workshop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean workshopExistsInEs = workshopSearchRepository.exists(workshop.getId());
        assertThat(workshopExistsInEs).isFalse();

        // Validate the database is empty
        List<Workshop> workshopList = workshopRepository.findAll();
        assertThat(workshopList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush(workshop);
        workshopSearchRepository.save(workshop);

        // Search the workshop
        restWorkshopMockMvc.perform(get("/api/_search/workshops?query=id:" + workshop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workshop.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE.toString())))
            .andExpect(jsonPath("$.[*].wcoordinator").value(hasItem(DEFAULT_WCOORDINATOR.toString())))
            .andExpect(jsonPath("$.[*].swTime").value(hasItem(DEFAULT_SW_TIME.toString())))
            .andExpect(jsonPath("$.[*].swDate").value(hasItem(DEFAULT_SW_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workshop.class);
        Workshop workshop1 = new Workshop();
        workshop1.setId(1L);
        Workshop workshop2 = new Workshop();
        workshop2.setId(workshop1.getId());
        assertThat(workshop1).isEqualTo(workshop2);
        workshop2.setId(2L);
        assertThat(workshop1).isNotEqualTo(workshop2);
        workshop1.setId(null);
        assertThat(workshop1).isNotEqualTo(workshop2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopDTO.class);
        WorkshopDTO workshopDTO1 = new WorkshopDTO();
        workshopDTO1.setId(1L);
        WorkshopDTO workshopDTO2 = new WorkshopDTO();
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
        workshopDTO2.setId(workshopDTO1.getId());
        assertThat(workshopDTO1).isEqualTo(workshopDTO2);
        workshopDTO2.setId(2L);
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
        workshopDTO1.setId(null);
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workshopMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workshopMapper.fromId(null)).isNull();
    }
}
