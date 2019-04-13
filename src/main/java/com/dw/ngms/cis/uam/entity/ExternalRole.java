package com.dw.ngms.cis.uam.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/03/30.
 */

@Entity
@Table(name = "EXTERNALROLES")
@Data
@Getter
@Setter
@ToString
public class ExternalRole implements Serializable {



   /* @Id
    @Column(name = "EXTERNALROLEID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long externalRoleCode;*/

    @Id
    @Column(name = "EXTERNALROLECODE", nullable = true, length = 50)
    @NotEmpty(message = "External CODE must not be empty")
    private String externalRoleCode;


    @Column(name = "ORGCODE", nullable = true, length = 50)
    @NotEmpty(message = "ORG CODE NAME must not be empty")
    private String orgCode;


    @Column(name = "ORGNAME", nullable = true, length = 50)
    @NotEmpty(message = "Org name must not be empty")
    private String ORGNAME;

    @Column(name = "PROVINCECODE", nullable = true, length = 255)
    @NotEmpty(message = "Province Code not be empty")
    private String provinceCode;

    @Column(name = "PROVINCENAME", nullable = true, length = 100)
    @NotEmpty(message = "PROVINCE NAME must not be empty")
    private String provinceName;

    @Column(name = "ROLECODE", nullable = true, length = 50)
    @NotEmpty(message = "ROLE CODE must not be empty")
    private String roleCode;

    @Column(name = "ROLENAME", nullable = true, length = 100)
    @NotEmpty(message = "ROLE NAME must not be empty")
    private String roleName;

    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description;

    @Column(name = "ISACTIVE", nullable = true, length = 10)
    @NotEmpty(message = "isActive must not be empty")
    private String isActive;

    @Column(name = "ACCESSRIGHTSJSON", nullable = true, length = 5000)
    @NotEmpty(message = "ACCESScRIGHTScJSON must not be empty")
    private String accessRightJson;

    @Column(name = "CREATEDDATE", nullable = true)
    Date createdDate;
}
