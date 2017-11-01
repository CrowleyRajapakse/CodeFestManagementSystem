package com.csse.codefest.service.impl;

import com.csse.codefest.service.FeedbackService;
import com.csse.codefest.domain.Feedback;
import com.csse.codefest.repository.FeedbackRepository;
import com.csse.codefest.repository.search.FeedbackSearchRepository;
import com.csse.codefest.service.dto.FeedbackDTO;
import com.csse.codefest.service.mapper.FeedbackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Feedback.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService{

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final FeedbackSearchRepository feedbackSearchRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper, FeedbackSearchRepository feedbackSearchRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.feedbackSearchRepository = feedbackSearchRepository;
    }

    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        log.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        FeedbackDTO result = feedbackMapper.toDto(feedback);
        feedbackSearchRepository.save(feedback);
        return result;
    }

    /**
     *  Get all the feedbacks.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> findAll() {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll().stream()
            .map(feedbackMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one feedback by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FeedbackDTO findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        Feedback feedback = feedbackRepository.findOne(id);
        return feedbackMapper.toDto(feedback);
    }

    /**
     *  Delete the  feedback by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        feedbackRepository.delete(id);
        feedbackSearchRepository.delete(id);
    }

    /**
     * Search for the feedback corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> search(String query) {
        log.debug("Request to search Feedbacks for query {}", query);
        return StreamSupport
            .stream(feedbackSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(feedbackMapper::toDto)
            .collect(Collectors.toList());
    }
}
