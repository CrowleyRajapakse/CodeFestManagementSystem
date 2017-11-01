package com.csse.codefest.service;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.domain.*;
import com.csse.codefest.repository.*;
import com.csse.codefest.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final CompetitionRepository competitionRepository;

    private final CompetitionSearchRepository competitionSearchRepository;

    private final CompetitorRepository competitorRepository;

    private final CompetitorSearchRepository competitorSearchRepository;

    private final EventRepository eventRepository;

    private final EventSearchRepository eventSearchRepository;

    private final FeedbackRepository feedbackRepository;

    private final FeedbackSearchRepository feedbackSearchRepository;

    private final ResulteventRepository resulteventRepository;

    private final ResulteventSearchRepository resulteventSearchRepository;

    private final WorkshopRepository workshopRepository;

    private final WorkshopSearchRepository workshopSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        CompetitionRepository competitionRepository,
        CompetitionSearchRepository competitionSearchRepository,
        CompetitorRepository competitorRepository,
        CompetitorSearchRepository competitorSearchRepository,
        EventRepository eventRepository,
        EventSearchRepository eventSearchRepository,
        FeedbackRepository feedbackRepository,
        FeedbackSearchRepository feedbackSearchRepository,
        ResulteventRepository resulteventRepository,
        ResulteventSearchRepository resulteventSearchRepository,
        WorkshopRepository workshopRepository,
        WorkshopSearchRepository workshopSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.competitionRepository = competitionRepository;
        this.competitionSearchRepository = competitionSearchRepository;
        this.competitorRepository = competitorRepository;
        this.competitorSearchRepository = competitorSearchRepository;
        this.eventRepository = eventRepository;
        this.eventSearchRepository = eventSearchRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackSearchRepository = feedbackSearchRepository;
        this.resulteventRepository = resulteventRepository;
        this.resulteventSearchRepository = resulteventSearchRepository;
        this.workshopRepository = workshopRepository;
        this.workshopSearchRepository = workshopSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Competition.class, competitionRepository, competitionSearchRepository);
        reindexForClass(Competitor.class, competitorRepository, competitorSearchRepository);
        reindexForClass(Event.class, eventRepository, eventSearchRepository);
        reindexForClass(Feedback.class, feedbackRepository, feedbackSearchRepository);
        reindexForClass(Resultevent.class, resulteventRepository, resulteventSearchRepository);
        reindexForClass(Workshop.class, workshopRepository, workshopSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
