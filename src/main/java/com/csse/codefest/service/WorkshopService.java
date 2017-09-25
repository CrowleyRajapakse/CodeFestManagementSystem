package com.csse.codefest.service;

import com.csse.codefest.service.dto.WorkshopDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Workshop.
 */
public interface WorkshopService {

    /**
     * Save a workshop.
     *
     * @param workshopDTO the entity to save
     * @return the persisted entity
     */
    WorkshopDTO save(WorkshopDTO workshopDTO);

    /**
     *  Get all the workshops.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkshopDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" workshop.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkshopDTO findOne(Long id);

    /**
     *  Delete the "id" workshop.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
