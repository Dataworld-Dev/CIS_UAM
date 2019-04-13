package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaroop on 2019/04/11.
 */
@Data
@Getter
@Setter
public class RegisterUserDTO implements Serializable {

    private String userCode;
    private List<RegisteredCountDTO> registeredCountDTOs;

}
