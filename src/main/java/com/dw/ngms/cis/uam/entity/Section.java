package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dw.ngms.cis.uam.enums.Status;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SECTIONS")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Section implements Serializable {

	private static final long serialVersionUID = -1043469818773504678L;

    @Id
	@Column(name = "SECTIONCODE", nullable = true, length = 50)
    private String code; 
    
    @Column(name = "SECTIONNAME", nullable = true, length = 100)
    private String name; 
    
	@Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date creationDate; 
    
}
