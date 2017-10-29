package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Competition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Competition entity.
 */
public interface CompetitionSearchRepository extends ElasticsearchRepository<Competition, Long> {
}
