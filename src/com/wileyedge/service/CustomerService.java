package com.wileyedge.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.wileyedge.exceptions.CustomerAlreadyHasBankAccountLinkedException;
import com.wileyedge.exceptions.InsufficientBalanceException;
import com.wileyedge.exceptions.InvalidAgeRangeException;
import com.wileyedge.exceptions.InvalidCustomerIdException;
import com.wileyedge.exceptions.OutOfRangeInputException;
import com.wileyedge.model.bankaccount.BankAccount;
import com.wileyedge.model.bankaccount.FixedDepositAccount;
import com.wileyedge.model.bankaccount.SavingAccount;
import com.wileyedge.model.customer.CustomerModel;
import com.wileyedge.utilities.InputUtilities;


public class CustomerService implements ICustomerService, Serializable {
	private Map<Integer,CustomerModel> customerMap; //temporary storage
	
	public CustomerService(Map<Integer, CustomerModel> customerMap) {
		this.customerMap = customerMap;
	}

	@Override
	 public boolean customerHasNoBankAccount(CustomerModel customer) {
	    	return customer.getBankAccount() == null;
	    }
	
	@Override
	public void addCustomerToList(CustomerModel customer) {
		customerMap.put(customer.getCustId(),customer);
		System.out.println("Below customer details has been added to the customer list successfully.");
		System.out.println(customer);
	}
	    
	@Override   
	public void createCustomer() {	
			String name;
			String mobNum;
			LocalDate dob;
			String passportNum;
			String varName = "Name";
			
			boolean validInput = false;
			
			// get customer name
			do {
				String promptGetCustomerInput = "Enter customer name :";
				int minNameLength = 2;
				int maxNameLength = 20;
				varName = "Name";
				name = InputUtilities.getInputAsString(varName, promptGetCustomerInput);
				if(name.length()<2) {
					System.out.println("Name must be at least" + minNameLength +  " character long. try again.");
					continue;
				}else if(name.length()>20) {
					System.out.println("Name must be no longer than " + maxNameLength +  " character long. try again.");
					continue;
				}
				validInput = true;
			}while(!validInput);
			
			
			//Get customer dob
			try {
				String promptGetOpeningDate = "Enter date of birth DD/MM/YYYY : ";
				LocalDate minDate = LocalDate.now().minusYears(100); 
				LocalDate maxDate = LocalDate.now().minusYears(18); 
				varName = "DoB";

				dob = InputUtilities.getInputAsDate(varName,promptGetOpeningDate);

				if(dob.isBefore(minDate)) {
					throw new InvalidAgeRangeException("Custmer must be not more than 80 y.o");
				}else if (dob.isAfter(maxDate)) {
					throw new InvalidAgeRangeException("customer must be at least 18 y.o");
				}
			}catch(InvalidAgeRangeException e) {
				System.out.println(e.getMessage());
				return;
			}
	    	
			//Get customer mobile phone number
			String promptGetMobInput = "Enter customer mobile number :";
			mobNum = InputUtilities.getInputAsPhoneNumber(promptGetMobInput);

			//Get customer passport number
			
			validInput = false;
			do {
				String promptGetPassportInput = "Enter customer passport number :";
				int minPassportNumLength = 5;
				int maxPassportNumLength = 15;
				varName = "Passport Number";
				passportNum = InputUtilities.getInputAsString(varName,promptGetPassportInput);
				if(passportNum.length() <minPassportNumLength || passportNum.length() > maxPassportNumLength) {
					System.out.println("Passport number must be between " + minPassportNumLength + " and  " + maxPassportNumLength + "character long");
					System.out.println("Please try again.");
					continue;
				}
				validInput = true;
			}while(!validInput);
			
			//Create customer object
			CustomerModel customer = new CustomerModel(name,mobNum,passportNum,dob);
		
			//Add customer to list
			addCustomerToList(customer);
		}
	
