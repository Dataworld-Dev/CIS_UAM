package com.dw.ngms.cis.uam.ldap;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserCredentials {

	private String username;
	private String password;
}
