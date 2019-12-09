package com.dw.ngms.cis.uam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.entity.ProvinceInformation;
import com.dw.ngms.cis.uam.repository.ProvinceInformationRespository;

@Service
public class ProivnceInformationService {

	@Autowired
	ProvinceInformationRespository provinceInfo;

	public ProvinceInformation getProvinceInformationByCode(String provcode) {
		return this.provinceInfo.getProvinceInformationByCode(provcode);
	}
}
