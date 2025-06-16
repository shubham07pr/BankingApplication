package com.bank.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Authenticator.RequestorType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.dto.AccountInfo;
import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EmailDetails;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.TransactionRequest;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.utils.AccountUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
				
		// if user already exists
		
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			BankResponse response = BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
			return response;
		}
		
		
		// Creating an account: saving a new user in the db
		
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
				.status("ACTIVE")
				.build();
		
		User savedUser = userRepository.save(newUser);
		
		// Send email alert
		
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(savedUser.getEmail())  // savedUser will get this email
				.subject("ACCOUNT CREATION")
				.messageBody("Congratulations! Your account has been successfully created.\nYour Account Details: \n "
						+ "Account Name: "	+ savedUser.getFirstName() + " " + savedUser.getLastName() + "\nAccount Number: " + savedUser.getAccountNumber())
				
				.build();	
		emailService.sendEmailAlert(emailDetails);
		
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
						.build()
	
						)
				.build();
				
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in the db
		
		boolean isAccountExist =  userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();		
		}
		
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
				.accountInfo(AccountInfo.builder()
						.accountBalance(foundUser.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
						.build()) 
				.build();
		
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		
		boolean isAccountExist =  userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
						
		}
		User foundUser= userRepository.findByAccountNumber(request.getAccountNumber());
		
		return foundUser.getFirstName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {


		boolean isAccountExist =  userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();	
			
		}
		
		User creditUser = userRepository.findByAccountNumber(request.getAccountNumber());
		creditUser.setAccountBalance(creditUser.getAccountBalance().add(request.getAmount()));
		userRepository.save(creditUser);
		
		// Save Transaction
		TransactionRequest transactionRequest = TransactionRequest.builder()
				.accountNumber(creditUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.build();
		
		transactionService.saveTransaction(transactionRequest);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(creditUser.getFirstName() + " " + creditUser.getLastName())
						.accountBalance(creditUser.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {

	    // Check if account exists
	    boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
	    if (!isAccountExist) {
	        return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
	                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    // Fetch user
	    User debitUser = userRepository.findByAccountNumber(request.getAccountNumber());

	    // Check if balance is sufficient
	    if (debitUser.getAccountBalance().compareTo(request.getAmount()) < 0) {
	        return BankResponse.builder()
	                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
	                .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    // Subtract and save
	    debitUser.setAccountBalance(debitUser.getAccountBalance().subtract(request.getAmount()));
	    userRepository.save(debitUser);
	    
	    // Save Transaction
	    TransactionRequest transactionRequest = TransactionRequest.builder()
				.accountNumber(debitUser.getAccountNumber())
				.transactionType("DEBIT")
				.amount(request.getAmount())
				.build();
		
		transactionService.saveTransaction(transactionRequest);

	    // Return response
	    return BankResponse.builder()
	            .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
	            .responseMessage(AccountUtils.ACCOUNT__DEBIT_SUCCESS_MESSAGE)
	            .accountInfo(AccountInfo.builder()
	                    .accountName(debitUser.getFirstName() + " " + debitUser.getLastName())
	                    .accountBalance(debitUser.getAccountBalance())
	                    .accountNumber(request.getAccountNumber())
	                    .build())
	            .build();
	}

	@Override
	public BankResponse trasfer(TransferRequest request) {
		
		//get the account to debit from (if exists)
		// check if the debit amount is not more than the current balance and debit the account
		// get the account to credit and credit the account
		
		
		boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		
		if(!isDestinationAccountExist) {
			return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
	                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                .accountInfo(null)
	                .build();
		}
		
		User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		
		
		
		if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance())> 0) {
			return BankResponse.builder()
	                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
	                .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
	                .accountInfo(null)
	                .build();
		}
		
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();
		
		userRepository.save(sourceAccountUser);
		
		EmailDetails debitAlert = EmailDetails.builder()
				.subject("DEBIT ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current balance is : " + sourceAccountUser.getAccountBalance())
				.build();
		
		emailService.sendEmailAlert(debitAlert);
		
		
		
		User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        
//        String recipientUserName = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName();
        userRepository.save(destinationAccountUser);
        
        EmailDetails creditAlert = EmailDetails.builder()
				.subject("CREDIT ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The sum of " + request.getAmount() + " has been sent to your account from " + sourceUserName + ". Your current balance is: " + sourceAccountUser.getAccountBalance())
				.build();
		
		emailService.sendEmailAlert(creditAlert);
		
		TransactionRequest transactionRequest = TransactionRequest.builder()
				.accountNumber(destinationAccountUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.build();
		
		transactionService.saveTransaction(transactionRequest);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT__TRANSFER_CODE)
				.responseMessage(AccountUtils.ACCOUNT__TRANSFER_MESSAGE)
				.accountInfo(null)
				
				.build();
	}

	@Override
	public BankResponse updateAccount(String email, UserRequest userRequest) {
		
		User existingUser = userRepository.findByEmail(email);

	    if (existingUser == null) {
	        return BankResponse.builder()
	                .responseCode("404")
	                .responseMessage("User with email " + email + " not found")
	                .accountInfo(null)
	                .build();
	    }

	    // Update the fields
	    existingUser.setFirstName(userRequest.getFirstName());
	    existingUser.setLastName(userRequest.getLastName());
	    existingUser.setGender(userRequest.getGender());
	    existingUser.setAddress(userRequest.getAddress());
	    existingUser.setStateOfOrigin(userRequest.getStateOfOrigin());
	    existingUser.setPhoneNumber(userRequest.getPhoneNumber());
	    existingUser.setAlternatePhoneNumber(userRequest.getAlternatePhoneNumber());

	    if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
	        existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
	    }

	    userRepository.save(existingUser);

	    return BankResponse.builder()
	            .responseCode("200")
	            .responseMessage("User updated successfully")
	            .accountInfo(AccountInfo.builder()
	                    .accountName(existingUser.getFirstName() + " " + existingUser.getLastName())
	                    .accountNumber(existingUser.getAccountNumber())
	                    .accountBalance(existingUser.getAccountBalance())
	                    .build())
	            .build();
		
	}

	@Override
	public BankResponse deleteAccount(String email) {
		User existingUser = userRepository.findByEmail(email);

	    if (existingUser == null) {
	        return BankResponse.builder()
	                .responseCode("404")
	                .responseMessage("User with email " + email + " not found")
	                .accountInfo(null)
	                .build();
	    }
		    userRepository.delete(existingUser);

		    return BankResponse.builder()
		            .responseCode("200")
		            .responseMessage("User deleted successfully")
		            .accountInfo(null)
		            .build();
	}

	
}
