package com.csse.codefest.repository;

import com.csse.codefest.domain.ApplicationForm;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {

}
