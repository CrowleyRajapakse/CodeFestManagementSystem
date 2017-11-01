package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Competitor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Competitor entity.
 */
public interface CompetitorSearchRepository extends ElasticsearchRepository<Competitor, Long> {
}
