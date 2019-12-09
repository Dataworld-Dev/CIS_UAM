package com.dw.ngms.cis.im.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "PROVINCES_INFO")
public class ProvinceInformation {

	@Id
	@Column(name = "SG_PROV_CODE", unique = true)
	private String provCode;

	@Column(name = "SG_OFFICE")
	private String provSgOffice;

	@Column(name = "SG_ADDRESS_L1")
	private String provAdds1;

	@Column(name = "SG_ADDRESS_L2")
	private String provAdds2;

	@Column(name = "SG_ADDRESS_L3")
	private String provAdds3;

	@Column(name = "SG_PHONE")
	private String provPhone;

	@Column(name = "SG_FAX")
	private String provFax;

	@Column(name = "SG_EMAIL")
	private String provEmail;

	@Column(name = "SG_BANK_ACCHLDR")
	private String provBankAccHldr;

	@Column(name = "SG_BANK_NAME")
	private String provBankName;

	@Column(name = "SG_BANK_BRANCHC")
	private String provBankBranchCode;

	@Column(name = "SG_BANK_ACCNUM")
	private String provBankAccNo;

	@Column(name = "SG_BANK_ACCTYPE")
	private String provBankAccType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = true)
	private Date createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

}
