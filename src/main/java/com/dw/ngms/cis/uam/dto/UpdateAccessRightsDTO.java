package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swaroop on 2019/04/02.
 */

@Data
@Getter
@Setter
public class UpdateAccessRightsDTO implements Serializable {

    private String usertype;
    private List<RolesDTO> roles;
    private String accessrightjson;
    private String dashboardrightjson;
}
