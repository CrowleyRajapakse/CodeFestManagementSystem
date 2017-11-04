package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.MailService;
import com.csse.codefest.service.CompetitorService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.CompetitorDTO;
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
 * REST controller for managing Competitor.
 */
@RestController
@RequestMapping("/api")
public class CompetitorResource {

    private final Logger log = LoggerFactory.getLogger(CompetitorResource.class);

    private static final String ENTITY_NAME = "competitor";

    private final CompetitorService competitorService;

    private final MailService mailService;

    public CompetitorResource(CompetitorService competitorService, MailService mailService) {
        this.competitorService = competitorService;
        this.mailService = mailService;
    }

    /**
     * POST  /competitors : Create a new competitor.
     *
     * @param competitorDTO the competitorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competitorDTO, or with status 400 (Bad Request) if the competitor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/competitors")
    @Timed
    public ResponseEntity<CompetitorDTO> createCompetitor(@Valid @RequestBody CompetitorDTO competitorDTO) throws URISyntaxException {
        log.debug("REST request to save Competitor : {}", competitorDTO);
        if (competitorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new competitor cannot already have an ID")).body(null);
        }
        CompetitorDTO result = competitorService.save(competitorDTO);
        mailService.sendGetDetailsMail(competitorDTO.getName(), competitorDTO.getEmail(), competitorDTO.getEventsName(), competitorDTO.getAge(), competitorDTO.getPhone(), competitorDTO.getCategory());
        return ResponseEntity.created(new URI("/api/competitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competitors : Updates an existing competitor.
     *
     * @param competitorDTO the competitorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competitorDTO,
     * or with status 400 (Bad Request) if the competitorDTO is not valid,
     * or with status 500 (Internal Server Error) if the competitorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/competitors")
    @Timed
    public ResponseEntity<CompetitorDTO> updateCompetitor(@Valid @RequestBody CompetitorDTO competitorDTO) throws URISyntaxException {
        log.debug("REST request to update Competitor : {}", competitorDTO);
        if (competitorDTO.getId() == null) {
            return createCompetitor(competitorDTO);
        }
        CompetitorDTO result = competitorService.save(competitorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, competitorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competitors : get all the competitors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of competitors in body
     */
    @GetMapping("/competitors")
    @Timed
    public ResponseEntity<List<CompetitorDTO>> getAllCompetitors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Competitors");
        Page<CompetitorDTO> page = competitorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competitors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competitors/:id : get the "id" competitor.
     *
     * @param id the id of the competitorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competitorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/competitors/{id}")
    @Timed
    public ResponseEntity<CompetitorDTO> getCompetitor(@PathVariable Long id) {
        log.debug("REST request to get Competitor : {}", id);
        CompetitorDTO competitorDTO = competitorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(competitorDTO));
    }

    /**
     * DELETE  /competitors/:id : delete the "id" competitor.
     *
     * @param id the id of the competitorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/competitors/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompetitor(@PathVariable Long id) {
        log.debug("REST request to delete Competitor : {}", id);
        competitorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/competitors?query=:query : search for the competitor corresponding
     * to the query.
     *
     * @param query    the query of the competitor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/competitors")
    @Timed
    public ResponseEntity<List<CompetitorDTO>> searchCompetitors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Competitors for query {}", query);
        Page<CompetitorDTO> page = competitorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/competitors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
