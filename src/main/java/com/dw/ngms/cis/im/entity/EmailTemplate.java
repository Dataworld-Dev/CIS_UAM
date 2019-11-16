package com.dw.ngms.cis.im.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by swaroop on 2019/11/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "EMAILTEMPLATE")
public class EmailTemplate {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long emailTemplateId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "HEADER")
    private String header;

    @Column(name = "BODY")
    private String body;

    @Column(name = "FOOTER")
    private String footer;

    @Column(name = "FROM")
    private String from;

    @Column(name = "IN_PUT_PARAMS")
    private String inputParams;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIED_DATE", nullable = true)
    private Date modifiedDate = new Date();
}
