package com.csse.codefest.web.rest;

import com.csse.codefest.service.FeedbackService;
import com.csse.codefest.service.MailService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.service.dto.FeedbackDTO;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Feedback.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

    private static final String ENTITY_NAME = "feedback";

    private final FeedbackService feedbackService;

    private final MailService mailService;

    public FeedbackResource(FeedbackService feedbackService, MailService mailService) {
        this.feedbackService = feedbackService;
        this.mailService = mailService;
    }

    /**
     * POST  /feedbacks : Create a new feedback.
     *
     * @param feedbackDTO the feedbackDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feedbackDTO, or with status 400 (Bad Request) if the feedback has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feedbacks")
    @Timed
    public ResponseEntity<FeedbackDTO> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) throws URISyntaxException {
        log.debug("REST request to save Feedback : {}", feedbackDTO);
        if (feedbackDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new feedback cannot already have an ID")).body(null);
        }
        FeedbackDTO result = feedbackService.save(feedbackDTO);
        mailService.sendFeedbackMail(feedbackDTO.getRecipient(), feedbackDTO.getEmail(), feedbackDTO.getMessage());
        return ResponseEntity.created(new URI("/api/feedbacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /feedbacks : Updates an existing feedback.
     *
     * @param feedbackDTO the feedbackDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feedbackDTO,
     * or with status 400 (Bad Request) if the feedbackDTO is not valid,
     * or with status 500 (Internal Server Error) if the feedbackDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/feedbacks")
    @Timed
    public ResponseEntity<FeedbackDTO> updateFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) throws URISyntaxException {
        log.debug("REST request to update Feedback : {}", feedbackDTO);
        if (feedbackDTO.getId() == null) {
            return createFeedback(feedbackDTO);
        }
        FeedbackDTO result = feedbackService.save(feedbackDTO);
        mailService.sendFeedbackMail(feedbackDTO.getRecipient(), feedbackDTO.getEmail(), feedbackDTO.getMessage());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, feedbackDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /feedbacks : get all the feedbacks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of feedbacks in body
     */
    @GetMapping("/feedbacks")
    @Timed
    public List<FeedbackDTO> getAllFeedbacks() {
        log.debug("REST request to get all Feedbacks");
        return feedbackService.findAll();
    }

    /**
     * GET  /feedbacks/:id : get the "id" feedback.
     *
     * @param id the id of the feedbackDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feedbackDTO, or with status 404 (Not Found)
     */
    @GetMapping("/feedbacks/{id}")
    @Timed
    public ResponseEntity<FeedbackDTO> getFeedback(@PathVariable Long id) {
        log.debug("REST request to get Feedback : {}", id);
        FeedbackDTO feedbackDTO = feedbackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(feedbackDTO));
    }

    /**
     * DELETE  /feedbacks/:id : delete the "id" feedback.
     *
     * @param id the id of the feedbackDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feedbacks/{id}")
    @Timed
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        log.debug("REST request to delete Feedback : {}", id);
        feedbackService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/feedbacks?query=:query : search for the feedback corresponding
     * to the query.
     *
     * @param query the query of the feedback search
     * @return the result of the search
     */
    @GetMapping("/_search/feedbacks")
    @Timed
    public List<FeedbackDTO> searchFeedbacks(@RequestParam String query) {
        log.debug("REST request to search Feedbacks for query {}", query);
        return feedbackService.search(query);
    }

}