	@Override       
	public void getGenericBankAccountInfo(CustomerModel customer) {    
		//Get customer account number
		String varName = "Account Number";
		String promptGetAccntNum = "Enter account number (At least 8 digits) :";
		long minAccNum = 10000000;
		boolean validInput = false;
		
		long accountNum;
		do {
			accountNum = InputUtilities.getInputAsLong(varName,promptGetAccntNum);
			if(accountNum<minAccNum) {
				System.out.println("Account number must be at least 8 digits. try again.");
				continue;
			}
			validInput = true;
		}while(!validInput);

		//Get customer account BSB Code
		String promptGetBsbCode = "Enter BSB Code :";
		long minBsbNum = 100000;
		long maxBsbNum = 900000;
		varName = "BSB Code";
		
		long bsb;
		validInput = false;
		do {
			bsb = InputUtilities.getInputAsLong(varName,promptGetBsbCode);
			if(bsb < minBsbNum || bsb > maxBsbNum) {
				System.out.println("BSB Code must be 6 digits.");
				continue;
			}
			validInput = true;
		}while(!validInput);
		
		//Get customer bank name
		String promptGetBankName = "Enter bank name :";
		int minBankName = 3;
		int maxBankName = 20;
		varName = "Bank Name";
    	String bankName = "";
    	
    	validInput = false;
    	do {
    		if(bankName.length()<minBankName | bankName.length() > maxBankName) {
    			bankName = InputUtilities.getInputAsString(varName,promptGetBankName);
    			continue;
    		}
    		validInput = true;
    	}while(!validInput);

    	//Get account balance
    	String promptGetBankBal = "Enter bank balance : ";
    	double minAccBal = 0.00;
    	varName = "Bank Balance";
    	double bankBal = 0.0;
    	try {
        	bankBal = InputUtilities.getInputAsDouble(varName,promptGetBankBal);
        	if(bankBal < minAccBal) {
        		throw new InsufficientBalanceException("Balance cannot be less than 0.00");
        	}
    	}catch(InsufficientBalanceException e) {
    		System.out.println(e.getMessage());
    		return;
    	}
    	
    	//Get Opening Date
    	String promptGetOpeningDate = "Enter opening date DD/MM/YYYY : ";
    	LocalDate todayDate = LocalDate.now();
        LocalDate minDate = todayDate.minusYears(80);
        LocalDate openingDate;
        varName = "Opening Date";
        try {
        	openingDate = InputUtilities.getInputAsDate(varName,promptGetOpeningDate);
        	if(openingDate.isAfter(todayDate)) {
        		System.out.println("Failed to assign bank account to customer.");
        		throw new OutOfRangeInputException("Opening date cannot be in the future");
        	}else if(openingDate.isBefore(minDate)) {
        		System.out.println("Failed to assign bank account to customer.");
        		throw new OutOfRangeInputException("Opening date cannot be before " + minDate);
        	}
        }catch(OutOfRangeInputException e) {
        	System.out.println(e.getMessage());
        	return;
        }

    	//Get type of bank account
    	String promptGetAccountType = "Is this a fixed deposit account or saving account ? Type F for fixed or S for Saving : ";
    	int minChar = 1;
    	int maxChar = 1;
    	varName = "Type of Account";
    	String accountType = InputUtilities.getInputAsString(varName, promptGetAccountType);
    	 	
    	//Invoke method to create bank account
    	this.assignBankAccountForCustomer(customer,accountNum,bsb,bankName,bankBal,openingDate,accountType);
	    	
	}
	
