package com.dw.ngms.cis.entity;

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

import com.dw.ngms.cis.uam.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CISNOTIFICATION")
public class CisNotification implements Serializable {
	
	private static final long serialVersionUID = -6207972210204835351L;

	@Id
	@SequenceGenerator(name = "generator", sequenceName = "CISNOTIFICATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	private Long id;
	
	@Column(name="PAYLOAD", length = 2000)
	private String payload;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATIONDATETIME")
	private Date creationDatetime = new Date();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATEDATETIME")
	private Date updateDatetime = new Date();
	
	@Column(name="ISREAD", columnDefinition="boolean")
	private boolean isRead = Boolean.FALSE;
	
	@Column(name="REQUESTCODE", length=50)
	private String requestCode;	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "CISUSERNOTIFICATION",
            joinColumns = { @JoinColumn(name = "NOTIFICATIONID", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "USERID", referencedColumnName = "USERID") })
    private List<User> userList = new ArrayList<>();
}
