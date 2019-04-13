package com.dw.ngms.cis.uam.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by swaroop on 2019/03/26.
 */
@Data
@Setter
@Getter
@ToString
public class ExternalUserAssistantDTO implements Serializable {

	private static final long serialVersionUID = -753283478574257946L;
	
	@NotEmpty(message = "Surveyer User Code must not be empty")
    private String surveyorusercode;
    @NotEmpty(message = "Surveyor User Name must not be empty")
    private String surveyorusername;
    @NotEmpty(message = "Assistant User Code not be empty")
    private String assistantusercode;
    @NotEmpty(message = "Assistance User Name not be empty")
    private String assistantusername;
    private String rejectionreason;
    private String isapproved;
    
	public String getSurveyorusercode() {
		return surveyorusercode;
	}
	public void setSurveyorusercode(String surveyorusercode) {
		this.surveyorusercode = surveyorusercode;
	}
	public String getSurveyorusername() {
		return surveyorusername;
	}
	public void setSurveyorusername(String surveyorusername) {
		this.surveyorusername = surveyorusername;
	}
	public String getAssistantusercode() {
		return assistantusercode;
	}
	public void setAssistantusercode(String assistantusercode) {
		this.assistantusercode = assistantusercode;
	}
	public String getAssistantusername() {
		return assistantusername;
	}
	public void setAssistantusername(String assistantusername) {
		this.assistantusername = assistantusername;
	}
	public String getRejectionreason() {
		return rejectionreason;
	}
	public void setRejectionreason(String rejectionreason) {
		this.rejectionreason = rejectionreason;
	}
	public String getIsapproved() {
		return isapproved;
	}
	public void setIsapproved(String isapproved) {
		this.isapproved = isapproved;
	}
   
}
