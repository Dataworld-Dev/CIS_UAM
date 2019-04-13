package com.dw.ngms.cis.uam.jsonresponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by swaroop on 2019/03/21.
 */

@Data
@Getter
@Setter
public class UserControllerResponse {

    private String exists;
    private String valid;
	private String isApproved;
	private String active;
	private String firstLogin;
	private String message;
    
	public String getExists() {
		return exists;
	}
	public void setExists(String exists) {
		this.exists = exists;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}

}
