package com.csse.codefest.service;

import com.csse.codefest.service.dto.JudgesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Judges.
 */
public interface JudgesService {

    /**
     * Save a judges.
     *
     * @param judgesDTO the entity to save
     * @return the persisted entity
     */
    JudgesDTO save(JudgesDTO judgesDTO);

    /**
     *  Get all the judges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JudgesDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" judges.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JudgesDTO findOne(Long id);

    /**
     *  Delete the "id" judges.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the judges corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JudgesDTO> search(String query, Pageable pageable);
}
