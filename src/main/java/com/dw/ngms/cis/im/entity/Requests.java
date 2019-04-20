package com.dw.ngms.cis.im.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by swaroop on 2019/04/19.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMREQUESTS")
public class Requests implements Serializable {

    @Id
    @Column(name = "REQUESTCODE")
    private String requestCode;

    @Column(name = "USERCODE", length = 100, unique = true)
    private String userCode;


    @Column(name = "USERNAME", length = 200)
    private String userName;

    @Column(name = "REQUESTTYPECODE")
    private String requestTypeCode;

    @Column(name = "REQUESTTYPENAME", length = 100, unique = true)
    private String requestTypeName;


    @Column(name = "REQUESTKINDCODE", length = 200)
    private String requestKindCode;


    @Column(name = "REQUESTKINDNAME")
    private String requestKindName;

    @Column(name = "REQUESTTITLE", length = 100, unique = true)
    private String requestTitle;


    @Column(name = "ORGANISATION", length = 200)
    private String organisation;


    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POSTALADDRESS1", length = 100, unique = true)
    private String postalAddress1;

    @Column(name = "POSTALADDRESS2", length = 100, unique = true)
    private String postalAddress2;

    @Column(name = "POSTALADDRESS3", length = 100, unique = true)
    private String postalAddress3;

    @Column(name = "POSTALADDRESS4", length = 100, unique = true)
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


    @Column(name = "REFERENCENUMBER", length = 200)
    private String referenceNumber;


    @Column(name = "MODIFIEDUSERCODE", length = 200)
    private String modifiedUserCode;


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

    @OneToMany(mappedBy="requestItems",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<RequestItems> requestItems;


}
