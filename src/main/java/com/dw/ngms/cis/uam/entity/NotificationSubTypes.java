package com.dw.ngms.cis.uam.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "NOTIFICATIONSUBTYPES")
public class NotificationSubTypes implements Serializable {

	@Id
	@Column(name = "NOTIFICATIONSUBTYPECODE", length = 50)
	private String notificationSubTypeCode;

	@Column(name = "NOTIFICATIONSUBTYPENAME", length = 100)
	private String notificationSubTypeName;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;


	@Column(name = "ISACTIVE", length = 100)
	private String isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDDATE", nullable = false, length = 10)
	private Date createdDate = new Date();


}
