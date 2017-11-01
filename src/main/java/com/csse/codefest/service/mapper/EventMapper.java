package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {CompetitionMapper.class, })
public interface EventMapper extends EntityMapper <EventDTO, Event> {

    @Mapping(source = "competition.id", target = "competitionId")
    @Mapping(source = "competition.title", target = "competitionTitle")
    EventDTO toDto(Event event); 

    @Mapping(source = "competitionId", target = "competition")
    Event toEntity(EventDTO eventDTO); 
    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
