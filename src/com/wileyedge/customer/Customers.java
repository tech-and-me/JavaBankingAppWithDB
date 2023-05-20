package com.wileyedge.customer;

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
import java.util.List;
import java.util.Scanner;

import com.wileyedge.bankaccount.BankAccount;
import com.wileyedge.bankaccount.FixedDepositAccount;
import com.wileyedge.bankaccount.SavingAccount;
import com.wileyedge.exceptions.InsufficientBalanceException;
import com.wileyedge.exceptions.InvalidCustomerIdException;
import com.wileyedge.utilities.InputUtilities;
import com.wileyedge.utilities.InputUtilitiesNoLoop;


public class Customers implements Serializable{
	private Customer[] customers;
	private static int count = 0;
	
	public Customers() {
		super();
	}

	public Customers(int initialNumOfCustomers) {
		super();
		this.customers = new Customer[initialNumOfCustomers];
	}
	
	public Customers(Customer[] customers) {
        this.customers = customers;
        count = customers.length;
    }

	public Customer[] getCustomers() {
		return customers;
	}

	public void setCustomers(Customer[] customers) {
		this.customers = customers;
	}

	public static int getCount() {
		return count;
	}
	
    public boolean customerHasNoBankAccount(Customer customer) {
    	return customer.getBankAccount() == null;
    }
    
