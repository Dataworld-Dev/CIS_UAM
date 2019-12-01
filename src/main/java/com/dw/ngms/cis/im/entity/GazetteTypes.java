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
@Table(name = "IMGAZZETTETYPES")
public class GazetteTypes {


    @Id
    @Column(name = "GAZZETTETYPECODE")
    private String gazetteTypeCode;

    @Column(name = "GAZZETTETYPENAME", length = 100, unique = true)
    private String gazetteTypeName;


    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();



}
