package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.service.UserService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	@Operation(
			summary = "Create New User Account"
			
			)
	
	@PostMapping
	 public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest){
		 return new ResponseEntity<BankResponse>(userService.createAccount(userRequest), HttpStatus.CREATED);
		 
	 }
	
	@PostMapping("/credit")
	public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest request){
		return new ResponseEntity<BankResponse>(userService.creditAccount(request), HttpStatus.CREATED);
	}
	
	@PostMapping("/debit")
	public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest request){
		return new ResponseEntity<BankResponse>(userService.debitAccount(request), HttpStatus.CREATED);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<BankResponse> transfer(@RequestBody TransferRequest request){
		return new ResponseEntity<BankResponse>(userService.trasfer(request), HttpStatus.CREATED);
	}
	
	@Operation(
			summary = "Balance Enquiry"
			)
	
	@GetMapping("/balanceEnquiry")
	public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request){
		return new ResponseEntity<BankResponse>(userService.balanceEnquiry(request), HttpStatus.OK);
	}
	
	@GetMapping("/nameEnquiry")
	public ResponseEntity<String> nameEnquiry(@RequestBody EnquiryRequest request){
		return new ResponseEntity<String>(userService.nameEnquiry(request), HttpStatus.OK);
	}
	
	@PutMapping("/update/{email}")
	public ResponseEntity<BankResponse>updateAccount(@PathVariable String email, @RequestBody UserRequest userRequest){
		return new ResponseEntity<BankResponse>(userService.updateAccount(email, userRequest), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<BankResponse> deleteAccount(@PathVariable String email) {
	    return new ResponseEntity<>(userService.deleteAccount(email), HttpStatus.OK);
	}

}
