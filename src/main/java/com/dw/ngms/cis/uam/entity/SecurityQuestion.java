package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */

@Entity
@Table(name = "SECURITYQUESTIONTYPES")
@Data
@Getter
@Setter
@ToString
public class SecurityQuestion implements Serializable {

	private static final long serialVersionUID = 1699353954812191931L;


	/*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECURITYQUESTIONTYPEID")
    private Long securityquestiontypeid;*/

    @Id
    @Column(name = "securityquestioncode",nullable = true, length = 50)
    private String securityquestionCode;

    @Column(name = "SECURITYQUESTION",nullable = true, length = 200)
    private String securityquestion;

    @Column(name = "ISACTIVE",nullable = true, length = 10)
    private String isactive;

    @Column(name = "CREATEDDATE",nullable = true)
    private Date createddate;



}
