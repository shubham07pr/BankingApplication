package com.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.TransactionRequest;
import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;

@Service
public class TransactioinServiceImpl implements TransactionService{
	
	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionRequest request) {
		
		Transaction transaction = Transaction.builder()
				.transactionType(request.getTransactionType())
				.accountNumber(request.getAccountNumber())
				.amount(request.getAmount())
				.status("SUCCESS")
				.build();
		
		transactionRepository.save(transaction);
		
		System.out.println("Transaction saved successfully");
		
	}

}
