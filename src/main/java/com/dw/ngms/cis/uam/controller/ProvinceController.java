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

import com.dw.ngms.cis.uam.entity.Province;
import com.dw.ngms.cis.uam.service.ProvinceService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ProvinceController extends MessageController {
	
	@Autowired
    private ProvinceService provinceService;

    @GetMapping("/getProvinces")
    public ResponseEntity<?> getAllProvinces(HttpServletRequest request) {
        try {
        	List<Province> provinceList = this.provinceService.getAllProvinces();
        	return (CollectionUtils.isEmpty(provinceList)) ? generateEmptyResponse(request, "Provinces not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(provinceList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllProvinces
    
}
