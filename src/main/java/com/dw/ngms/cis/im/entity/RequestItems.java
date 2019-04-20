package com.dw.ngms.cis.im.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/19.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMREQUESTITEMS")
public class RequestItems implements Serializable {

    @Id
    @Column(name = "REQUESTITEMCODE")
    private String requestItemCode;


    @Column(name = "REQUESTGAZZETTETYPE", length = 200)
    private String requestGazetteType;

    @Column(name = "REQUESTGAZZETTE1")
    private String requestGazette1;

    @Column(name = "REQUESTGAZZETTE2", length = 100, unique = true)
    private String requestGazette2;


    @Column(name = "REQUESTCOST", length = 200)
    private String requestCost;

    @Column(name = "REQUESTHOURS", length = 200)
    private String requestHours;


    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="requestCode", nullable=false)
    @JsonBackReference
    private Requests requestItems;
}
