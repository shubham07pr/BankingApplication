package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	
	private String firstName;
	private String lastName;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String email;
	private String password;
	private String phoneNumber;
	private String alternatePhoneNumber;
	
}
