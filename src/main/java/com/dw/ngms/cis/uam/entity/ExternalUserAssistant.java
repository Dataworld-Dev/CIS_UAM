package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */
@Entity
@Table(name = "EXTERNALUSERASSISTANTS")
@Data
@Getter
@Setter
@ToString
public class ExternalUserAssistant implements Serializable {

	private static final long serialVersionUID = 432458826856147391L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EXTERNALASSISTANTID")
    private Long externalassistantid;

	@Column(name = "USERID", nullable = true, length = 50)
	private Long userId;

    @Column(name = "SURVEYORUSERCODE",nullable = true, length = 50)
    private String surveyorusercode;

    @Column(name = "SURVEYORUSERNAME",nullable = true, length = 255)
    private String surveyorusername;

    @Column(name = "ASSISTANTUSERCODE",nullable = true, length = 50)
    private String assistantusercode;

    @Column(name = "ASSISTANTUSERNAME",nullable = true, length = 255)
    private String assistantusername;

    @Column(name = "REJECTIONREASON",nullable = true, length = 500)
    private String rejectionreason;

	@Column(name = "ISACTIVE",nullable = true, length = 500)
	private String isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ISAPPREJDATE")
    private Date isapprejdate;

    @Column(name = "ISAPPROVED",nullable = true, length = 500)
    private String isApproved;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE",nullable = true)
    private Date createddate;


    
}
