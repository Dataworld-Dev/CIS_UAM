package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by swaroop on 2019/04/06.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASKS")
public class Task implements Serializable {

	private static final long serialVersionUID = -176045520796625793L;

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

    @Column(name = "TASKSTATUS", length = 30, unique=true)
    private String taskStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TASKOPENDATE")
    private Date taskOpenDate = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TASKCLOSEDATE")
    private Date taskCloseDate;

    @Column(name = "TASKCLOSEDESC", length = 100, unique=true)
    private String taskCLoseDESC;

    @Column(name = "TASKDONEUSERCODE", length = 100, unique=true)
    private String taskDoneUserCode;

    @Column(name = "TASKDONEUSERNAME", length = 100, unique=true)
    private String taskDoneUserName;

    @Column(name = "TASKDONEUSERFULLNAME", length = 125)
    private String taskDoneUserFullName;
    
    @Column(name = "INITIATEDUSER", length = 255)
    private String initiatedUser;
    
    @Column(name = "ASSIGNEDUSER", length = 255)
    private String assignedUser;
    
//    @Column(name = "DESCRIPTION", length = 255)
//    private String description;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATEDDATE", nullable = true)
    private Date updatedDate = new Date();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "ROLE_TASK",
    	joinColumns = @JoinColumn(name = "TASKID"),
        inverseJoinColumns = @JoinColumn(name = "INTERNALROLEID"))
    private List<InternalRole> internalRoleList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "USER_TASK",
    	joinColumns = @JoinColumn(name = "TASKID"),
        inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<User> userList = new ArrayList<>();

}
