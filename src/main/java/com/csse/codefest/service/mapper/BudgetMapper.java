package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.BudgetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Budget and its DTO BudgetDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, })
public interface BudgetMapper extends EntityMapper <BudgetDTO, Budget> {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    BudgetDTO toDto(Budget budget); 

    @Mapping(source = "eventId", target = "event")
    Budget toEntity(BudgetDTO budgetDTO); 
    default Budget fromId(Long id) {
        if (id == null) {
            return null;
        }
        Budget budget = new Budget();
        budget.setId(id);
        return budget;
    }
}
