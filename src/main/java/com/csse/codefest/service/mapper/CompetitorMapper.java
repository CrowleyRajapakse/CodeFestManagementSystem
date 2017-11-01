package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.CompetitorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Competitor and its DTO CompetitorDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, })
public interface CompetitorMapper extends EntityMapper <CompetitorDTO, Competitor> {

    @Mapping(source = "events.id", target = "eventsId")
    @Mapping(source = "events.name", target = "eventsName")
    CompetitorDTO toDto(Competitor competitor); 

    @Mapping(source = "eventsId", target = "events")
    Competitor toEntity(CompetitorDTO competitorDTO); 
    default Competitor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Competitor competitor = new Competitor();
        competitor.setId(id);
        return competitor;
    }
}
