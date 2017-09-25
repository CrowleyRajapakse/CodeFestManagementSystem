package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.CompetitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Competition and its DTO CompetitionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompetitionMapper extends EntityMapper <CompetitionDTO, Competition> {
    
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "workshops", ignore = true)
    Competition toEntity(CompetitionDTO competitionDTO); 
    default Competition fromId(Long id) {
        if (id == null) {
            return null;
        }
        Competition competition = new Competition();
        competition.setId(id);
        return competition;
    }
}
