package com.dw.ngms.cis.uam.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by swaroop on 2019/04/26.
 */
@Getter
@Setter
public class RequestsJsonDTO {

    private String requestDocuments[];
    private String paymentConfirmation[];
    private String invoiceDocs[];
    private String dispatchDocs[];


}
