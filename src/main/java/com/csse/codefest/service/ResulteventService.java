package com.csse.codefest.service;

import com.csse.codefest.service.dto.ResulteventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Resultevent.
 */
public interface ResulteventService {

    /**
     * Save a resultevent.
     *
     * @param resulteventDTO the entity to save
     * @return the persisted entity
     */
    ResulteventDTO save(ResulteventDTO resulteventDTO);

    /**
     *  Get all the resultevents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResulteventDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" resultevent.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ResulteventDTO findOne(Long id);

    /**
     *  Delete the "id" resultevent.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the resultevent corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ResulteventDTO> search(String query, Pageable pageable);
}
