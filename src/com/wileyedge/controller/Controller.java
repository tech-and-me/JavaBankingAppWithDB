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
		File customersDataFile = new File("C:\\C353\\CustomersDataFile.txt");
		CustomerModel customer = new CustomerModel();
		Map<Integer,CustomerModel> customerMap = new HashMap<>();
		CustomerService customerService = new CustomerService(customerMap);
		int customerId;
		
//		//Read customer data from the file
//		CustomerModel[] customerData = CustomerList.readData(customersDataFile);
//
//		// If the customer data array is not null or empty, add it to the customers object
//		if (customerData != null && customerData.length > 0) {
//			customers = new CustomerList(customerData);
//			minCapacity = customerData.length + initialCapacity;
//		    customers.ensureCapacity(minCapacity);
//		}else {
//			customers = new CustomerList(initialCapacity);
//		}
        

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
            System.out.println("5 - Persist Customer Data");
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
            	System.out.println("Enter customer id you want to assign bank account : ");
            	customerId = scanner.nextInt();
            	scanner.nextLine();
            	customerService.assignBankAccountForCustomer(customerId);
            	break;
            case "3": // Display balance or interest earned of a customer
            	System.out.println("Enter customer id you want to assign bank account : ");
            	customerId = scanner.nextInt();
            	scanner.nextLine();
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
            	customerService.persistData(customersDataFile);
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
            	customerService.persistData(customersDataFile);
            	// Exit the program
            	System.out.println("Good Bye!");
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
