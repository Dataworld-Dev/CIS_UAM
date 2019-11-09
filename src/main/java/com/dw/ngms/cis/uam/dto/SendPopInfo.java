package com.dw.ngms.cis.uam.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class SendPopInfo {
	
	@JsonProperty("requestCode")
	private String requestCode;
	
	@Email(message = "Email should be a valid")
	@JsonProperty("emailAddress")
	@NotEmpty(message = "Email should not be empty")
	private String emailAddress;
}
