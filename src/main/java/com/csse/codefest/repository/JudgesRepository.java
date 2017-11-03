package com.csse.codefest.repository;

import com.csse.codefest.domain.Judges;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Judges entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JudgesRepository extends JpaRepository<Judges, Long> {

}
