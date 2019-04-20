package com.dw.ngms.cis.uam.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.repository.PlsUserRepository;

@Service
public class PlsUserService {

	@Autowired
	private PlsUserRepository plsUserRepository;
	
	public List<PlsUser> getAllPlsUsers() {
		return this.plsUserRepository.findAll();
	}//getAllPlsUsers

	public PlsUser findByEmail(String email) {
		return this.plsUserRepository.findByEmail(email);
	} //findByEmail
	
	public PlsUser findByCode(String plsCode) {
		return this.plsUserRepository.findByPlsCode(plsCode);
	}//findByCode

	public PlsUser findByCodeAndEmail(String plsCode, String email) {
		return this.plsUserRepository.findByCodeAndEmail(plsCode, email);
	} //findByCodeAndEmail
	
	public PlsUser addPlsUser(@Valid PlsUser plsuser) {
		if(plsuser == null) 
			throw new RuntimeException("PlsUser required to register");
		if(StringUtils.isEmpty(plsuser.getPlscode())) 
			throw new RuntimeException("PlsUser code required to register");
		if(StringUtils.isEmpty(plsuser.getEmail())) 
			throw new RuntimeException("PlsUser email required to register");
		
		PlsUser user = findByEmail(plsuser.getEmail());
		if(user != null)
			throw new RuntimeException("PlsUser is already registered with code "+plsuser.getPlscode()+" and email: "+plsuser.getEmail());
	
        return this.plsUserRepository.save(plsuser);
    }//addPlsUser
	
	public PlsUser updatePlsUser(PlsUser plsuser) {
		if(plsuser == null) 
			throw new RuntimeException("PlsUser required to update");
		if(StringUtils.isEmpty(plsuser.getPlscode())) 
			throw new RuntimeException("PlsUser code required to update");
		if(StringUtils.isEmpty(plsuser.getEmail())) 
			throw new RuntimeException("PlsUser email required to update");
		
		PlsUser user = findByEmail(plsuser.getEmail());
		if(user == null)
			throw new RuntimeException("PlsUser is not registered with code "+plsuser.getPlscode()+" and email: "+plsuser.getEmail());
	
        return this.plsUserRepository.save(populatePlsUser(plsuser, user));
	}//updatePlsUser
    	
	private PlsUser populatePlsUser(PlsUser uiplsuser, PlsUser dbplsuser) {
		dbplsuser.setCellphoneno(uiplsuser.getCellphoneno());
		dbplsuser.setCourierservice(uiplsuser.getCourierservice());
		dbplsuser.setDescription(uiplsuser.getDescription());
		dbplsuser.setFaxno(uiplsuser.getFaxno());
		dbplsuser.setGeneralnotes(uiplsuser.getGeneralnotes());
		dbplsuser.setInitials(uiplsuser.getInitials());
		dbplsuser.setIsActive(uiplsuser.getIsActive());
		dbplsuser.setIsValid(uiplsuser.getIsValid());
		dbplsuser.setModifieddate(getFormattedDate());
		dbplsuser.setModifieduser(uiplsuser.getModifieduser());
		dbplsuser.setPostaladdress1(uiplsuser.getPostaladdress1());
		dbplsuser.setPostaladdress2(uiplsuser.getPostaladdress2());
		dbplsuser.setPostaladdress3(uiplsuser.getPostaladdress3());
		dbplsuser.setPostaladdress4(uiplsuser.getPostaladdress4());
		dbplsuser.setPostalcode(uiplsuser.getPostalcode());
		dbplsuser.setProvcode(uiplsuser.getProvcode());
		dbplsuser.setRestictedind(uiplsuser.getRestictedind());
		dbplsuser.setSectionalplanind(uiplsuser.getSectionalplanind());
		dbplsuser.setSgofficeid(uiplsuser.getSgofficeid());
		dbplsuser.setSurname(uiplsuser.getSurname());
		dbplsuser.setSurveyorid(uiplsuser.getSurveyorid());
		dbplsuser.setTelephoneno(uiplsuser.getTelephoneno());
		
		return dbplsuser;
	}//populatePlsUser
	
	private String getFormattedDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		return dateFormat.format(new Date());  
	}//getFormattedDate
	
}
