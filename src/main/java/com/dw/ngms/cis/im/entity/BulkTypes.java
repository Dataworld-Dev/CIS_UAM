package com.dw.ngms.cis.im.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMBULKTYPES")
public class BulkTypes {

    @Id
    @Column(name = "BULKTYPEID")
    @SequenceGenerator(name = "generator", sequenceName = "BULKTYPES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long bulkTypeId;

    @OneToMany(mappedBy="subBulkItems",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BulkSubTypes> subBulkItems;

    @Column(name = "BULKTYPECODE")
    private String bulkTypeCode;

    @Column(name = "BULKTYPENAME", length = 200, unique = true)
    private String bulkTypeName;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "ISACTIVE", length = 10, unique = true)
    private String isActive;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIEDDATE")
    private Date modifiedDate;


    @Column(name = "ISDELETED", length = 10, unique = true)
    private String isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETEDDATE")
    private Date deletedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();

}
