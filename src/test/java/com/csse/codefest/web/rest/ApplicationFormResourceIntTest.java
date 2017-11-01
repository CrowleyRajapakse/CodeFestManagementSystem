package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.ApplicationForm;
import com.csse.codefest.repository.ApplicationFormRepository;
import com.csse.codefest.service.ApplicationFormService;
import com.csse.codefest.repository.search.ApplicationFormSearchRepository;
import com.csse.codefest.service.dto.ApplicationFormDTO;
import com.csse.codefest.service.mapper.ApplicationFormMapper;
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
 * Test class for the ApplicationFormResource REST controller.
 *
 * @see ApplicationFormResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class ApplicationFormResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private ApplicationFormRepository applicationFormRepository;

    @Autowired
    private ApplicationFormMapper applicationFormMapper;

    @Autowired
    private ApplicationFormService applicationFormService;

    @Autowired
    private ApplicationFormSearchRepository applicationFormSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationFormMockMvc;

    private ApplicationForm applicationForm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationFormResource applicationFormResource = new ApplicationFormResource(applicationFormService);
        this.restApplicationFormMockMvc = MockMvcBuilders.standaloneSetup(applicationFormResource)
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
    public static ApplicationForm createEntity(EntityManager em) {
        ApplicationForm applicationForm = new ApplicationForm()
            .name(DEFAULT_NAME)
            .dob(DEFAULT_DOB)
            .year(DEFAULT_YEAR)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .category(DEFAULT_CATEGORY);
        return applicationForm;
    }

    @Before
    public void initTest() {
        applicationFormSearchRepository.deleteAll();
        applicationForm = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationForm() throws Exception {
        int databaseSizeBeforeCreate = applicationFormRepository.findAll().size();

        // Create the ApplicationForm
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);
        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationForm in the database
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationForm testApplicationForm = applicationFormList.get(applicationFormList.size() - 1);
        assertThat(testApplicationForm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationForm.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testApplicationForm.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testApplicationForm.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApplicationForm.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApplicationForm.getCategory()).isEqualTo(DEFAULT_CATEGORY);

        // Validate the ApplicationForm in Elasticsearch
        ApplicationForm applicationFormEs = applicationFormSearchRepository.findOne(testApplicationForm.getId());
        assertThat(applicationFormEs).isEqualToComparingFieldByField(testApplicationForm);
    }

    @Test
    @Transactional
    public void createApplicationFormWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationFormRepository.findAll().size();

        // Create the ApplicationForm with an existing ID
        applicationForm.setId(1L);
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationForm in the database
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationFormRepository.findAll().size();
        // set the field null
        applicationForm.setName(null);

        // Create the ApplicationForm, which fails.
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationFormRepository.findAll().size();
        // set the field null
        applicationForm.setYear(null);

        // Create the ApplicationForm, which fails.
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationFormRepository.findAll().size();
        // set the field null
        applicationForm.setEmail(null);

        // Create the ApplicationForm, which fails.
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationFormRepository.findAll().size();
        // set the field null
        applicationForm.setCategory(null);

        // Create the ApplicationForm, which fails.
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        restApplicationFormMockMvc.perform(post("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationForms() throws Exception {
        // Initialize the database
        applicationFormRepository.saveAndFlush(applicationForm);

        // Get all the applicationFormList
        restApplicationFormMockMvc.perform(get("/api/application-forms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getApplicationForm() throws Exception {
        // Initialize the database
        applicationFormRepository.saveAndFlush(applicationForm);

        // Get the applicationForm
        restApplicationFormMockMvc.perform(get("/api/application-forms/{id}", applicationForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationForm.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationForm() throws Exception {
        // Get the applicationForm
        restApplicationFormMockMvc.perform(get("/api/application-forms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationForm() throws Exception {
        // Initialize the database
        applicationFormRepository.saveAndFlush(applicationForm);
        applicationFormSearchRepository.save(applicationForm);
        int databaseSizeBeforeUpdate = applicationFormRepository.findAll().size();

        // Update the applicationForm
        ApplicationForm updatedApplicationForm = applicationFormRepository.findOne(applicationForm.getId());
        updatedApplicationForm
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .year(UPDATED_YEAR)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .category(UPDATED_CATEGORY);
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(updatedApplicationForm);

        restApplicationFormMockMvc.perform(put("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationForm in the database
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeUpdate);
        ApplicationForm testApplicationForm = applicationFormList.get(applicationFormList.size() - 1);
        assertThat(testApplicationForm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationForm.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testApplicationForm.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testApplicationForm.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicationForm.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicationForm.getCategory()).isEqualTo(UPDATED_CATEGORY);

        // Validate the ApplicationForm in Elasticsearch
        ApplicationForm applicationFormEs = applicationFormSearchRepository.findOne(testApplicationForm.getId());
        assertThat(applicationFormEs).isEqualToComparingFieldByField(testApplicationForm);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationForm() throws Exception {
        int databaseSizeBeforeUpdate = applicationFormRepository.findAll().size();

        // Create the ApplicationForm
        ApplicationFormDTO applicationFormDTO = applicationFormMapper.toDto(applicationForm);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationFormMockMvc.perform(put("/api/application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationFormDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationForm in the database
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplicationForm() throws Exception {
        // Initialize the database
        applicationFormRepository.saveAndFlush(applicationForm);
        applicationFormSearchRepository.save(applicationForm);
        int databaseSizeBeforeDelete = applicationFormRepository.findAll().size();

        // Get the applicationForm
        restApplicationFormMockMvc.perform(delete("/api/application-forms/{id}", applicationForm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean applicationFormExistsInEs = applicationFormSearchRepository.exists(applicationForm.getId());
        assertThat(applicationFormExistsInEs).isFalse();

        // Validate the database is empty
        List<ApplicationForm> applicationFormList = applicationFormRepository.findAll();
        assertThat(applicationFormList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApplicationForm() throws Exception {
        // Initialize the database
        applicationFormRepository.saveAndFlush(applicationForm);
        applicationFormSearchRepository.save(applicationForm);

        // Search the applicationForm
        restApplicationFormMockMvc.perform(get("/api/_search/application-forms?query=id:" + applicationForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationForm.class);
        ApplicationForm applicationForm1 = new ApplicationForm();
        applicationForm1.setId(1L);
        ApplicationForm applicationForm2 = new ApplicationForm();
        applicationForm2.setId(applicationForm1.getId());
        assertThat(applicationForm1).isEqualTo(applicationForm2);
        applicationForm2.setId(2L);
        assertThat(applicationForm1).isNotEqualTo(applicationForm2);
        applicationForm1.setId(null);
        assertThat(applicationForm1).isNotEqualTo(applicationForm2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationFormDTO.class);
        ApplicationFormDTO applicationFormDTO1 = new ApplicationFormDTO();
        applicationFormDTO1.setId(1L);
        ApplicationFormDTO applicationFormDTO2 = new ApplicationFormDTO();
        assertThat(applicationFormDTO1).isNotEqualTo(applicationFormDTO2);
        applicationFormDTO2.setId(applicationFormDTO1.getId());
        assertThat(applicationFormDTO1).isEqualTo(applicationFormDTO2);
        applicationFormDTO2.setId(2L);
        assertThat(applicationFormDTO1).isNotEqualTo(applicationFormDTO2);
        applicationFormDTO1.setId(null);
        assertThat(applicationFormDTO1).isNotEqualTo(applicationFormDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationFormMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationFormMapper.fromId(null)).isNull();
    }
}
