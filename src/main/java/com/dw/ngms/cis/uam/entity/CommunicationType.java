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
@Table(name = "COMMUNICATIONMODETYPES")
public class CommunicationType implements Serializable {

	private static final long serialVersionUID = -7263662741431187675L;

	@Id
    @Column(name = "COMMUNICATIONMODETYPECODE", nullable = false, length = 50)
    private String communicationTypeCode;

    @Column(name = "COMMUNICATIONMODETYPENAME", nullable = false, length = 70)
    private String communicationTypeName;    
        
    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = false, length = 10)
    private Status isActive = Status.Y; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = false)
    private Date creationDate = new Date(); 
       
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((communicationTypeCode == null) ? 0 : communicationTypeCode.hashCode());
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
		CommunicationType other = (CommunicationType) obj;
		if (communicationTypeCode == null) {
			if (other.communicationTypeCode != null)
				return false;
		} else if (!communicationTypeCode.equals(other.communicationTypeCode))
			return false;
		return true;
	}
    
}