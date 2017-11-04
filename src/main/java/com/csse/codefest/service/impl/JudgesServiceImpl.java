package com.csse.codefest.service.impl;

import com.csse.codefest.service.JudgesService;
import com.csse.codefest.domain.Judges;
import com.csse.codefest.repository.JudgesRepository;
import com.csse.codefest.repository.search.JudgesSearchRepository;
import com.csse.codefest.service.dto.JudgesDTO;
import com.csse.codefest.service.mapper.JudgesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Judges.
 */
@Service
@Transactional
public class JudgesServiceImpl implements JudgesService{

    private final Logger log = LoggerFactory.getLogger(JudgesServiceImpl.class);

    private final JudgesRepository judgesRepository;

    private final JudgesMapper judgesMapper;

    private final JudgesSearchRepository judgesSearchRepository;

    public JudgesServiceImpl(JudgesRepository judgesRepository, JudgesMapper judgesMapper, JudgesSearchRepository judgesSearchRepository) {
        this.judgesRepository = judgesRepository;
        this.judgesMapper = judgesMapper;
        this.judgesSearchRepository = judgesSearchRepository;
    }

    /**
     * Save a judges.
     *
     * @param judgesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JudgesDTO save(JudgesDTO judgesDTO) {
        log.debug("Request to save Judges : {}", judgesDTO);
        Judges judges = judgesMapper.toEntity(judgesDTO);
        judges = judgesRepository.save(judges);
        JudgesDTO result = judgesMapper.toDto(judges);
        judgesSearchRepository.save(judges);
        return result;
    }

    /**
     *  Get all the judges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JudgesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Judges");
        return judgesRepository.findAll(pageable)
            .map(judgesMapper::toDto);
    }

    /**
     *  Get one judges by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JudgesDTO findOne(Long id) {
        log.debug("Request to get Judges : {}", id);
        Judges judges = judgesRepository.findOne(id);
        return judgesMapper.toDto(judges);
    }

    /**
     *  Delete the  judges by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Judges : {}", id);
        judgesRepository.delete(id);
        judgesSearchRepository.delete(id);
    }

    /**
     * Search for the judges corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JudgesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Judges for query {}", query);
        Page<Judges> result = judgesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(judgesMapper::toDto);
    }
}