	@Override
    public void assignBankAccountForCustomer(int customerId) {
		CustomerModel customer = null;
    	try {
    		customer = getCustomerById(customerId);
    		if(customer == null) {
    			throw new InvalidCustomerIdException("Customer not found");
    		}else if (!customerHasNoBankAccount(customer)) {
    			throw new CustomerAlreadyHasBankAccountLinkedException("This customer already has a bank account linked.");
    		}
    	}catch(InvalidCustomerIdException e) {
    		System.out.println(e.getMessage());
    		return;
    	}catch(CustomerAlreadyHasBankAccountLinkedException e) {
    		System.out.println(e.getMessage());
    		return;
    	}
    	getGenericBankAccountInfo(customer);
    }
	
	
	@Override
	 public void assignBankAccountForCustomer(CustomerModel customer,long accntNum, long bsbCode, String accntName, double accntBal, LocalDate accntOpeningDate, String accntType) {
	    	BankAccount bankAccount = null;
	    	boolean isLooping = false;
	    	String varName = "Deposit";
	    	try {
	    		  if (accntType.equalsIgnoreCase("F")) {   	            
	    	            String promptGetDepositAmount = "Enter deposit amount : ";
	    	        	double minAccBal = 1000.00;
	    	        	double maxAccBal = 0;
	    	        	double depositAmount = InputUtilities.getInputAsDouble(varName,promptGetDepositAmount);
	    	        	if(depositAmount < minAccBal) {
	    	        		throw new InsufficientBalanceException("Deposit amount cannot be less than $" + minAccBal);
	    	        	}
	    	        	
	    	        	String promptGetTenure= "Enter tenure (in years)  :";
	    	        	int minTenure = 1;
	    	        	int maxTenure = 7;
	    	        	varName = "Tenure (in years)";
	    	        	int tenure = InputUtilities.getInputAsInteger(varName,promptGetTenure);
	    	        	if(tenure >=1 && tenure <=7) {
	    	        		//create bank account
		    	        	bankAccount = new FixedDepositAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate,depositAmount,tenure); 
	    	        	}else {
	    	        		throw new OutOfRangeInputException("Tenure must be between " + minTenure + " and " + maxTenure + " years. ");
	    	        	}
	    		  } else{	        	
	    	        	String promptIsSalaryAccount = "Is this salary account ? Y/N : ";
	    	        	int minChar = 1;
	    	        	int maxChar = 1;
	    	        	isLooping = true;
	    	        	varName = "Option";
	    	        	
	    	        	String isSalaryAccountStr = InputUtilities.getInputAsString(varName,promptIsSalaryAccount);
	    	        	float minBalNonSalAccnt = 100;
	    	        	
	    	        	if (isSalaryAccountStr.equalsIgnoreCase("Y")) {  	        
	    	        		bankAccount = new SavingAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate, true); 
	    	            } else {    	            	
	    	                if(accntBal < minBalNonSalAccnt) {
	    	                	throw new InsufficientBalanceException("Minimum balance required is " + minBalNonSalAccnt + " dollars.");
	    	                }
	    	                bankAccount = new SavingAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate, false,minBalNonSalAccnt);
	    	            }
	    	        } 
	    		  
	    		  customer.setCustomerBankAccount(bankAccount);
	    	      System.out.println("Bank account assigned to customer successfully");
	    		  
	    	}catch(InsufficientBalanceException e) {
	    		bankAccount = null;
	    		System.out.println(e.getMessage());
	            System.out.println("Failed to assign account to the customer.");
	            return;
	    	}catch(OutOfRangeInputException e) {
	    		System.out.println(e.getMessage());
	    		System.out.println("Failed to assign account to the customer.");
	            return;
	    	}
	    	catch(Exception e) {
	    		bankAccount = null;
	    		System.out.println("Ooop! something went wrong ! Failed to assign account to customer.");
	    		return;
	    	}
	             
	    }
	
	@Override
	public void withdrawal() {
		String varName = "Id";
		String promptGetId = "Enter customer Id for bank account withdrawal : ";
    	int min = 100;
    	int max = 0;

    	int id = InputUtilities.getInputAsInteger(varName,promptGetId);

    	//Get customer object
    	CustomerModel customer = null;
    	try{
    		customer = getCustomerById(id);
    		if(customer == null) {
    			throw new InvalidCustomerIdException("Invalid customer id. Bye!");
    		}

    		// Get amount to withdraw
    		String promptGetAmount = "Enter Amount to withdrawal : ";
    		varName = "Amount";
    		double minAmount = 5.00;
    		double maxAmount = 0;
    		double amount = 0.00;	
    		String tryAgain = "";
    		while(amount < minAmount) {
    			amount = InputUtilities.getInputAsDouble(varName,promptGetAmount); 
    			System.out.println("Withdrwal amount must be, at least, " + minAmount + "\nWould you like to try another amount ? Y/N : ");
    			tryAgain = new Scanner(System.in).nextLine();
    			if(!tryAgain.equalsIgnoreCase("Y")) {
    				break;
    			}
    		}

    		//Check type of bank account (Fixed Deposit or Saving)
    		BankAccount account = customer.getBankAccount(); // this will return fixed or saving account
    		double updatedBankBal = account.withdraw(amount); // invoke method in fixed or saving account

    	}catch(InvalidCustomerIdException e) {
    		System.out.println(e.getMessage());
    		return;
    	}
	}

	
	@Override
	public List<CustomerModel> searchCustomersByName(String name) {
        List<CustomerModel> matchingCustomers = new ArrayList<>();

        for (CustomerModel customer : customerMap.values()) {
            if (customer.getCustName().equalsIgnoreCase(name)) {
                matchingCustomers.add(customer);
            }
        }
        this.displayCustomers(matchingCustomers);
        return matchingCustomers;
    }
	
	@Override	
	public CustomerModel getCustomerById(int customerId) {
		try {
			for (CustomerModel customer : customerMap.values()) {
		        if (customer != null && customer.getCustId() == customerId) {
		            return customer;
		        }
			}
			throw new InvalidCustomerIdException("Customer not found.");
			
		}catch(InvalidCustomerIdException e){
			return null;
		}catch(Exception e) {
			System.out.println("Oooop! Something went wrong !");
			return null;
		}
	     
	}
		
	@Override
	public boolean allItemsAreNull(){
		boolean isAllNull = true;
		 for (CustomerModel element : this.customerMap.values()) {
		        if (element != null) {
		        	isAllNull = false;
		        	break;
		        }
		    }
	    return isAllNull;
	}
		
	@Override
	public void sortByName() {	
		 // Create a list of customer values from the HashMap
	    List<CustomerModel> customerList = new ArrayList<>(customerMap.values());

	    // Sort the customer list based on the customer name
	    customerList.sort(Comparator.comparing(CustomerModel::getCustName));

	    //Display customer list
	    this.displayCustomers(customerList);
	}
	
	@Override
	public void sortById() {	
		 // Create a list of customer values from the HashMap
	    List<CustomerModel> customerList = new ArrayList<>(customerMap.values());

	    // Sort the customer list based on the customer name
	    customerList.sort(Comparator.comparing(CustomerModel::getCustId));

	    //Display customer list
	    this.displayCustomers(customerList);
	}
		
	@Override
	public void displayCustomers() {
		boolean allNull = allItemsAreNull();	 
		 if(allNull) {
			 System.out.println("No customer to display");
		 }else {
			 System.out.println("\n==========Customers List==========");
			 sortById();
		 }		 	 
	}

	@Override
	public void displayCustomers(List<CustomerModel> customerList) {
    	for(CustomerModel cust: customerList) {
	        if(cust != null) {
	            System.out.println(cust);
	        }
	    }
	}
		
	@Override
	public void displayInterstEarnedForCustomer(int customerId){
			 CustomerModel customer = getCustomerById(customerId);
			    if (customer == null) {
			        return;
			    }
			    System.out.println("Interest earned for customer ID: " + customer.getCustId() + " is: " + customer.getBankAccount().calculateInterest() + " dollars.");
		}

	@Override
	public void persistData(File filename) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this.customerMap);
			System.out.println("Customer data persisted to file successfully!");	
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error persisting customer data to file: " + e.getMessage());
		}finally {
			try {
				bos.close();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


		
//	@Override
//	public static CustomerModel[] readData(File filename) {
//	    CustomerModel[] customers = null;
//	    FileInputStream fis = null;
//	    BufferedInputStream bis = null;
//	    ObjectInputStream ois = null;
//	    try {
//	        fis = new FileInputStream(filename);
//	        bis = new BufferedInputStream(fis);
//	        ois = new ObjectInputStream(bis);
//	        Object obj = ois.readObject();
//	        if(obj instanceof CustomerModel[]) {
//	        	customers = (CustomerModel[])obj;
//	        }else {
//	        	System.out.println("Obj is not an instance of Customer[]");
//	        }
//	        for(CustomerModel c:customers) {
//	        	if(c!=null) {
//	        		count++;
//	        	}
//	        }
//	        CustomerModel.setLastCustId(100 + count);
//	        System.out.println("Customer data read from file successfully!");
//	    } catch (FileNotFoundException e) {
//	        System.out.println("Error file not found: " + e.getMessage());
//	    } catch (IOException e) {
//	        System.out.println("No data to load");
//	    } catch (ClassNotFoundException e) {
//	    	System.out.println("Error - Class not found Exception" + e.getMessage());
//		} finally {
//	        try {
//	            if (ois != null) {
//	                ois.close();
//	            }
//	            if (bis != null) {
//	                bis.close();
//	            }
//	            if (fis != null) {
//	                fis.close();
//	            }
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return customers;
//	}



}
