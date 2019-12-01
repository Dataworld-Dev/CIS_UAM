package com.dw.ngms.cis.im.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMMEDIATYPES")
public class MediaTypes {


    @Id
    @Column(name = "MEDIATYPECODE")
    private String mediaTypeCode;

    @Column(name = "MEDIATYPENAME", length = 100, unique = true)
    private String mediaTypeName;


    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();



}
