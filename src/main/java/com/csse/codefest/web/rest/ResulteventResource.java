package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.ResulteventService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.ResulteventDTO;
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
 * REST controller for managing Resultevent.
 */
@RestController
@RequestMapping("/api")
public class ResulteventResource {

    private final Logger log = LoggerFactory.getLogger(ResulteventResource.class);

    private static final String ENTITY_NAME = "resultevent";

    private final ResulteventService resulteventService;

    public ResulteventResource(ResulteventService resulteventService) {
        this.resulteventService = resulteventService;
    }

    /**
     * POST  /resultevents : Create a new resultevent.
     *
     * @param resulteventDTO the resulteventDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resulteventDTO, or with status 400 (Bad Request) if the resultevent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resultevents")
    @Timed
    public ResponseEntity<ResulteventDTO> createResultevent(@Valid @RequestBody ResulteventDTO resulteventDTO) throws URISyntaxException {
        log.debug("REST request to save Resultevent : {}", resulteventDTO);
        if (resulteventDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resultevent cannot already have an ID")).body(null);
        }
        ResulteventDTO result = resulteventService.save(resulteventDTO);
        return ResponseEntity.created(new URI("/api/resultevents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resultevents : Updates an existing resultevent.
     *
     * @param resulteventDTO the resulteventDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resulteventDTO,
     * or with status 400 (Bad Request) if the resulteventDTO is not valid,
     * or with status 500 (Internal Server Error) if the resulteventDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resultevents")
    @Timed
    public ResponseEntity<ResulteventDTO> updateResultevent(@Valid @RequestBody ResulteventDTO resulteventDTO) throws URISyntaxException {
        log.debug("REST request to update Resultevent : {}", resulteventDTO);
        if (resulteventDTO.getId() == null) {
            return createResultevent(resulteventDTO);
        }
        ResulteventDTO result = resulteventService.save(resulteventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resulteventDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resultevents : get all the resultevents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resultevents in body
     */
    @GetMapping("/resultevents")
    @Timed
    public ResponseEntity<List<ResulteventDTO>> getAllResultevents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Resultevents");
        Page<ResulteventDTO> page = resulteventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resultevents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resultevents/:id : get the "id" resultevent.
     *
     * @param id the id of the resulteventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resulteventDTO, or with status 404 (Not Found)
     */
    @GetMapping("/resultevents/{id}")
    @Timed
    public ResponseEntity<ResulteventDTO> getResultevent(@PathVariable Long id) {
        log.debug("REST request to get Resultevent : {}", id);
        ResulteventDTO resulteventDTO = resulteventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resulteventDTO));
    }

    /**
     * DELETE  /resultevents/:id : delete the "id" resultevent.
     *
     * @param id the id of the resulteventDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resultevents/{id}")
    @Timed
    public ResponseEntity<Void> deleteResultevent(@PathVariable Long id) {
        log.debug("REST request to delete Resultevent : {}", id);
        resulteventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/resultevents?query=:query : search for the resultevent corresponding
     * to the query.
     *
     * @param query the query of the resultevent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/resultevents")
    @Timed
    public ResponseEntity<List<ResulteventDTO>> searchResultevents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Resultevents for query {}", query);
        Page<ResulteventDTO> page = resulteventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/resultevents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
