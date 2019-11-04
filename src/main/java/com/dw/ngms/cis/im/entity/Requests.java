package com.dw.ngms.cis.im.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.LapseStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by swaroop on 2019/04/19.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMREQUESTS")
public class Requests implements Serializable {

	private static final long serialVersionUID = 8311145174688482135L;

	@Id
    @Column(name = "REQUESTID")
    private Long requestId;

    @OneToMany(mappedBy="requestItems",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RequestItems> requestItems;

    @Column(name = "REQUESTCODE")
    private String requestCode;

    @Column(name = "USERCODE", length = 100, unique = true)
    private String userCode;

    @Column(name = "PROVINCECODE", length = 100, unique = true)
    private String provinceCode;

    @Column(name = "SECTIONCODE", length = 100, unique = true)
    private String sectionCode;

    @Column(name = "USERNAME", length = 200)
    private String userName;

    @Column(name = "EMAIL", length = 200)
    private String email;


    @Column(name = "REQUESTTYPENAME", length = 100, unique = true)
    private String requestTypeName;


    @Column(name = "REQUESTKINDNAME")
    private String requestKindName;

    @Column(name = "REQUESTTITLE", length = 100, unique = true)
    private String requestTitle;

    @Column(name = "ORGANISATION", length = 200)
    private String organisation;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POSTALADDRESS1", length = 100)
    private String postalAddress1;

    @Column(name = "POSTALADDRESS2", length = 100)
    private String postalAddress2;

    @Column(name = "POSTALADDRESS3", length = 100)
    private String postalAddress3;

    @Column(name = "POSTALADDRESS4", length = 100)
    private String postalAddress4;



    @Column(name = "DELIVERYMETHOD", length = 200)
    private String deliveryMethod;

    @Column(name = "MEDIATYPE", length = 200)
    private String mediaType;

    @Column(name = "FORMATTYPE", length = 200)
    private String formatType;

    @Column(name = "PAYMENTCURRENCY", length = 200)
    private String paymentCurrency;

    @Column(name = "TOTALPAYMENTAMOUNT", length = 200)
    private String totalPaymentAmount;

    @Column(name = "TOTALPAYMENTVAT", length = 200)
    private String totalPayment;

    @Column(name = "TOTALAMOUNT", length = 200)
    private String totalAmount;

    @Column(name = "INVOICENUMBER", length = 200)
    private String invoiceNumber;

    @Column(name = "INVOICEFILEPATH", length = 200)
    private String invoiceFilePath;

    @Column(name = "POPFILEPATH", length = 200)
    private String popFilePath;

    @Column(name = "PAYMENTSTATUS", length = 10)
    private String paymentStatus;


    @Column(name = "REFERENCENUMBER", length = 200)
    private String referenceNumber;


    @Column(name = "MODIFIEDUSERCODE", length = 200)
    private String modifiedUserCode;

    @Column(name = "DISPATCHDOCS", length = 200)
    private String dispatchDocs;

    @Column(name = "DOCUMENTSTORECODE", length = 20)
    private String documentStoreCode;

    @Column(name = "EXTERNALUSERDISPATCHDOCS", length = 200)
    private String externalUserDispatchDocs;

    @Transient
    private String currentStatus;


    @Column(name = "ISACTIVE", length = 200)
    private String isActive;

    @Column(name = "ISDELETED", length = 200)
    private String isDeleted;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "REQUESTDATE", nullable = true)
    private Date requestDate = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "DELETEDDATE")
    private Date deletedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIEDDATE")
    private Date modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "LAPSESTATUS", length = 20)
    private LapseStatus lapseStatus = LapseStatus.NONE;

    @Transient
    private String processId;    
    @Transient
    private String userFullName;    
    @Transient
    private String sequenceRequest;    
    @Transient
    private boolean internalCapturer;    
    @Transient
    private String capturerCode;
    @Transient
    private String capturerName; 
    @Transient
    private String capturerFullName; 
    @Transient
    private String assigneeInfoManager;    
    @Transient
    private String assigneeInfoOfficer;
    @Transient
    private String requestFtpSiteUrl;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
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
        Requests other = (Requests) obj;
        if (requestId == null) {
            if (other.requestId != null)
                return false;
        } else if (!requestId.equals(other.requestId))
            return false;
        return true;
    }

}
