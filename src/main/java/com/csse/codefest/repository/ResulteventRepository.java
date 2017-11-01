package com.csse.codefest.repository;

import com.csse.codefest.domain.Resultevent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Resultevent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResulteventRepository extends JpaRepository<Resultevent, Long> {

}
