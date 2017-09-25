package com.csse.codefest.repository;

import com.csse.codefest.domain.Workshop;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Workshop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

}
