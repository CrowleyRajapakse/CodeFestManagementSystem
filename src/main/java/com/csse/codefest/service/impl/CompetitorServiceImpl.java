package com.csse.codefest.service.impl;

import com.csse.codefest.service.CompetitorService;
import com.csse.codefest.domain.Competitor;
import com.csse.codefest.repository.CompetitorRepository;
import com.csse.codefest.repository.search.CompetitorSearchRepository;
import com.csse.codefest.service.dto.CompetitorDTO;
import com.csse.codefest.service.mapper.CompetitorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Competitor.
 */
@Service
@Transactional
public class CompetitorServiceImpl implements CompetitorService{

    private final Logger log = LoggerFactory.getLogger(CompetitorServiceImpl.class);

    private final CompetitorRepository competitorRepository;

    private final CompetitorMapper competitorMapper;

    private final CompetitorSearchRepository competitorSearchRepository;

    public CompetitorServiceImpl(CompetitorRepository competitorRepository, CompetitorMapper competitorMapper, CompetitorSearchRepository competitorSearchRepository) {
        this.competitorRepository = competitorRepository;
        this.competitorMapper = competitorMapper;
        this.competitorSearchRepository = competitorSearchRepository;
    }

    /**
     * Save a competitor.
     *
     * @param competitorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CompetitorDTO save(CompetitorDTO competitorDTO) {
        log.debug("Request to save Competitor : {}", competitorDTO);
        Competitor competitor = competitorMapper.toEntity(competitorDTO);
        competitor = competitorRepository.save(competitor);
        CompetitorDTO result = competitorMapper.toDto(competitor);
        competitorSearchRepository.save(competitor);
        return result;
    }

    /**
     *  Get all the competitors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Competitors");
        return competitorRepository.findAll(pageable)
            .map(competitorMapper::toDto);
    }

    /**
     *  Get one competitor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CompetitorDTO findOne(Long id) {
        log.debug("Request to get Competitor : {}", id);
        Competitor competitor = competitorRepository.findOne(id);
        return competitorMapper.toDto(competitor);
    }

    /**
     *  Delete the  competitor by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Competitor : {}", id);
        competitorRepository.delete(id);
        competitorSearchRepository.delete(id);
    }

    /**
     * Search for the competitor corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Competitors for query {}", query);
        Page<Competitor> result = competitorSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(competitorMapper::toDto);
    }
}
