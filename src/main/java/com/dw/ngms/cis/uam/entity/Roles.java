package com.dw.ngms.cis.uam.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by swaroop on 2019/03/24.
 */

@Entity
@Table(name = "ROLES")
@Data
@Getter
@Setter
@ToString
public class Roles implements Serializable {


    private static final long serialVersionUID = 8072176445175268887L;

  /*  @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLEID")
    private Long roleid;*/

    @Column(name = "ROLENAME")
    private String rolename;

    @Column(name = "ROLETYPE")
    private String roletype;

    @Column(name = "NEEDAPPROVAL")
    private String needapproval;

    @Id
    @Column(name = "ROLECODE")
    private String rolecode;

    @Column(name = "MENUJSON", length = 2000)
    private String menuJson;
}
