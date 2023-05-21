package com.wileyedge.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.wileyedge.model.customer.CustomerModel;
import com.wileyedge.service.CustomerService;
import com.wileyedge.utilities.InputUtilities;


public class Controller {
	public static void main(String[] args) {
		Map<Integer,CustomerModel> customers = new HashMap<>(); // for temporaryStorage
		CustomerService customerService = new CustomerService(customers);
		
		// Retrieve all data from database
		if(customerService.retrieveAllCustomersFromDatabase() != null) {
			customers.putAll(customerService.retrieveAllCustomersFromDatabase());
		}
		
		int customerId;
		
		// Retrieve the lastCustId value from the database and assign it to the lastCustId variable
		int retrievedLastCustId = customerService.retrieveLastCustomerIdFromDatabase();
		
		// Find the maximum ID from the current data  
		//(Cos sometime when data lost due to error in SQL, the lastCustID record in database is not correct.
		//hence, double check it with this method. )
		int maxId = 0;
		for (CustomerModel customer : customers.values()) {
		    if (customer.getCustId() > maxId) {
		        maxId = customer.getCustId();
		    }
		}
		
		// Update lastCustId with the maximum value between retrievedLastCustId, current lastCustId, and maxId
		int newLastCustId = Math.max(Math.max(CustomerModel.getLastCustId(), retrievedLastCustId), maxId+1);
		CustomerModel.setLastCustId(newLastCustId);
		
		// Display main menu
		boolean readyToExit = false;
		Scanner scanner = new Scanner(System.in);
		while(!readyToExit) {
			System.out.println("-------------------------");
            System.out.println("Java Banking Application");
            System.out.println("-------------------------");
            System.out.println("1 - Create New Customer Data");
            System.out.println("2 - Assign a Bank Account to a Customer");
            System.out.println("3 - Display balance or interest earned of a Customer");
            System.out.println("4 - Sort Customer Data ordered by Name");
//            System.out.println("5 - Persist Customer Data");
            System.out.println("6 - Show All Customers ordered by ID");
            System.out.println("7 - Search Customers by Name");
            System.out.println("8 - Exit");
            System.out.println("9 - Withdraw bank balance by customer ID");
//            System.out.println("10 - Deposit to Customer Bank Account by customer ID");
            
            String option = scanner.nextLine();
            
            switch(option) {
            case "1": // Create New Customer Data and add it to the customerMap
            	customerService.createCustomer();
            	break;
            case "2":// Assign a Bank Account to a Customer
            	customerId = InputUtilities.getInputAsInteger("CustomerId", "Enter customer Id you want to assigg bank account : ");
            	customerService.assignBankAccountForCustomer(customerId);
            	break;
            case "3": // Display balance or interest earned of a customer
            	customerId = InputUtilities.getInputAsInteger("CustomerId", "Enter customer Id to assign bank account to : "); 
            	customerService.displayInterstEarnedForCustomer(customerId);
            	break;
            case "4": // Sort customer data 
            	System.out.println("1: Sort by name\n2: Sort by Id");
            	String sortingType = scanner.nextLine();
            	if(sortingType.equals("1")) {
            		customerService.sortByName();
            	}else {
            		customerService.sortById();
            	}
            	break;
            case "5": // Persist Customer Data to flat file 
//            	customerService.persistDataToFile(customersDataFile);
            	break;
            case "6": // Show all customers            
            	customerService.displayCustomers();
            	break;
            case "7": // Search Customers by Name
            	System.out.println("Enter customer name you want to search : ");
            	String searchName = scanner.nextLine();
            	List<CustomerModel> matchingCustomers = customerService.searchCustomersByName(searchName);
            	break;
            case "8": // Exit
            	//Persist Customers Data to Database 
            	customerService.updateAllCustomersToDatabase(customers);
            	// Exit the program
            	System.out.println("Goodbye!");
            	scanner.close();
            	readyToExit = true;
            	break;
            case "9": // Withdraw
            	customerService.withdrawal();
            	break;
////            case "10": // Deposit
////            	System.out.println("-------Deposit----TO BE COMPLETED----");
////            	break;
            default:
            		System.out.println("Option not valid - try again !");
            }
		}	
	}

}
