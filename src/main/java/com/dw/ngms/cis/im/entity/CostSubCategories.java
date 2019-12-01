package com.dw.ngms.cis.im.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMCOSTSUBCATEGORIES")
public class CostSubCategories {


    @Column(name = "COSTCATEGORYID", nullable = false, length = 50, insertable = false, updatable = false)
    private Long costCategoryId;

    @Id
    @Column(name = "SUBCATEGORYID")
    @SequenceGenerator(name = "generator", sequenceName = "SUBCOSTCATEGORY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long subCategoryId;

    @Column(name = "COSTCATEGORYCODE")
    private String costCategoryCode;

    @Column(name = "COSTCATEGORYNAME", length = 200, unique = true)
    private String costCategoryName;

    @Column(name = "COSTSUBCATEGORYCODE", length = 50, unique = true)
    private String costSubCategoryCode;

    @Column(name = "COSTSUBCATEGORYNAME", length = 200, unique = true)
    private String costSubCategoryName;

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


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="COSTCATEGORYID", nullable=false)
    @JsonBackReference
    private CostCategories subCategoriesItems;


}
