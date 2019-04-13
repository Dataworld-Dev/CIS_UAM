package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.dto.ExternalUserAssistantDTO;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
import com.dw.ngms.cis.uam.repository.ExternalUserAssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/26.
 */

@Service
public class ExternalUserAssistantService {
    @Autowired
    private ExternalUserAssistantRepository externalUserAssistantRepository;

    public ExternalUserAssistant findByUserCodeName(ExternalUserAssistantDTO externalUserAssistant) {
        return this.externalUserAssistantRepository.findByUserCodeName(externalUserAssistant.getSurveyorusercode(),externalUserAssistant.getSurveyorusername(),externalUserAssistant.getAssistantusercode(),externalUserAssistant.getAssistantusername());
    }

    public ExternalUserAssistant updateExternalAssistant(ExternalUserAssistant externalUserAssistant) {
        return this.externalUserAssistantRepository.save(externalUserAssistant);
    }

    public void deleteAssistant(ExternalUserAssistant externalUserAssistant) {
         this.externalUserAssistantRepository.delete(externalUserAssistant);
    }


    public ExternalUserAssistant saveExternalAssistant(ExternalUserAssistant externalUserAssistant) {
        return this.externalUserAssistantRepository.save(externalUserAssistant);
    }





}
