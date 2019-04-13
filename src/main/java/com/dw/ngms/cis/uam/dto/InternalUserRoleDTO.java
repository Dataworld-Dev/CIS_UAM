package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by swaroop on 2019/03/28.
 */

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternalUserRoleDTO implements Serializable {

    private static final long serialVersionUID = -3886666149331363345L;

    private String userCode;
    private String userName;
    private String provinceCode;
    private String provinceName;
    private String sectionCode;
    private String sectionName;
    private String roleCode;
    private String roleName;
    private String internalRoleCode;
    
}