    public void ensureCapacity(int minCapacity) {
        int oldCapacity = customers.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = Math.max(minCapacity, oldCapacity * 2);
            customers = Arrays.copyOf(customers, newCapacity);
        }
    }
    
    public void addCustomer(Customer customer) {
        // Creating new array double the size
		if(customers.length == count) {
        	Customer newCustomersList[] = new Customer[2 * count];
    
        	//copy all items of original customer list to new customer list
    		for(int i=0; i<customers.length;i++) {
    			newCustomersList[i] = customers[i];
    		}	
    		//Assigning new customers list to original customers list
    		customers = newCustomersList;
        }		
		// adding new item to the the array
		customers[count++] = customer;
		System.out.println("Custmer with Id of " + customer.getCustId() + "has been added to the list successfully.");		
    }
    
    public Customer CreateNewCustomerData() {	
		String name;
		int age = 0;
		int mobNum = 0;
		String passportNum;
		
		// get customer name
		String promptGetCustomerInput = "Enter customer name :";
		int minNameLength = 2;
		int maxNameLenght = 20;
		name = InputUtilities.getInputAsString(promptGetCustomerInput, minNameLength, maxNameLenght);
		
		//Get customer age
//		String promptGetAgeInput = "Enter customer age : ";
//		int minAge = 18;
//		int maxAge = 100;
//		age = InputUtilities.getInputAsInteger(promptGetAgeInput, minAge, maxAge);
//		
		//Get customer mobile phone number
		String promptGetMobInput = "Enter customer mobile number :";
		int minPhoneNum = 100000000;
		int maxPhoneNum = 500000000;
		mobNum = InputUtilities.getInputAsInteger(promptGetMobInput, minPhoneNum, maxPhoneNum);
		
		//Get customer passport number
		String promptGetPassportInput = "Enter customer passport number :";
		int minPassportNumLength = 5;
		int maxPassportNumLenght = 15;
		passportNum = InputUtilities.getInputAsString(promptGetPassportInput, minPassportNumLength, maxPassportNumLenght);

		// Create customer object
		Customer customer = new Customer(name,mobNum,passportNum);
		
		// Add customer to customer List
		this.addCustomer(customer);
		
		// Display customer details added
		System.out.println("Successfully added -- see details below :");
		System.out.println(customer);
		
		// return customer object
		return customer;
	}
       
    public void getBankAccountDetails(int customerId) {    	
    	//Get customer account number
    	String promptGetAccntNum = "Enter account number :";
    	long minAccNum = 100000;
    	long maxAccNum = 1000000000;
    	long accountNum = InputUtilities.getInputAsLong(promptGetAccntNum, minAccNum, maxAccNum);
    	
    	//Get customer account BSB Code
    	String promptGetBsbCode = "Enter BSB Code :";
    	long minBsbNum = 100000;
    	long maxBsbNum = 900000;
    	long bsb = InputUtilities.getInputAsLong(promptGetBsbCode, minBsbNum, maxBsbNum);
    	
    	//Get customer bank name
    	String promptGetBankName = "Enter bank name :";
    	int minBankName = 3;
    	int maxBankName = 20;
    	String bankName = InputUtilities.getInputAsString(promptGetBankName, minBankName, maxBankName);
    	
    	//Get customer account number
    	String promptGetBankBal = "Enter bank balance : ";
    	double minAccBal = 0.00;
    	double maxAccBal = 900000000.00;
    	double bankBal = InputUtilities.getInputAsDouble(promptGetBankBal, minAccBal, maxAccBal);
    	
    	//Get Opening Date
    	String promptGetOpeningDate = "Enter opening date DD/MM/YYYY : ";
    	LocalDate maxDate = LocalDate.now();
        LocalDate minDate = maxDate.minusYears(80);
    	String openingDate = InputUtilities.getInputAsDate(promptGetOpeningDate,minDate,maxDate);

    	//Get customer object from customer id
    	Customer customer = getCustomerById(customerId);
    	    	
    	//Get type of bank account
    	String promptGetAccountType = "Is this a fixed deposit account or saving account ? Type F for fixed or S for Saving : ";
    	int minChar = 1;
    	int maxChar = 1;
    	String accountType = InputUtilities.getInputAsString(promptGetAccountType, minChar, maxChar);
    	 	
    	//Invoke method to create bank account
    	this.assignBankAccountForCustomer(customer,accountNum,bsb,bankName,bankBal,openingDate,accountType);
    	
    }
    
    public void assignBankAccountForCustomer(int customerId) {
    	Customer customer = getCustomerById(customerId);
    	if( customer == null) {
    		System.out.println("Invalid customer ID.");
            return;
    	}else if(!customerHasNoBankAccount(customer)) {
    		System.out.println("This customer already has a bank account linked.");
            return;
    	}	
    	getBankAccountDetails(customerId);
    }
    
    public void assignBankAccountForCustomer(Customer customer,long accntNum, long bsbCode, String accntName, double accntBal, String accntOpeningDate, String accntType) {
    	BankAccount bankAccount;
    	try {
    		  if (accntType.equalsIgnoreCase("F")) {   	            
    	            String promptGetDepositAmount = "Enter deposit amount : ";
    	        	double minAccBal = 1000.00;
    	        	double maxAccBal = 1000000.00;
    	        	double depositAmount = InputUtilitiesNoLoop.getInputAsDouble(promptGetDepositAmount, minAccBal, maxAccBal);
   	
    	        	bankAccount = new FixedDepositAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate);
    	        	FixedDepositAccount fixedDepositAccount = (FixedDepositAccount)bankAccount;
    	        	fixedDepositAccount.setDepositAmount(depositAmount);
    	        	
    	        	String promptGetTenure= "Enter tenure (in years)  :";
    	        	int minTenure = 1;
    	        	int maxTenure = 7;
    	        	int tenure = InputUtilities.getInputAsInteger(promptGetTenure, minTenure, maxTenure);
    	        	((FixedDepositAccount)bankAccount).setTenure(tenure);	        	
    	        } else{	        	
    	        	String promptIsSalaryAccount = "Is this salary account ? Y/N : ";
    	        	int minChar = 1;
    	        	int maxChar = 1;
    	        	String isSalaryAccount = InputUtilities.getInputAsString(promptIsSalaryAccount, minChar, maxChar);
    	        	
    	        	float minBalSalAccnt = 0;
    	        	float minBalNonSalAccnt = 100;
    	        	
    	        	if (isSalaryAccount.equalsIgnoreCase("Y")) {  	        
    	        		bankAccount = new SavingAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate); 
    	                ((SavingAccount) bankAccount).setSalaryAccount(true);
    	            } else {    	            	
    	                if(accntBal < minBalNonSalAccnt) {
    	                	throw new InsufficientBalanceException("Minimum balance required is " + minBalNonSalAccnt + " dollars.");
    	                }
    	                bankAccount = new SavingAccount(accntNum, bsbCode, accntName, accntBal, accntOpeningDate);
    	                ((SavingAccount) bankAccount).setSalaryAccount(false);
    	                ((SavingAccount) bankAccount).setMinBalance(minBalNonSalAccnt);
    	            }
    	        } 
    		  
    		  customer.setCustomerBankAccount(bankAccount);
    	      System.out.println("Bank account assigned to customer successfully");
    		  
    	}catch(InsufficientBalanceException e) {
    		System.out.println(e.getMessage());
            System.out.println("Failed to assign the account to the customer. Goodbye!");
    	}catch(Exception e) {
    		System.out.println("Ooop! something went wrong ! Failed to assign account to customer. Good Bye!");
    	}
             
    }
    
    public void withdrawal(int customerId, double amount) {
    	//Get customer object
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return;
        }
        
        //Check type of bank account (Fixed Deposit or Saving)
        BankAccount account = customer.getBankAccount();
        double updatedBankBal = account.withdraw(amount);
    }

	public Customer[] searchCustomersByName(String name) {
	    Customer[] matchingCustomers = new Customer[count];
	    int matchingCount = 0;    
	    for (int i = 0; i < count; i++) {
	    	if(customers[i] != null) {
	    		if (this.customers[i].getCustName().equalsIgnoreCase(name)) {
		            matchingCustomers[matchingCount++] = customers[i];
		        }
	    	}
	    }
	    matchingCustomers = Arrays.copyOfRange(matchingCustomers, 0, matchingCount);	    
	    this.displayCustomers(matchingCustomers);
	    return matchingCustomers;
	}
	
	public Customer getCustomerById(int customerId) {
		try {
			for (Customer customer : customers) {
		        if (customer != null && customer.getCustId() == customerId) {
		            return customer;
		        }
			}
			throw new InvalidCustomerIdException("Customer not found. Please double check customer Id.");
			
		}catch(InvalidCustomerIdException e){
			System.out.println(e.getMessage());
			return null;
		}
	     
	}
	
	public boolean allItemsAreNull(){
		boolean isAllNull = true;
		 for (Customer element : this.customers) {
		        if (element != null) {
		        	isAllNull = false;
		        	break;
		        }
		    }
	    return isAllNull;
	}
	
	public void sortByName() {	
	    if(!allItemsAreNull()) {
	    	Customer[] sorted = Arrays.copyOf(customers, customers.length);
	 	    for (int i = 0; i < sorted.length - 1; i++) {
	 	        for (int j = i + 1; j < sorted.length; j++) {
	 	            if (sorted[i] != null && sorted[j] != null && sorted[i].getCustName().compareTo(sorted[j].getCustName()) > 0) {
	 	                Customer temp = sorted[i];
	 	                sorted[i] = sorted[j];
	 	                sorted[j] = temp;
	 	            }
	 	        }
	 	    }
	 	    for(Customer c:sorted) {
	 	    	if(c != null) {
	 	    		System.out.println(c);
	 	    	} 	    	
	 	    }
	    }else {
	    	System.out.println("No customer to display.");
	    }
	}
	
	public void displayCustomers() {
		boolean allNull = allItemsAreNull();	 
		 if(allNull) {
			 System.out.println("No customer to display");
		 }else {
			 System.out.println("\n==========Customers List==========");
			 displayCustomers(this.customers);
		 }		 	 
	}

	public void displayCustomers(Customer[] customers) {
    	for(Customer cust: customers) {
	        if(cust != null) {
	            System.out.println(cust);
	        }
	    }
	}
	
	public void displayInterstEarnedForCustomer(int customerId){
		 Customer customer = getCustomerById(customerId);
		    if (customer == null) {
		        return;
		    }
		    System.out.println("Interest earned for customer ID: " + customer.getCustId() + " is: " + customer.getBankAccount().calculateInterest() + " dollars.");
	}

	public void persistData(File filename) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this.customers);
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
	
	public static Customer[] readData(File filename) {
	    Customer[] customers = null;
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    ObjectInputStream ois = null;
	    try {
	        fis = new FileInputStream(filename);
	        bis = new BufferedInputStream(fis);
	        ois = new ObjectInputStream(bis);
	        Object obj = ois.readObject();
	        if(obj instanceof Customer[]) {
	        	customers = (Customer[])obj;
	        }else {
	        	System.out.println("Obj is not an instance of Customer[]");
	        }
	        for(Customer c:customers) {
	        	if(c!=null) {
	        		count++;
	        	}
	        }
	        Customer.setLastCustId(100 + count);
	        System.out.println("Customer data read from file successfully!");
	    } catch (FileNotFoundException e) {
	        System.out.println("Error file not found: " + e.getMessage());
	    } catch (IOException e) {
	        System.out.println("No data to load");
	    } catch (ClassNotFoundException e) {
	    	System.out.println("Error - Class not found Exception" + e.getMessage());
		} finally {
	        try {
	            if (ois != null) {
	                ois.close();
	            }
	            if (bis != null) {
	                bis.close();
	            }
	            if (fis != null) {
	                fis.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return customers;
	}
	
}
