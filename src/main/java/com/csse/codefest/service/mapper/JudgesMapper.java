package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.JudgesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Judges and its DTO JudgesDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, })
public interface JudgesMapper extends EntityMapper <JudgesDTO, Judges> {

    @Mapping(source = "events.id", target = "eventsId")
    @Mapping(source = "events.name", target = "eventsName")
    JudgesDTO toDto(Judges judges); 

    @Mapping(source = "eventsId", target = "events")
    Judges toEntity(JudgesDTO judgesDTO); 
    default Judges fromId(Long id) {
        if (id == null) {
            return null;
        }
        Judges judges = new Judges();
        judges.setId(id);
        return judges;
    }
}
