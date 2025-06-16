package com.bank.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE= "User already exists";
	
	public static final String ACCOUNT_CREATION_SUCCESS= "002";
	public static final String ACCOUNT_CREATION_MESSAGE= "Account created successfully";
	
	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with provided Account Number does not exists ";
	
	
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
	
	public static final String ACCOUNT_CREDIT_SUCCESS = "005";
	public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Amount Credited";
	
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
	
	public static final String ACCOUNT_DEBIT_SUCCESS = "007";
	public static final String ACCOUNT__DEBIT_SUCCESS_MESSAGE = "Amount debited";
	
	public static final String ACCOUNT__TRANSFER_CODE = "008";
	public static final String ACCOUNT__TRANSFER_MESSAGE = "Transfer Successful";
	
	
	
	
	
	public static String generateAccountNumber() {
		
		/*
		 * Account Number generation 
		 * 
		 * current year + randomSixDigit
		 * */
		
		
		Year currentYear = Year.now();
		
		int min = 100000;
		int max = 999999;
		
		// Generate a random number between min and max
		
		int randNumber = (int)Math.floor(Math.random() * (max-min+1) + min);
		
		// convent year and randNumber into strings and concatenate them
		
		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		
		StringBuilder accountNumber = new StringBuilder();
		return accountNumber.append(year).append(randomNumber).toString();
		
	}
	

}
