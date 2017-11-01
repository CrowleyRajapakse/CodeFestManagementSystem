package com.csse.codefest.repository.search;

import com.csse.codefest.domain.ApplicationForm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ApplicationForm entity.
 */
public interface ApplicationFormSearchRepository extends ElasticsearchRepository<ApplicationForm, Long> {
}
