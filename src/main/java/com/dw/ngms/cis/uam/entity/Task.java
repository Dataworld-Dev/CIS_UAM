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
@Table(name = "TASKS")
public class Task implements Serializable {



    @Id
    @Column(name = "TASKID")
    @SequenceGenerator(name = "generator", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long taskId;


    @Column(name = "TASKCODE")
    @SequenceGenerator(name = "generator", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private String taskCode;

    @Column(name = "TASKTYPE", length = 50, unique=true)
    private String taskType;

    @Column(name = "TASKREFERENCECODE", length = 50, unique=true)
    private String taskReferenceCode;

    @Column(name = "TASKREFERENCETYPE", length = 100, unique=true)
    private String taskReferenceType;

    @Column(name = "TASKOPENDESC", length = 1000, unique=true)
    private String taskOpenDesc;

    @Column(name = "TASKALLOCPROVINCECODE", length = 50, unique=true)
    private String taskAllProvinceCode;

    @Column(name = "TASKALLOCSECTIONCODE", length = 50, unique=true)
    private String taskAllOCSectionCode;

    @Column(name = "TASKALLOCROLECODE", length = 50, unique=true)
    private String taskAllOCRoleCode;

    @Column(name = "TASKSTATUS", length = 10, unique=true)
    private String taskStatus;


    @Temporal(TemporalType.DATE)
    @Column(name = "TASKOPENDATE")
    private Date taskOpenDate = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "TASKCLOSEDATE")
    private Date taskCloseDate;

    @Column(name = "TASKCLOSEDESC", length = 100, unique=true)
    private String taskCLoseDESC;


    @Column(name = "TASKDONEUSERCODE", length = 100, unique=true)
    private String taskDoneUserCode;

    @Column(name = "TASKDONEUSERNAME", length = 100, unique=true)
    private String taskDoneUserName;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();



}
