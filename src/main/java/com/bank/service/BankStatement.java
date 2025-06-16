package com.bank.service;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
	
	
	/* retrieve list of transaction within a data range given by an account number
	 * generate a pdf file of transaction
	 * send the file while via email
	 * */
	
	private TransactionRepository transactionRepository;
	
//	private static final String FILE = "C:\\Users\\Kumar Shubham\\OneDrive\\Documents\\MyStatement.pdf";
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate){
	    LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
	    LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

	    List<Transaction> transactions = transactionRepository.findAll().stream()
	            .filter(transaction -> transaction.getAccountNumber() != null && transaction.getAccountNumber().equals(accountNumber))
	            .filter(transaction -> transaction.getCreatedAt() != null)
	            .filter(transaction -> {
	                LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
	                return (!transactionDate.isBefore(start) && !transactionDate.isAfter(end));
	            })
	            .toList();
	    
	    return transactions;
	   
	    
	}

}
