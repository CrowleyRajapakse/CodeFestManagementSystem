package com.csse.codefest.service.impl;

import com.csse.codefest.service.ApplicationFormService;
import com.csse.codefest.domain.ApplicationForm;
import com.csse.codefest.repository.ApplicationFormRepository;
import com.csse.codefest.repository.search.ApplicationFormSearchRepository;
import com.csse.codefest.service.dto.ApplicationFormDTO;
import com.csse.codefest.service.mapper.ApplicationFormMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ApplicationForm.
 */
@Service
@Transactional
public class ApplicationFormServiceImpl implements ApplicationFormService{

    private final Logger log = LoggerFactory.getLogger(ApplicationFormServiceImpl.class);

    private final ApplicationFormRepository applicationFormRepository;

    private final ApplicationFormMapper applicationFormMapper;

    private final ApplicationFormSearchRepository applicationFormSearchRepository;

    public ApplicationFormServiceImpl(ApplicationFormRepository applicationFormRepository, ApplicationFormMapper applicationFormMapper, ApplicationFormSearchRepository applicationFormSearchRepository) {
        this.applicationFormRepository = applicationFormRepository;
        this.applicationFormMapper = applicationFormMapper;
        this.applicationFormSearchRepository = applicationFormSearchRepository;
    }

    /**
     * Save a applicationForm.
     *
     * @param applicationFormDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationFormDTO save(ApplicationFormDTO applicationFormDTO) {
        log.debug("Request to save ApplicationForm : {}", applicationFormDTO);
        ApplicationForm applicationForm = applicationFormMapper.toEntity(applicationFormDTO);
        applicationForm = applicationFormRepository.save(applicationForm);
        ApplicationFormDTO result = applicationFormMapper.toDto(applicationForm);
        applicationFormSearchRepository.save(applicationForm);
        return result;
    }

    /**
     *  Get all the applicationForms.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationFormDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationForms");
        return applicationFormRepository.findAll(pageable)
            .map(applicationFormMapper::toDto);
    }

    /**
     *  Get one applicationForm by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationFormDTO findOne(Long id) {
        log.debug("Request to get ApplicationForm : {}", id);
        ApplicationForm applicationForm = applicationFormRepository.findOne(id);
        return applicationFormMapper.toDto(applicationForm);
    }

    /**
     *  Delete the  applicationForm by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationForm : {}", id);
        applicationFormRepository.delete(id);
        applicationFormSearchRepository.delete(id);
    }

    /**
     * Search for the applicationForm corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationFormDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ApplicationForms for query {}", query);
        Page<ApplicationForm> result = applicationFormSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(applicationFormMapper::toDto);
    }
}
