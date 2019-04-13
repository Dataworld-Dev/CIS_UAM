package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "AUDITENTRY")
public class AuditEntry implements Serializable {

	private static final long serialVersionUID = -5302767605769257492L;

	@Id
	@Column(name = "ID", nullable = false, length = 16)
	private UUID id;

	@Column(name = "OPERATION", length = 100)
	private String operation;

	@Column(name = "REQUESTURL", length = 500)
	private String requestUrl;

	@Column(name = "USERCODE", length = 20)
	private String userCode;

	@Column(name = "USERNAME", length = 50)
	private String userName;
	
	@Column(name = "USERTYPENAME", length = 20)
	private String userType;
	
	@Column(name = "REQUESTJSON")
	private String requestJson;

	@Column(name = "RESPONSEJSON")
	private String responseJson;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUESTDATETIME")
	private Date requestDatetime = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESPONSEDATETIME")
	private Date responseDatetime;

	public AuditEntry(String operation, String requestUrl, String userCode, String userName, String userType, String requestJson) {
		this.id = UUID.randomUUID();
		this.operation = operation;
		this.requestUrl = requestUrl;
		this.userCode = userCode;
		this.userName = userName;
		this.userType = userType;
		this.requestJson = requestJson;
	}

	
}
