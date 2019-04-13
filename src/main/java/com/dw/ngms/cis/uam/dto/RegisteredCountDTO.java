package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by swaroop on 2019/04/11.
 */
@Data
@Getter
@Setter
public class RegisteredCountDTO implements Serializable {

    private String userProvinceCode;
    private String userProvinceName;
}
