package com.dw.ngms.cis.im.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by swaroop on 2019/11/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "EMAILTEMPLATE")
public class EmailTemplate {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long emailTemplateId;

    @Column(name = "EMAIL_CATEGORY")
    private String category;

    @Column(name = "EMAIL_DESCRIPTION")
    private String description;

    @Column(name = "EMAIL_SUBJECT")
    private String subject;
    @Column(name = "EMAIL_HEADER")
    private String header;

    @Column(name = "EMAIL_BODY")
    private String body;
    @Column(name = "EMAIL_FOOTER")
    private String footer;

    @Column(name = "EMAIL_FROM")
    private String from;

    @Column(name = "IN_PUT_PARAMS")
    private String inputParams;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", nullable = true)
    private Date modifiedDate = new Date();
}
