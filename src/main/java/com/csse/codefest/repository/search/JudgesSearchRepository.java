package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Judges;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Judges entity.
 */
public interface JudgesSearchRepository extends ElasticsearchRepository<Judges, Long> {
}
