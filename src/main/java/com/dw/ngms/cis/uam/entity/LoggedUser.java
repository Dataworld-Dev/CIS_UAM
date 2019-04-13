package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/20.
 */
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoggedUser implements Serializable {

	private static final long serialVersionUID = -6799591290267861351L;
	
	@NotEmpty(message = "user name must not be empty")
    private String username;
    @NotEmpty(message = "password must not be empty")
    private String password;
    @NotEmpty(message = "internal must not be empty")
    private String internal;

}
