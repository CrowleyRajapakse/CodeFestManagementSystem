package com.csse.codefest.service;

import com.csse.codefest.service.dto.ApplicationFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ApplicationForm.
 */
public interface ApplicationFormService {

    /**
     * Save a applicationForm.
     *
     * @param applicationFormDTO the entity to save
     * @return the persisted entity
     */
    ApplicationFormDTO save(ApplicationFormDTO applicationFormDTO);

    /**
     *  Get all the applicationForms.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ApplicationFormDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" applicationForm.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ApplicationFormDTO findOne(Long id);

    /**
     *  Delete the "id" applicationForm.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the applicationForm corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ApplicationFormDTO> search(String query, Pageable pageable);
}
