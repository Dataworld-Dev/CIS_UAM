package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dw.ngms.cis.uam.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "ORGANIZATIONTYPES")
public class OrganisationType implements Serializable {

	private static final long serialVersionUID = -3791837488494889093L;

	@Id
	@Column(name = "ORGANIZATIONTYPECODE", nullable = false, length = 30)
	private String organizationTypeCode;
	
	@Column(name = "ORGANIZATIONTYPENAME", nullable = false, length = 10)
	private String organizationTypeName;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 500)
	private String description;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "ISACTIVE", nullable = false, length = 10)
	private Status isActive = Status.Y;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDDATE", nullable = false, length = 10)
	private Date createdDate = new Date();

}

	
	
