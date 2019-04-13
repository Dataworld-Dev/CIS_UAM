package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by swaroop on 2019/03/24.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "EXTERNALUSERDATA")
public class ExternalUser implements Serializable {

    private static final long serialVersionUID = -955002950121265546L;

  /*  @Id
    @Column(name = "EXTERNALUSERDATAID")
    private Long externaluserdataid;
*/

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USERID",updatable =false, insertable = false, referencedColumnName="USERID", nullable = false)
    private User user;



    @Id
    @Column(name = "USERCODE", nullable = true, length = 50)
    @NotEmpty(message = "USERCODE must not be empty")
    private String usercode;

    @Column(name = "USERID", nullable = true, length = 50)
    private Long userId;

    @Column(name = "ORGANIZATIONTYPECODE", nullable = true, length = 50)
    private String organizationtypecode;

    @Column(name = "ORGANIZATIONTYPENAME", nullable = true, length = 70)
    private String organizationtypename;

    @Column(name = "PPNNO", length = 70)
    private String ppno;

    @Column(name = "PRACTISENAME", length = 100)
    private String practicename;

    @Column(name = "POSTALADDRESSLINE1", nullable = true, length = 70)
    private String postaladdressline1;

    @Column(name = "POSTALADDRESSLINE2", nullable = true, length = 70)
    private String postaladdressline2;

    @Column(name = "POSTALADDRESSLINE3", length = 70)
    private String postaladdressline3;

    @Column(name = "POSTALCODE", nullable = true, length = 15)
    private String postalcode;

    @Column(name = "COMMUNICATIONMODETYPECODE", nullable = true, length = 50)
    private String communicationmodetypecode;

    @Column(name = "COMMUNICATIONMODETYPENAME", nullable = true, length = 15)
    private String communicationmodetypename;

    @Column(name = "SECURITYQUESTIONTYPECODE1", nullable = true, length = 50)
    private String securityquestiontypecode1;

    @Column(name = "SECURITYQUESTION1", nullable = true, length = 150)
    private String securityquestion1;

    @Column(name = "SECURITYANSWER1", nullable = true, length = 50)
    private String securityanswer1;

    @Column(name = "SECURITYQUESTIONTYPECODE2", nullable = true, length = 50)
    private String securityquestiontypecode2;

    @Column(name = "SECURITYQUESTION2", nullable = true, length = 150)
    private String securityquestion2;

    @Column(name = "SECURITYANSWER2", nullable = true, length = 50)
    private String securityanswer2;

    @Column(name = "SECURITYQUESTIONTYPECODE3", nullable = true, length = 50)
    private String securityquestiontypecode3;

    @Column(name = "SECURITYQUESTION3", nullable = true, length = 150)
    private String securityquestion3;

    @Column(name = "SECURITYANSWER3", nullable = true, length = 50)
    private String securityanswer3;;

    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate;

    @Column(name = "SUBSCRIBENEWS",length = 1)
    private String subscribenews;

    @Column(name = "SUBSCRIBENOTIFICATIONS",length = 1)
    private String subscribenotifications;

    @Column(name = "SUBSCRIBEEVENTS",length = 1)
    private String subscribeevents;
    
    @Column(name = "SECTORCODE",length = 50)
    private String sectorcode;

    @Column(name = "SECTORNAME",length = 100)
    private String sectorname;

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((externaluserdataid == null) ? 0 : externaluserdataid.hashCode());
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
		ExternalUser other = (ExternalUser) obj;
		/*if (externaluserdataid == null) {
			if (other.externaluserdataid != null)
				return false;
		} else if (!externaluserdataid.equals(other.externaluserdataid))
			return false;*/
		return true;
	}

}
