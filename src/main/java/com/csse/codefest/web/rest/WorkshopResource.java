package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.WorkshopService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.WorkshopDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Workshop.
 */
@RestController
@RequestMapping("/api")
public class WorkshopResource {

    private final Logger log = LoggerFactory.getLogger(WorkshopResource.class);

    private static final String ENTITY_NAME = "workshop";

    private final WorkshopService workshopService;

    public WorkshopResource(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    /**
     * POST  /workshops : Create a new workshop.
     *
     * @param workshopDTO the workshopDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workshopDTO, or with status 400 (Bad Request) if the workshop has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workshops")
    @Timed
    public ResponseEntity<WorkshopDTO> createWorkshop(@Valid @RequestBody WorkshopDTO workshopDTO) throws URISyntaxException {
        log.debug("REST request to save Workshop : {}", workshopDTO);
        if (workshopDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workshop cannot already have an ID")).body(null);
        }
        WorkshopDTO result = workshopService.save(workshopDTO);
        return ResponseEntity.created(new URI("/api/workshops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workshops : Updates an existing workshop.
     *
     * @param workshopDTO the workshopDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workshopDTO,
     * or with status 400 (Bad Request) if the workshopDTO is not valid,
     * or with status 500 (Internal Server Error) if the workshopDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workshops")
    @Timed
    public ResponseEntity<WorkshopDTO> updateWorkshop(@Valid @RequestBody WorkshopDTO workshopDTO) throws URISyntaxException {
        log.debug("REST request to update Workshop : {}", workshopDTO);
        if (workshopDTO.getId() == null) {
            return createWorkshop(workshopDTO);
        }
        WorkshopDTO result = workshopService.save(workshopDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workshopDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workshops : get all the workshops.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workshops in body
     */
    @GetMapping("/workshops")
    @Timed
    public ResponseEntity<List<WorkshopDTO>> getAllWorkshops(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Workshops");
        Page<WorkshopDTO> page = workshopService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workshops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workshops/:id : get the "id" workshop.
     *
     * @param id the id of the workshopDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workshopDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workshops/{id}")
    @Timed
    public ResponseEntity<WorkshopDTO> getWorkshop(@PathVariable Long id) {
        log.debug("REST request to get Workshop : {}", id);
        WorkshopDTO workshopDTO = workshopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workshopDTO));
    }

    /**
     * DELETE  /workshops/:id : delete the "id" workshop.
     *
     * @param id the id of the workshopDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workshops/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long id) {
        log.debug("REST request to delete Workshop : {}", id);
        workshopService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/workshops?query=:query : search for the workshop corresponding
     * to the query.
     *
     * @param query the query of the workshop search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/workshops")
    @Timed
    public ResponseEntity<List<WorkshopDTO>> searchWorkshops(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Workshops for query {}", query);
        Page<WorkshopDTO> page = workshopService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/workshops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
