package com.dw.ngms.cis.im.entity;

import com.dw.ngms.cis.uam.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "IMCOSTSUBCATEGORIES")
public class CostSubCategories {



    @Column(name = "COSTCATEGORYCODE")
    private String costCategoryCode;

    @Column(name = "COSTCATEGORYNAME", length = 200, unique = true)
    private String categoryName;


    @Id
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


    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIEDDATE")
    private Date modifiedDate;

    @Column(name = "ISDELETED", length = 10, unique = true)
    private String isDeleted;


    @Temporal(TemporalType.DATE)
    @Column(name = "DELETEDDATE")
    private Date deletedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();


/*
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="COSTCATEGORYCODE", nullable=false)
    @JsonBackReference
    private CostCategories costSubCategory;*/


}
