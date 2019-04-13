package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Srinas Neelapala iv on 2019/03/24.
 */

@Entity
@Table(name = "INTERNALUSERROLES")
@Data
@Getter
@Setter
@ToString
public class InternalUserRoles implements Serializable {

	private static final long serialVersionUID = 1098484893268694655L;



	@Id
    @Column(name = "USERROLEID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userRoleId;
    
    @Column(name = "USERROLECODE", nullable = true, length = 50)
    @NotEmpty(message = "USER ROLE CODE must not be empty")
    private String roleCode;

    @Column(name = "USERROLENAME", nullable = true, length = 50)
    @NotEmpty(message = "USER ROLE NAME must not be empty")
    private String roleName;

    @Column(name = "USERCODE", nullable = true, length = 50)
    @NotEmpty(message = "USER CODE must not be empty")
    private String userCode;

    @Column(name = "USERNAME", nullable = true, length = 255)
    @NotEmpty(message = "USER NAME must not be empty")
    private String userName;
    
    @Column(name = "USERPROVINCECODE", nullable = true, length = 100)
    private String provinceCode;

    @Column(name = "USERPROVINCENAME", nullable = true, length = 50)
    private String provinceName;

    @Column(name = "USERSECTIONCODE", nullable = false, length = 100)
    private String sectionCode;

    @Column(name = "USERSECTIONNAME", nullable = false, length = 50)
    private String sectionName;

    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private String isActive;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    Date createddate;

    @Column(name = "SIGNEDACCESSDOCPATH", nullable = false)
    private String signedAccessDocPath;

    @Column(name = "INTERNALROLECODE",length = 50)
    private String internalRoleCode;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userRoleId == null) ? 0 : userRoleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InternalUserRoles other = (InternalUserRoles) obj;
		if (userRoleId == null) {
			if (other.userRoleId != null)
				return false;
		} else if (!userRoleId.equals(other.userRoleId))
			return false;
		return true;
	}
    
}
