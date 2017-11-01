package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.WorkshopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Workshop and its DTO WorkshopDTO.
 */
@Mapper(componentModel = "spring", uses = {CompetitionMapper.class, })
public interface WorkshopMapper extends EntityMapper <WorkshopDTO, Workshop> {

    @Mapping(source = "competition.id", target = "competitionId")
    @Mapping(source = "competition.title", target = "competitionTitle")
    WorkshopDTO toDto(Workshop workshop); 

    @Mapping(source = "competitionId", target = "competition")
    Workshop toEntity(WorkshopDTO workshopDTO); 
    default Workshop fromId(Long id) {
        if (id == null) {
            return null;
        }
        Workshop workshop = new Workshop();
        workshop.setId(id);
        return workshop;
    }
}
