package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Resultevent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Resultevent entity.
 */
public interface ResulteventSearchRepository extends ElasticsearchRepository<Resultevent, Long> {
}
