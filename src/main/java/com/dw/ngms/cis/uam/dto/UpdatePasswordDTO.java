package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by swaroop on 2019/04/03.
 */

@Data
@Getter
@Setter
@ToString
public class UpdatePasswordDTO implements Serializable {

    private String usercode;
    private String username;
    private String oldpassword;
    private String newpassword;
    private String type;
    private String firstlogin;


}
