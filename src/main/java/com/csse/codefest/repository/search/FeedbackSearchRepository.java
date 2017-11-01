package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Feedback;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Feedback entity.
 */
public interface FeedbackSearchRepository extends ElasticsearchRepository<Feedback, Long> {
}
