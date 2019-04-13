package com.dw.ngms.cis.uam.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by swaroop on 2019/03/27.
 */
@Data
@Getter
@Setter
@ToString
public class SecurityQuestionDTO implements Serializable {

	private static final long serialVersionUID = 8462144096554739452L;
	
	private String code;
    private String que;
    private String ans;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getQue() {
		return que;
	}
	public void setQue(String que) {
		this.que = que;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}
   
}
