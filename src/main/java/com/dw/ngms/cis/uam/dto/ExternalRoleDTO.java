package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by swaroop on 2019/04/09.
 */
@Data
@Getter
@Setter
@ToString
public class ExternalRoleDTO implements Serializable {

    private String usercode;
    private String rolecode;
    private String rolename;
    private String provincecode;
    private String provincename;
    private String username;
    private String userId;
    private String externalrolecode;

}
