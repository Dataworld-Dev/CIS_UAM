package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * Created by swaroop on 2019/03/26.
 */

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 8918972033622516172L;
	

    private String usercode;
    private String username;
    private String isapproved;
    private String rejectionreason;
    private String isactive;
	private String email;
	private List<RegisteredCountDTO> registeredCountDTOs;
    private List<SecurityQuestionDTO> question;
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIsapproved() {
		return isapproved;
	}
	public void setIsapproved(String isapproved) {
		this.isapproved = isapproved;
	}
	public String getRejectionreason() {
		return rejectionreason;
	}
	public void setRejectionreason(String rejectionreason) {
		this.rejectionreason = rejectionreason;
	}
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public List<SecurityQuestionDTO> getQuestion() {
		return question;
	}
	public void setQuestion(List<SecurityQuestionDTO> question) {
		this.question = question;
	}

}
