package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NaturalId;

import com.dw.ngms.cis.uam.enums.Status;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "PROVINCES")
public class Province implements Serializable {

	private static final long serialVersionUID = -9126471965162438729L;

/*	@Column(name = "PROVINCEID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;*/

    @Column(name = "ORGCODE", nullable = true, length = 50)
    private String organisationCode;

    @Column(name = "ORGNAME", nullable = true, length = 100)
    private String organisationName;    
    
    @Id
    @Column(name = "PROVINCECODE", nullable = true, length = 50)
    private String code; 
    
    @Column(name = "PROVINCENAME", nullable = true, length = 100)
    private String name; 
    
    @Column(name = "PROVINCESHORTNAME", nullable = true, length = 50)
    private String shortName; 
    
    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date creationDate; 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Province other = (Province) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
    
}