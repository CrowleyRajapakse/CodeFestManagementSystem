package com.csse.codefest.service.impl;

import com.csse.codefest.service.WorkshopService;
import com.csse.codefest.domain.Workshop;
import com.csse.codefest.repository.WorkshopRepository;
import com.csse.codefest.service.dto.WorkshopDTO;
import com.csse.codefest.service.mapper.WorkshopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Workshop.
 */
@Service
@Transactional
public class WorkshopServiceImpl implements WorkshopService{

    private final Logger log = LoggerFactory.getLogger(WorkshopServiceImpl.class);

    private final WorkshopRepository workshopRepository;

    private final WorkshopMapper workshopMapper;

    public WorkshopServiceImpl(WorkshopRepository workshopRepository, WorkshopMapper workshopMapper) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
    }

    /**
     * Save a workshop.
     *
     * @param workshopDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkshopDTO save(WorkshopDTO workshopDTO) {
        log.debug("Request to save Workshop : {}", workshopDTO);
        Workshop workshop = workshopMapper.toEntity(workshopDTO);
        workshop = workshopRepository.save(workshop);
        return workshopMapper.toDto(workshop);
    }

    /**
     *  Get all the workshops.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workshops");
        return workshopRepository.findAll(pageable)
            .map(workshopMapper::toDto);
    }

    /**
     *  Get one workshop by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WorkshopDTO findOne(Long id) {
        log.debug("Request to get Workshop : {}", id);
        Workshop workshop = workshopRepository.findOne(id);
        return workshopMapper.toDto(workshop);
    }

    /**
     *  Delete the  workshop by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workshop : {}", id);
        workshopRepository.delete(id);
    }
}
