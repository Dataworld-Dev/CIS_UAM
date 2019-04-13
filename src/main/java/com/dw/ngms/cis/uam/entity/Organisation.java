package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "ORGANIZATION")
public class Organisation implements Serializable {

	private static final long serialVersionUID = -3791837488494889093L;

	@Column(name = "ORGANIZATIONID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@Column(name = "ORGCODE", nullable = false, length = 30)
	private String organisationCode;
	
	@Column(name = "ORGNAME", nullable = false, length = 10)
	private String organisationName;
	
	@Column(name = "ADDRESS", nullable = true, length = 500)
	private String address;
	
	@Column(name = "DESCRIPTION", nullable = false, length = 500)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ISACTIVE", nullable = false, length = 10)
	private Status isActive = Status.Y;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATEDDATE", nullable = false, length = 10)
	private Date createdDate = new Date();

}

	
	
