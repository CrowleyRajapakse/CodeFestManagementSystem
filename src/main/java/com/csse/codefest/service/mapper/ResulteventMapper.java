package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.ResulteventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resultevent and its DTO ResulteventDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, })
public interface ResulteventMapper extends EntityMapper <ResulteventDTO, Resultevent> {

    @Mapping(source = "eventresult.id", target = "eventresultId")
    @Mapping(source = "eventresult.name", target = "eventresultName")
    ResulteventDTO toDto(Resultevent resultevent); 

    @Mapping(source = "eventresultId", target = "eventresult")
    Resultevent toEntity(ResulteventDTO resulteventDTO); 
    default Resultevent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resultevent resultevent = new Resultevent();
        resultevent.setId(id);
        return resultevent;
    }
}
