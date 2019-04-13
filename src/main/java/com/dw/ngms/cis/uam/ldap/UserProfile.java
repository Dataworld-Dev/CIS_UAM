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
public class UserProfile {

	private String uid;
	private String firstName;
	private String lastName;	
	private String employeeNumber;
	private String facsimileTelephoneNumber;
	private String mail;
	private String mobile;
	private String telephoneNumber;
	private String street;
	private String city;
	private String province;
	private String postalCode;
	private boolean isExists = false;

}
