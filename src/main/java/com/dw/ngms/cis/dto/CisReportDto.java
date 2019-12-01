package com.dw.ngms.cis.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dw.ngms.cis.uam.dto.UserSummaryReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CisReportDto implements Serializable {

	private static final long serialVersionUID = 4248269480395418665L;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date toDate;
	private String province;
	private String provinceCode;
	private String sector;
	private String organisation;
	private String section;
	private String sectionCode;
	private String userType;
	private String category;
	private String taskStatus;
	private String admin;
	private String officer;
	private String resourcePath;
	
}
