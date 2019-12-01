package com.dw.ngms.cis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CISBUSINESSRULEHISTORY")
public class BusinessRuleHistory implements Serializable {
	
	private static final long serialVersionUID = -6207972210204835351L;

	@Id
	@SequenceGenerator(name = "generator", sequenceName = "BUSINESSRULEHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	private Long id;
	
	@Column(name="REFERENCENUMBER", length = 200)
	private String referenceNumber;
	
	@Column(name="CATEGORY", length = 500)
	private String category;
	
	@Column(name="SUBCATEGORY", length = 500)
	private String subCategory;
	
	@Column(name="PROVINCE", length = 100)
	private String province;
	
	@Column(name="OFFICER", length = 255)
	private String officer;
	
	@Column(name="BUSINESSRULE", length = 255)
	private String businessRule;
	
	@Column(name="USERNAME", length = 255)
	private String userName;
		
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="DATERECEIVED")
	private Date dateReceived;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="OVERRIDEDATE")
	private Date overrideDate;
	
	@Column(name="OVERRIDEREASON", length = 500)
	private String overrideReason;
}
