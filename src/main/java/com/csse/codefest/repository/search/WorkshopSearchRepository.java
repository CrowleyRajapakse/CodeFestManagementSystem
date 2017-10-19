package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Workshop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Workshop entity.
 */
public interface WorkshopSearchRepository extends ElasticsearchRepository<Workshop, Long> {
}
