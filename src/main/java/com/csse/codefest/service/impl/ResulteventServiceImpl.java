package com.csse.codefest.service.impl;

import com.csse.codefest.service.ResulteventService;
import com.csse.codefest.domain.Resultevent;
import com.csse.codefest.repository.ResulteventRepository;
import com.csse.codefest.repository.search.ResulteventSearchRepository;
import com.csse.codefest.service.dto.ResulteventDTO;
import com.csse.codefest.service.mapper.ResulteventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Resultevent.
 */
@Service
@Transactional
public class ResulteventServiceImpl implements ResulteventService{

    private final Logger log = LoggerFactory.getLogger(ResulteventServiceImpl.class);

    private final ResulteventRepository resulteventRepository;

    private final ResulteventMapper resulteventMapper;

    private final ResulteventSearchRepository resulteventSearchRepository;

    public ResulteventServiceImpl(ResulteventRepository resulteventRepository, ResulteventMapper resulteventMapper, ResulteventSearchRepository resulteventSearchRepository) {
        this.resulteventRepository = resulteventRepository;
        this.resulteventMapper = resulteventMapper;
        this.resulteventSearchRepository = resulteventSearchRepository;
    }

    /**
     * Save a resultevent.
     *
     * @param resulteventDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResulteventDTO save(ResulteventDTO resulteventDTO) {
        log.debug("Request to save Resultevent : {}", resulteventDTO);
        Resultevent resultevent = resulteventMapper.toEntity(resulteventDTO);
        resultevent = resulteventRepository.save(resultevent);
        ResulteventDTO result = resulteventMapper.toDto(resultevent);
        resulteventSearchRepository.save(resultevent);
        return result;
    }

    /**
     *  Get all the resultevents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResulteventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resultevents");
        return resulteventRepository.findAll(pageable)
            .map(resulteventMapper::toDto);
    }

    /**
     *  Get one resultevent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ResulteventDTO findOne(Long id) {
        log.debug("Request to get Resultevent : {}", id);
        Resultevent resultevent = resulteventRepository.findOne(id);
        return resulteventMapper.toDto(resultevent);
    }

    /**
     *  Delete the  resultevent by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resultevent : {}", id);
        resulteventRepository.delete(id);
        resulteventSearchRepository.delete(id);
    }

    /**
     * Search for the resultevent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResulteventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Resultevents for query {}", query);
        Page<Resultevent> result = resulteventSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(resulteventMapper::toDto);
    }
}
