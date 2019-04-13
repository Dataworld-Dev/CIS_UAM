package com.dw.ngms.cis.uam.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/12.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DASHBOARD")
public class DashBoard implements Serializable {

    @Id
    @Column(name = "DASHBOARID")
    @SequenceGenerator(name = "generator", sequenceName = "isuuelog_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long dashBoardID;

    @Column(name = "MODULE",length = 100)
    private String module;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "JSON", length = 2000)
    private String dashBoardJson;

}
