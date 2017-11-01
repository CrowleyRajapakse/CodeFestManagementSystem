package com.csse.codefest.service;

import com.csse.codefest.service.dto.CompetitorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Competitor.
 */
public interface CompetitorService {

    /**
     * Save a competitor.
     *
     * @param competitorDTO the entity to save
     * @return the persisted entity
     */
    CompetitorDTO save(CompetitorDTO competitorDTO);

    /**
     *  Get all the competitors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CompetitorDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" competitor.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CompetitorDTO findOne(Long id);

    /**
     *  Delete the "id" competitor.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the competitor corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CompetitorDTO> search(String query, Pageable pageable);
}
