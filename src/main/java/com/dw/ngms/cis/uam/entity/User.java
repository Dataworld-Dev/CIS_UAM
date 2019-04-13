package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.enums.Status;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 6118117170894690660L;

	@Id
    @Column(name = "USERID")
    @SequenceGenerator(name = "generator", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long userId;


    @Column(name = "USERCODE", length = 50, unique=true)
    private String userCode;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="user")
    private ExternalUser externaluser;


    @OneToMany(mappedBy="externalUserRole",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ExternalUserRoles> externalUserRoles;

    @Column(name = "USERNAME", nullable = true, length = 255)
    private String userName;

    @Column(name = "USERTYPECODE", nullable = true, length = 50)
    private String userTypeCode;

    @Column(name = "USERTYPENAME", nullable = true, length = 50)
    private String userTypeName;

    @Column(name = "PASSWORD", nullable = true, length = 60)
    private String password;

    @Column(name = "TITLE",length = 15)
    private String title;

    @Column(name = "FIRSTNAME", nullable = true, length = 60)
    private String firstName;

    @Column(name = "SURNAME", nullable = true, length = 60)
    private String surname;

    @Column(name = "MOBILENO",nullable = true, length = 20)
    private String mobileNo;

    @Column(name = "TELEPHONENO", length = 20)
    private String telephoneNo;

    @Column(name = "EMAIL", nullable = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISAPPROVED",length = 3)
    private ApprovalStatus isApproved = ApprovalStatus.YES;

    @Column(name = "REJECTIONREASON",length = 500)
    private String rejectionReason;

    @Column(name = "ISAPPREJUSERCODE",length = 50)
    private String isApprejuserCode;

    @Column(name = "ISAPPREJUSERNAME",length = 255)
    private String isApprejuserName;

    @Column(name = "ISAPPREJDATE")
    private Date isApprejDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive = Status.Y;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();


    @Column(name = "FIRSTLOGIN",nullable = true, length = 1)
    private String firstLogin;

    @Transient
    private List<InternalUserRoles> internalUserRoles;

    @Transient
    private String mainRoleCode;

    @Transient
    private String mainRoleName;


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
