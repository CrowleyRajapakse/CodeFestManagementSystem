package com.csse.codefest.repository.search;

import com.csse.codefest.domain.Budget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Budget entity.
 */
public interface BudgetSearchRepository extends ElasticsearchRepository<Budget, Long> {
}
