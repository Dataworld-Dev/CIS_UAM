package com.dw.ngms.cis.im.entity;

import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
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
@Table(name = "IMCOSTCATEGORIES")
public class CostCategories {

    @Id
    @Column(name = "COSTCATEGORYID")
//    @SequenceGenerator(name = "generator", sequenceName = "COSTCATEGORY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long costCategoryId;

    @OneToMany(mappedBy="subCategoriesItems",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CostSubCategories> subCategoriesItems;

    @Column(name = "COSTCATEGORYCODE")
    private String categoryCode;

    @Column(name = "COSTCATEGORYNAME", length = 200, unique = true)
    private String categoryName;

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
