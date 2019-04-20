package com.dw.ngms.cis.im.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "APPLICATIONPROPERTIES")
public class ApplicationProperties implements Serializable {


    @Id
    @Column(name = "KEYNAME", length = 200, unique = true)
    private String keyName;


    @Column(name = "KEYVALUE", length = 200, unique = true)
    private String keyValue;


    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "ISEDITABLE", length = 10,unique = true)
    private String isEditable;



    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();



}
