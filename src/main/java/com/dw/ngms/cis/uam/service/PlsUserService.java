package com.dw.ngms.cis.uam.service;

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
	
        return this.plsUserRepository.save(plsuser);
	}//updatePlsUser
    	
}
