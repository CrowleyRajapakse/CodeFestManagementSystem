package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.ApplicationFormService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.ApplicationFormDTO;
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
 * REST controller for managing ApplicationForm.
 */
@RestController
@RequestMapping("/api")
public class ApplicationFormResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationFormResource.class);

    private static final String ENTITY_NAME = "applicationForm";

    private final ApplicationFormService applicationFormService;

    public ApplicationFormResource(ApplicationFormService applicationFormService) {
        this.applicationFormService = applicationFormService;
    }

    /**
     * POST  /application-forms : Create a new applicationForm.
     *
     * @param applicationFormDTO the applicationFormDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationFormDTO, or with status 400 (Bad Request) if the applicationForm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-forms")
    @Timed
    public ResponseEntity<ApplicationFormDTO> createApplicationForm(@Valid @RequestBody ApplicationFormDTO applicationFormDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationForm : {}", applicationFormDTO);
        if (applicationFormDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new applicationForm cannot already have an ID")).body(null);
        }
        ApplicationFormDTO result = applicationFormService.save(applicationFormDTO);
        return ResponseEntity.created(new URI("/api/application-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-forms : Updates an existing applicationForm.
     *
     * @param applicationFormDTO the applicationFormDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationFormDTO,
     * or with status 400 (Bad Request) if the applicationFormDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationFormDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-forms")
    @Timed
    public ResponseEntity<ApplicationFormDTO> updateApplicationForm(@Valid @RequestBody ApplicationFormDTO applicationFormDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationForm : {}", applicationFormDTO);
        if (applicationFormDTO.getId() == null) {
            return createApplicationForm(applicationFormDTO);
        }
        ApplicationFormDTO result = applicationFormService.save(applicationFormDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationFormDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-forms : get all the applicationForms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of applicationForms in body
     */
    @GetMapping("/application-forms")
    @Timed
    public ResponseEntity<List<ApplicationFormDTO>> getAllApplicationForms(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ApplicationForms");
        Page<ApplicationFormDTO> page = applicationFormService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/application-forms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /application-forms/:id : get the "id" applicationForm.
     *
     * @param id the id of the applicationFormDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationFormDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-forms/{id}")
    @Timed
    public ResponseEntity<ApplicationFormDTO> getApplicationForm(@PathVariable Long id) {
        log.debug("REST request to get ApplicationForm : {}", id);
        ApplicationFormDTO applicationFormDTO = applicationFormService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationFormDTO));
    }

    /**
     * DELETE  /application-forms/:id : delete the "id" applicationForm.
     *
     * @param id the id of the applicationFormDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-forms/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationForm(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationForm : {}", id);
        applicationFormService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/application-forms?query=:query : search for the applicationForm corresponding
     * to the query.
     *
     * @param query the query of the applicationForm search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/application-forms")
    @Timed
    public ResponseEntity<List<ApplicationFormDTO>> searchApplicationForms(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ApplicationForms for query {}", query);
        Page<ApplicationFormDTO> page = applicationFormService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/application-forms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
