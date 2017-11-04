package com.csse.codefest.web.rest;

import com.csse.codefest.CodeFestManagementSystemV1App;

import com.csse.codefest.domain.Budget;
import com.csse.codefest.domain.Event;
import com.csse.codefest.repository.BudgetRepository;
import com.csse.codefest.service.BudgetService;
import com.csse.codefest.repository.search.BudgetSearchRepository;
import com.csse.codefest.service.dto.BudgetDTO;
import com.csse.codefest.service.mapper.BudgetMapper;
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
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeFestManagementSystemV1App.class)
public class BudgetResourceIntTest {

    private static final Double DEFAULT_FOOD = 100D;
    private static final Double UPDATED_FOOD = 200D;

    private static final Double DEFAULT_DECORATIONS = 100D;
    private static final Double UPDATED_DECORATIONS = 200D;

    private static final Double DEFAULT_PRIZES = 100D;
    private static final Double UPDATED_PRIZES = 200D;

    private static final Double DEFAULT_PHOTOGRAPHY = 100D;
    private static final Double UPDATED_PHOTOGRAPHY = 200D;

    private static final Double DEFAULT_TRANSPORT = 100D;
    private static final Double UPDATED_TRANSPORT = 200D;

    private static final Double DEFAULT_STATIONERY = 100D;
    private static final Double UPDATED_STATIONERY = 200D;

    private static final Double DEFAULT_GUESTS = 100D;
    private static final Double UPDATED_GUESTS = 200D;

    private static final Double DEFAULT_OTHER = 100D;
    private static final Double UPDATED_OTHER = 200D;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetSearchRepository budgetSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBudgetMockMvc;

