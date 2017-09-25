package com.csse.codefest.service;

import com.csse.codefest.service.dto.CompetitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Competition.
 */
public interface CompetitionService {

    /**
     * Save a competition.
     *
     * @param competitionDTO the entity to save
     * @return the persisted entity
     */
    CompetitionDTO save(CompetitionDTO competitionDTO);

    /**
     *  Get all the competitions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CompetitionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" competition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CompetitionDTO findOne(Long id);

    /**
     *  Delete the "id" competition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
