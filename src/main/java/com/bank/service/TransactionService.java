package com.bank.service;

import com.bank.dto.TransactionRequest;
import com.bank.entity.Transaction;

public interface TransactionService {
	
	void saveTransaction(TransactionRequest transaction);

}
