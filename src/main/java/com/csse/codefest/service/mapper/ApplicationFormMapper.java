package com.csse.codefest.service.mapper;

import com.csse.codefest.domain.*;
import com.csse.codefest.service.dto.ApplicationFormDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationForm and its DTO ApplicationFormDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationFormMapper extends EntityMapper <ApplicationFormDTO, ApplicationForm> {
    
    
    default ApplicationForm fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationForm applicationForm = new ApplicationForm();
        applicationForm.setId(id);
        return applicationForm;
    }
}
