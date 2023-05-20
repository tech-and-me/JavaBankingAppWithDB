package com.wileyedge.customer;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.wileyedge.bankaccount.BankAccount;
import com.wileyedge.utilities.InputUtilities;

public class Customer implements Serializable {
	private int custId;
	private String custName;
	private int custAge;
	private int custMobNum;
	private String custPassportNum;
	private String dob;
	private BankAccount bankAccount;
	
	private static int lastCustId = 100;
	
	public Customer() {
		super();
	}

	public Customer(String custName, int custMobNum, String custPassportNum) {
		super();
		this.custId = lastCustId;
		this.custName = custName;	
		this.custMobNum = custMobNum;
		this.custPassportNum = custPassportNum;
		this.setDob();
		this.setCustAge();
		
		lastCustId++;
	}
	
	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getCustAge() {
		return custAge;
	}

	public void setCustAge() {
		// Calculate age based on DOB
        String[] dateParts = dob.split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        LocalDate dobDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();
        Period agePeriod = Period.between(dobDate, currentDate);
        this.custAge = agePeriod.getYears();
	}

	public int getCustMobNum() {
		return custMobNum;
	}

	public void setCustMobNum(int custMobNum) {
		this.custMobNum = custMobNum;
	}

	public String getCustPassportNum() {
		return custPassportNum;
	}

	public void setCustPassportNum(String custPassportNum) {
		this.custPassportNum = custPassportNum;
	}

	public static int getLastCustId() {
		return lastCustId;
	}

	public static void setLastCustId(int lastCustId) {
		Customer.lastCustId = lastCustId;
	}
	
	public void setDob() {
		//Get Date of Birth
    	String promptGetOpeningDate = "Enter date of birth DD/MM/YYYY : ";
    	LocalDate minDate = LocalDate.now().minusYears(100); // customer can be 100 years old from current date.
    	LocalDate maxDate = LocalDate.now().minusYears(18); // customer must be at least 18 years old as of current date.
    	
    	String dob = InputUtilities.getInputAsDate(promptGetOpeningDate,minDate,maxDate);
		
		this.dob = dob;
	  }
	
	
	
	public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setCustomerBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    
    
		
	@Override
	public String toString() {
		String bankName = "N/A";
		String accountNumber = "N/A";
		double bankBalance = 0.00;
		if(this.bankAccount != null) {
			bankName = this.bankAccount.getBankName();
			accountNumber = String.valueOf(this.bankAccount.getAccntNum());
			bankBalance = this.bankAccount.getAccntBal();
		}
		return "ID: " + this.custId + " | " + "NAME: " + this.custName + " | " 
				+ "AGE: " + this.custAge 	+ " | " + "MOBILE: " + this.custMobNum + " | " + "Bank Name: " + bankName + " | " 
		+ "Bank Account Number: " + accountNumber + " | " + "Bank Balance: " + bankBalance;
	}
	
	
}
