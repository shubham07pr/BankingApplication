package com.bank.service;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
	BankResponse balanceEnquiry(EnquiryRequest request);
	String nameEnquiry(EnquiryRequest request);
	BankResponse creditAccount(CreditDebitRequest request);
	BankResponse debitAccount(CreditDebitRequest request);
	BankResponse trasfer(TransferRequest request);
    BankResponse updateAccount(String email, UserRequest userRequest);
    BankResponse deleteAccount(String email);

}
