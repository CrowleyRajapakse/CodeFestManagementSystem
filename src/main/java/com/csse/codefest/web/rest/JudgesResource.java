package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.JudgesService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.JudgesDTO;
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
 * REST controller for managing Judges.
 */
@RestController
@RequestMapping("/api")
public class JudgesResource {

    private final Logger log = LoggerFactory.getLogger(JudgesResource.class);

    private static final String ENTITY_NAME = "judges";

    private final JudgesService judgesService;

    public JudgesResource(JudgesService judgesService) {
        this.judgesService = judgesService;
    }

    /**
     * POST  /judges : Create a new judges.
     *
     * @param judgesDTO the judgesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new judgesDTO, or with status 400 (Bad Request) if the judges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/judges")
    @Timed
    public ResponseEntity<JudgesDTO> createJudges(@Valid @RequestBody JudgesDTO judgesDTO) throws URISyntaxException {
        log.debug("REST request to save Judges : {}", judgesDTO);
        if (judgesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new judges cannot already have an ID")).body(null);
        }
        JudgesDTO result = judgesService.save(judgesDTO);
        return ResponseEntity.created(new URI("/api/judges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /judges : Updates an existing judges.
     *
     * @param judgesDTO the judgesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated judgesDTO,
     * or with status 400 (Bad Request) if the judgesDTO is not valid,
     * or with status 500 (Internal Server Error) if the judgesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/judges")
    @Timed
    public ResponseEntity<JudgesDTO> updateJudges(@Valid @RequestBody JudgesDTO judgesDTO) throws URISyntaxException {
        log.debug("REST request to update Judges : {}", judgesDTO);
        if (judgesDTO.getId() == null) {
            return createJudges(judgesDTO);
        }
        JudgesDTO result = judgesService.save(judgesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, judgesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /judges : get all the judges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of judges in body
     */
    @GetMapping("/judges")
    @Timed
    public ResponseEntity<List<JudgesDTO>> getAllJudges(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Judges");
        Page<JudgesDTO> page = judgesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/judges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /judges/:id : get the "id" judges.
     *
     * @param id the id of the judgesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the judgesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/judges/{id}")
    @Timed
    public ResponseEntity<JudgesDTO> getJudges(@PathVariable Long id) {
        log.debug("REST request to get Judges : {}", id);
        JudgesDTO judgesDTO = judgesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(judgesDTO));
    }

    /**
     * DELETE  /judges/:id : delete the "id" judges.
     *
     * @param id the id of the judgesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/judges/{id}")
    @Timed
    public ResponseEntity<Void> deleteJudges(@PathVariable Long id) {
        log.debug("REST request to delete Judges : {}", id);
        judgesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/judges?query=:query : search for the judges corresponding
     * to the query.
     *
     * @param query the query of the judges search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/judges")
    @Timed
    public ResponseEntity<List<JudgesDTO>> searchJudges(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Judges for query {}", query);
        Page<JudgesDTO> page = judgesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/judges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
