package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.Section;
import com.dw.ngms.cis.uam.service.SectionService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class SectionController extends MessageController {
	
	@Autowired
    private SectionService sectionService;

    @GetMapping("/getSections")
    public ResponseEntity<?> getAllSections(HttpServletRequest request) {
        try {
        	List<Section> sectionList = this.sectionService.getAllSections();
        	return (CollectionUtils.isEmpty(sectionList)) ? generateEmptyResponse(request, "Sections not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(sectionList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllSections
    
}
