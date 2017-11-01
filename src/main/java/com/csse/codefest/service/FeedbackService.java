package com.csse.codefest.service;

import com.csse.codefest.service.dto.FeedbackDTO;
import java.util.List;

/**
 * Service Interface for managing Feedback.
 */
public interface FeedbackService {

    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save
     * @return the persisted entity
     */
    FeedbackDTO save(FeedbackDTO feedbackDTO);

    /**
     *  Get all the feedbacks.
     *
     *  @return the list of entities
     */
    List<FeedbackDTO> findAll();

    /**
     *  Get the "id" feedback.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FeedbackDTO findOne(Long id);

    /**
     *  Delete the "id" feedback.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the feedback corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<FeedbackDTO> search(String query);
}
