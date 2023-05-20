package com.wileyedge.model.customer;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.wileyedge.model.bankaccount.BankAccount;
import com.wileyedge.utilities.InputUtilities;

public class CustomerModel implements Serializable {
	private int custId;
	private String custName;
	private int custAge;
	private String custMobNum;
	private String custPassportNum;
	private LocalDate dob;
	private BankAccount bankAccount;
	
	private static int lastCustId = 100;
	
	public CustomerModel() {
		super();
	}

	public CustomerModel(String custName, String custMobNum, String custPassportNum,LocalDate dob) {
		super();
		this.custId = lastCustId;
		this.custName = custName;	
		this.custMobNum = custMobNum;
		this.custPassportNum = custPassportNum;
		this.dob = dob;
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
		LocalDate currentDate = LocalDate.now();
		Period agePeriod = Period.between(dob,currentDate);
		int age = agePeriod.getYears();
		this.custAge = age;
		
	}

	public String getCustMobNum() {
		return custMobNum;
	}

	public void setCustMobNum(String custMobNum) {
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
		CustomerModel.lastCustId = lastCustId;
	}
	
	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
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
		return "custId=" + custId + ", custName=" + custName + ", custAge=" + custAge + ", custMobNum="
				+ custMobNum + " , bankAccount=" + bankAccount;
				
	}
    
//	@Override
//	public String toString() {
//		String bankName = "N/A";
//		String accountNumber = "N/A";
//		double bankBalance = 0.00;
//		if(this.bankAccount != null) {
//			bankName = this.bankAccount.getBankName();
//			accountNumber = String.valueOf(this.bankAccount.getAccntNum());
//			bankBalance = this.bankAccount.getAccntBal();
//		}
//		return "ID: " + this.custId + " | " + "NAME: " + this.custName + " | " 
//				+ "AGE: " + this.custAge 	+ " | " + "MOBILE: " + this.custMobNum + " | " + "Bank Name: " + bankName + " | " 
//		+ "Bank Account Number: " + accountNumber + " | " + "Bank Balance: " + bankBalance;
//	}
	
	
	
	
	
}
