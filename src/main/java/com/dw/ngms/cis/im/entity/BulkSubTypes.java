package com.dw.ngms.cis.im.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMBULKSUBTYPES")
public class BulkSubTypes {


    @Column(name = "BULKTYPEID", nullable = false, length = 50, insertable = false, updatable = false)
    private Long bulkTypeId;

    @Id
    @Column(name = "SUBBULKID")
    @SequenceGenerator(name = "generator", sequenceName = "SUBBULKTYPES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long subBulkId;

    @Column(name = "BULKTYPECODE")
    private String bulkTypeCode;

    @Column(name = "BULKTYPENAME", length = 200, unique = true)
    private String bulkTypeName;

    @Column(name = "SUBBULKCODE", length = 50, unique = true)
    private String subBulkCode;

    @Column(name = "SUBBULKNAME", length = 200, unique = true)
    private String subBulkName;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "FIXEDRATE", length = 10)
    private String fixedRate;

    @Column(name = "HOURRATE", length = 10)
    private String hourRate;


    @Column(name = "HALFHOURRATE", length = 10)
    private String halfHourRate;

    @Column(name = "ISACTIVE", length = 10, unique = true)
    private String isActive;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIEDDATE")
    private Date modifiedDate;

    @Column(name = "ISDELETED", length = 10)
    private String isDeleted;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETEDDATE")
    private Date deletedDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BULKTYPEID", nullable = false)
    @JsonBackReference
    private BulkTypes subBulkItems;


}
