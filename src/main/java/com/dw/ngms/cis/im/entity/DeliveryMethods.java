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

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMDELIVERYMETHODS")
public class DeliveryMethods {



    @Id
    @Column(name = "DELIVERYMETHODCODE")
    private String deliveryMethodCode;

    @Column(name = "DELIVERYMETHODNAME", length = 100, unique = true)
    private String deliveryMethodName;


    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();



}
