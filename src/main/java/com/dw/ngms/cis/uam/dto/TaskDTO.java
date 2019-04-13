package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/04/06.
 */

@Data
@Getter
@Setter
@ToString
public class TaskDTO {

    private String taskCode;
    private String taskReferenceCode;
    private String taskReferenceType;
    private String taskCloseDesc;
    private String taskDoneByUserCode;
    private String taskDoneByUserName;



}
