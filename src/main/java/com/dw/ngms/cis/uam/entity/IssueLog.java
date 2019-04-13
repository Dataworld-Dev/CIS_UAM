package com.dw.ngms.cis.uam.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ISSUELOG")
public class IssueLog implements Serializable {



    @Id
    @Column(name = "ISSUELOGID")
    @SequenceGenerator(name = "generator", sequenceName = "isuuelog_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long issueId;


    @Column(name = "FULLNAME",length = 500)
    private String fullName;

    @Column(name = "EMAIL", length = 500)
    private String email;

    @Column(name = "ISSUETYPE", length = 50)
    private String issueType;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Column(name = "ISSUESTATUS", length = 50)
    private String issueStatus;

    @Column(name = "USERTYPE", length = 50)
    private String userType;

    @Column(name = "ROLE", length = 50)
    private String role;

    @Column(name = "PROVINCE", length = 50)
    private String province;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE")
    private Date createdDate = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "DATERESOLVED")
    private Date dateResolved;

    @Column(name = "RESOLVEDBY", length = 50)
    private String resolvedBy;




}
