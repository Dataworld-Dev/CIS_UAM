package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dw.ngms.cis.uam.enums.Status;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "PLSUSERS")
public class PlsUser implements Serializable {

	private static final long serialVersionUID = 7700110758724840308L;
	
	@Id
	@Column(name = "PLSCODE")
	private String plscode;
	
	@Column(name = "SURNAME")
	private String surname;
	
	@Column(name = "INITIALS")
	private String initials;
	
	@Column(name = "POSTALADDRESS_1")
	private String postaladdress1;
	
	@Column(name = "POSTALADDRESS_2")
	private String postaladdress2;
	
	@Column(name = "POSTALADDRESS_3")
	private String postaladdress3;
	
	@Column(name = "POSTALADDRESS_4")
	private String postaladdress4;
	
	@Column(name = "POSTALCODE")
	private String postalcode;
	
	@Column(name = "TELEPHONE_NO")
	private String telephoneno;
	
	@Column(name = "CELL_PHONE_NO")
	private String cellphoneno;
	
	@Column(name = "FAX_NO")
	private String faxno;
	
	@Column(name = "COURIER_SERVICE")
	private String courierservice;
	
	@Column(name = "RESTICTED_IND")
	private String restictedind;
	
	@Column(name = "SECTIONAL_PLAN_IND")
	private String sectionalplanind;
	
	@Column(name = "GENERAL_NOTES")
	private String generalnotes;
	
	@Column(name = "PROV_CODE")
	private String provcode;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "CREATED_USER")
	private String createduser;
	
	@Column(name = "CREATED_DATE")
	private String createddate;
	
	@Column(name = "MODIFIED_USER")
	private String modifieduser;
	
	@Column(name = "MODIFIED_DATE")
	private String modifieddate;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "SURVEYOR_ID")
	private String surveyorid;
	
	@Column(name = "SG_OFFICE_ID")
	private String sgofficeid;

	@Enumerated(EnumType.STRING)
	@Column(name = "ISACTIVE")
	private Status isActive = Status.Y;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ISVALID")
	private Status isValid = Status.Y;
	
}