    private Budget budget;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BudgetResource budgetResource = new BudgetResource(budgetService);
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
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
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget()
            .food(DEFAULT_FOOD)
            .decorations(DEFAULT_DECORATIONS)
            .prizes(DEFAULT_PRIZES)
            .photography(DEFAULT_PHOTOGRAPHY)
            .transport(DEFAULT_TRANSPORT)
            .stationery(DEFAULT_STATIONERY)
            .guests(DEFAULT_GUESTS)
            .other(DEFAULT_OTHER);
        // Add required entity
        Event event = EventResourceIntTest.createEntity(em);
        em.persist(event);
        em.flush();
        budget.setEvent(event);
        return budget;
    }

    @Before
    public void initTest() {
        budgetSearchRepository.deleteAll();
        budget = createEntity(em);
    }

    @Test
    @Transactional
    public void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);
        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate + 1);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getFood()).isEqualTo(DEFAULT_FOOD);
        assertThat(testBudget.getDecorations()).isEqualTo(DEFAULT_DECORATIONS);
        assertThat(testBudget.getPrizes()).isEqualTo(DEFAULT_PRIZES);
        assertThat(testBudget.getPhotography()).isEqualTo(DEFAULT_PHOTOGRAPHY);
        assertThat(testBudget.getTransport()).isEqualTo(DEFAULT_TRANSPORT);
        assertThat(testBudget.getStationery()).isEqualTo(DEFAULT_STATIONERY);
        assertThat(testBudget.getGuests()).isEqualTo(DEFAULT_GUESTS);
        assertThat(testBudget.getOther()).isEqualTo(DEFAULT_OTHER);

        // Validate the Budget in Elasticsearch
        Budget budgetEs = budgetSearchRepository.findOne(testBudget.getId());
        assertThat(budgetEs).isEqualToComparingFieldByField(testBudget);
    }

    @Test
    @Transactional
    public void createBudgetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget with an existing ID
        budget.setId(1L);
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBudgets() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get all the budgetList
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].food").value(hasItem(DEFAULT_FOOD.doubleValue())))
            .andExpect(jsonPath("$.[*].decorations").value(hasItem(DEFAULT_DECORATIONS.doubleValue())))
            .andExpect(jsonPath("$.[*].prizes").value(hasItem(DEFAULT_PRIZES.doubleValue())))
            .andExpect(jsonPath("$.[*].photography").value(hasItem(DEFAULT_PHOTOGRAPHY.doubleValue())))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.doubleValue())))
            .andExpect(jsonPath("$.[*].stationery").value(hasItem(DEFAULT_STATIONERY.doubleValue())))
            .andExpect(jsonPath("$.[*].guests").value(hasItem(DEFAULT_GUESTS.doubleValue())))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.doubleValue())));
    }

    @Test
    @Transactional
    public void getBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.food").value(DEFAULT_FOOD.doubleValue()))
            .andExpect(jsonPath("$.decorations").value(DEFAULT_DECORATIONS.doubleValue()))
            .andExpect(jsonPath("$.prizes").value(DEFAULT_PRIZES.doubleValue()))
            .andExpect(jsonPath("$.photography").value(DEFAULT_PHOTOGRAPHY.doubleValue()))
            .andExpect(jsonPath("$.transport").value(DEFAULT_TRANSPORT.doubleValue()))
            .andExpect(jsonPath("$.stationery").value(DEFAULT_STATIONERY.doubleValue()))
            .andExpect(jsonPath("$.guests").value(DEFAULT_GUESTS.doubleValue()))
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        budgetSearchRepository.save(budget);
        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Update the budget
        Budget updatedBudget = budgetRepository.findOne(budget.getId());
        updatedBudget
            .food(UPDATED_FOOD)
            .decorations(UPDATED_DECORATIONS)
            .prizes(UPDATED_PRIZES)
            .photography(UPDATED_PHOTOGRAPHY)
            .transport(UPDATED_TRANSPORT)
            .stationery(UPDATED_STATIONERY)
            .guests(UPDATED_GUESTS)
            .other(UPDATED_OTHER);
        BudgetDTO budgetDTO = budgetMapper.toDto(updatedBudget);

        restBudgetMockMvc.perform(put("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isOk());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getFood()).isEqualTo(UPDATED_FOOD);
        assertThat(testBudget.getDecorations()).isEqualTo(UPDATED_DECORATIONS);
        assertThat(testBudget.getPrizes()).isEqualTo(UPDATED_PRIZES);
        assertThat(testBudget.getPhotography()).isEqualTo(UPDATED_PHOTOGRAPHY);
        assertThat(testBudget.getTransport()).isEqualTo(UPDATED_TRANSPORT);
        assertThat(testBudget.getStationery()).isEqualTo(UPDATED_STATIONERY);
        assertThat(testBudget.getGuests()).isEqualTo(UPDATED_GUESTS);
        assertThat(testBudget.getOther()).isEqualTo(UPDATED_OTHER);

        // Validate the Budget in Elasticsearch
        Budget budgetEs = budgetSearchRepository.findOne(testBudget.getId());
        assertThat(budgetEs).isEqualToComparingFieldByField(testBudget);
    }

    @Test
    @Transactional
    public void updateNonExistingBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBudgetMockMvc.perform(put("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        budgetSearchRepository.save(budget);
        int databaseSizeBeforeDelete = budgetRepository.findAll().size();

        // Get the budget
        restBudgetMockMvc.perform(delete("/api/budgets/{id}", budget.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean budgetExistsInEs = budgetSearchRepository.exists(budget.getId());
        assertThat(budgetExistsInEs).isFalse();

        // Validate the database is empty
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        budgetSearchRepository.save(budget);

        // Search the budget
        restBudgetMockMvc.perform(get("/api/_search/budgets?query=id:" + budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].food").value(hasItem(DEFAULT_FOOD.doubleValue())))
            .andExpect(jsonPath("$.[*].decorations").value(hasItem(DEFAULT_DECORATIONS.doubleValue())))
            .andExpect(jsonPath("$.[*].prizes").value(hasItem(DEFAULT_PRIZES.doubleValue())))
            .andExpect(jsonPath("$.[*].photography").value(hasItem(DEFAULT_PHOTOGRAPHY.doubleValue())))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.doubleValue())))
            .andExpect(jsonPath("$.[*].stationery").value(hasItem(DEFAULT_STATIONERY.doubleValue())))
            .andExpect(jsonPath("$.[*].guests").value(hasItem(DEFAULT_GUESTS.doubleValue())))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = new Budget();
        budget1.setId(1L);
        Budget budget2 = new Budget();
        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);
        budget2.setId(2L);
        assertThat(budget1).isNotEqualTo(budget2);
        budget1.setId(null);
        assertThat(budget1).isNotEqualTo(budget2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetDTO.class);
        BudgetDTO budgetDTO1 = new BudgetDTO();
        budgetDTO1.setId(1L);
        BudgetDTO budgetDTO2 = new BudgetDTO();
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
        budgetDTO2.setId(budgetDTO1.getId());
        assertThat(budgetDTO1).isEqualTo(budgetDTO2);
        budgetDTO2.setId(2L);
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
        budgetDTO1.setId(null);
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(budgetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(budgetMapper.fromId(null)).isNull();
    }
}
