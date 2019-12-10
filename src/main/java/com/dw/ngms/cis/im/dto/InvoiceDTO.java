package com.dw.ngms.cis.im.dto;

import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by ragava on 27/06/17.
 */

@Data
@Getter
@Setter
public class InvoiceDTO implements Serializable{


    private static final long serialVersionUID = 4675671622777042755L;

    private String id;

    private String fullName;
    private String orgnization;
    private String telephone;
    private String postalAddress;
    private String email;
    private String mobile;
    private String requestNumber;
    private String requestType;
    private String amount;
    private ProcessAdditionalInfo processAdditionalInfo;

}
