package com.wileyedge.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wileyedge.model.bankaccount.BankAccount;
import com.wileyedge.model.bankaccount.FixedDepositAccount;
import com.wileyedge.model.bankaccount.SavingAccount;
import com.wileyedge.model.customer.CustomerModel;

public class CustomerDAOImpl implements ICustomerDAO {
	
	public Connection openConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");//Type-4 driver is a registered with DriverManager
			System.out.println("MYSQL driver registred with DriverManager");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/customer","root","root");
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			System.out.println("MYSQL suitable driver not found");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void closeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// DONT USE create and createAllCustomer yet ///////////////////////////////

	@Override
	public void create(CustomerModel customer) {
	    int id = customer.getCustId();
	    String name = customer.getCustName();
	    int age = customer.getCustAge();
	    String passport = customer.getCustPassportNum();
	    LocalDate dob = customer.getDob();

	    Connection con = openConnection();
	    try {
	        String customerSql = "INSERT INTO Customer (id,name, phone, Passport, dob) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement customerPstat = con.prepareStatement(customerSql);
	        customerPstat.setInt(1, id);
	        customerPstat.setString(2, name);
	        customerPstat.setInt(3, age);
	        customerPstat.setString(4, passport);
	        customerPstat.setDate(5, java.sql.Date.valueOf(dob));
	        int customerRowsInserted = customerPstat.executeUpdate();

	        System.out.println("Number of customer rows inserted: " + customerRowsInserted);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    closeConnection(con);
	}

	public void createAllCustomers(Map<Integer, CustomerModel> customers) {
	    for (CustomerModel customer : customers.values()) {
	        create(customer);
	    }
	}
	

	/////////////////////////////////////////////////
		
	@Override
	public void updateAllCustomersToDatabase(Map<Integer, CustomerModel> customers) {
		Connection con = openConnection();
		try {
			Statement truncateStatement = con.createStatement();
			truncateStatement.addBatch("TRUNCATE TABLE Customer");
			truncateStatement.addBatch("TRUNCATE TABLE BankAccount");
			truncateStatement.executeBatch();

			String customerSql = "INSERT INTO Customer (id, name, phone, Passport, dob) VALUES (?, ?, ?, ?, ?)";
			String bankAccountSql = "INSERT INTO customer.BankAccount (accntNum, bsbCode, bankName, accntBal, accntOpeningDate, bankAccountType, isSalaryAccount, depositAmount, tenure, custId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement customerPstat = con.prepareStatement(customerSql);
			PreparedStatement bankAccountPstat = con.prepareStatement(bankAccountSql);

			for (CustomerModel customer : customers.values()) {
				customerPstat.setInt(1, customer.getCustId());
				customerPstat.setString(2, customer.getCustName());
				customerPstat.setString(3, customer.getCustMobNum());
				customerPstat.setString(4, customer.getCustPassportNum());
				customerPstat.setDate(5, java.sql.Date.valueOf(customer.getDob()));
				customerPstat.executeUpdate();

				BankAccount bankAccount = customer.getBankAccount();
				if (bankAccount != null) {
					bankAccountPstat.setLong(1, bankAccount.getAccntNum());
					bankAccountPstat.setLong(2, bankAccount.getBsbCode());
					bankAccountPstat.setString(3, bankAccount.getBankName());
					bankAccountPstat.setDouble(4, bankAccount.getAccntBal());
					bankAccountPstat.setDate(5, java.sql.Date.valueOf(bankAccount.getAccntOpeningDate()));
					bankAccountPstat.setString(6, bankAccount.getBankAccountType());

					if (bankAccount instanceof SavingAccount) {
						SavingAccount savingAccount = (SavingAccount) bankAccount;
						bankAccountPstat.setBoolean(7, savingAccount.isSalaryAccount());
						bankAccountPstat.setNull(8, Types.INTEGER);
						bankAccountPstat.setNull(9, Types.DOUBLE);
					} else if (bankAccount instanceof FixedDepositAccount) {
						FixedDepositAccount fixedDepositAccount = (FixedDepositAccount) bankAccount;
						bankAccountPstat.setBoolean(7, false);
						bankAccountPstat.setDouble(8, fixedDepositAccount.getDepositAmount());
						bankAccountPstat.setInt(9, fixedDepositAccount.getTenure());
					}
					bankAccountPstat.setInt(10, customer.getCustId());
					bankAccountPstat.executeUpdate();
				}
			}

			
			 PreparedStatement checkExistingRecordsPstat = con.prepareStatement("SELECT COUNT(*) FROM customer.customeridtracker");
		        ResultSet resultSet = checkExistingRecordsPstat.executeQuery();
		        resultSet.next();
		        int rowCount = resultSet.getInt(1);

		        if (rowCount > 0) {
		            // Existing records found, perform an update
		            PreparedStatement updateLastCustIdPstat = con.prepareStatement("UPDATE customer.customeridtracker SET LAST_CUST_ID = ?");
		            updateLastCustIdPstat.setInt(1, CustomerModel.getLastCustId());
		            updateLastCustIdPstat.executeUpdate();
		        } else {
		            // No existing records found, perform an insert
		            PreparedStatement insertLastCustIdPstat = con.prepareStatement("INSERT INTO customer.customeridtracker (LAST_CUST_ID) VALUES (?)");
		            insertLastCustIdPstat.setInt(1, CustomerModel.getLastCustId());
		            insertLastCustIdPstat.executeUpdate();
		        }

			
			
			
			

			System.out.println("All customer data and bank account records sent to the database.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeConnection(con);	
	}
	
	@Override
	public void updateLastCustomerIdToDatabase(int lastCustomerId) {
	    Connection con = openConnection();

	    try {
	        PreparedStatement checkExistingRecordsPstat = con.prepareStatement("SELECT COUNT(*) FROM customer.customeridtracker");
	        ResultSet resultSet = checkExistingRecordsPstat.executeQuery();
	        resultSet.next();
	        int rowCount = resultSet.getInt(1);

	        if (rowCount > 0) {
	            // Existing records found, perform an update
	            PreparedStatement updateLastCustIdPstat = con.prepareStatement("UPDATE customer.customeridtracker SET LAST_CUST_ID = ?");
	            updateLastCustIdPstat.setInt(1, CustomerModel.getLastCustId());
	            updateLastCustIdPstat.executeUpdate();
	        } else {
	            // No existing records found, perform an insert
	            PreparedStatement insertLastCustIdPstat = con.prepareStatement("INSERT INTO customer.customeridtracker (LAST_CUST_ID) VALUES (?)");
	            insertLastCustIdPstat.setInt(1, CustomerModel.getLastCustId());
	            insertLastCustIdPstat.executeUpdate();
	        }
	        
	        System.out.println("updated last id of " + lastCustomerId + " successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    closeConnection(con);
	}
	
	
	
	

	@Override
	public Map<Integer, CustomerModel> retrieveAllCustomersFromDatabase() {
		Map<Integer, CustomerModel> customers = new HashMap<>();
	    Connection con = openConnection();

	    try {
	        // Retrieve customer and bank account data using a JOIN operation
	        String sql = "SELECT * FROM Customer c LEFT JOIN BankAccount b ON c.id = b.custId";
	        PreparedStatement statement = con.prepareStatement(sql);
	        ResultSet rs = statement.executeQuery();

	        // Iterate over the result set and populate the customers map
	        while (rs.next()) {
	            int custId = rs.getInt("id");
	            String custName = rs.getString("name");
	            String custMobNum = rs.getString("phone");
	            String custPassportNum = rs.getString("passport");
	            LocalDate dob = rs.getDate("dob").toLocalDate();

	            CustomerModel customer = new CustomerModel(custId, custName, custMobNum, custPassportNum, dob);
	            customers.put(custId, customer);
	            
	            BankAccount bankAccount = null;
	            long accntNum = rs.getLong("accntNum");
	            if (accntNum != 0) {
	            	long bsbCode = rs.getLong("bsbCode");
		            String bankName = rs.getString("bankName");
		            double accntBal = rs.getDouble("accntBal");
		            LocalDate accntOpeningDate = rs.getDate("accntOpeningDate").toLocalDate();
		            String bankAccountType = rs.getString("bankAccountType");

		            if (bankAccountType.equalsIgnoreCase("Saving Account")) {
	                    boolean isSalaryAccount = rs.getBoolean("isSalaryAccount");
	                    
	                    float minBalance = isSalaryAccount ? 0 : 100;
	                    bankAccount = new SavingAccount(accntNum, bsbCode, bankName, accntBal, accntOpeningDate, isSalaryAccount, minBalance);
	                    bankAccount.setBankAccountType("Saving Account");
	                } else if (bankAccountType.equalsIgnoreCase("Fixed Deposit Account")) {
	                    int tenure = rs.getInt("tenure");
	                    double depositAmount = rs.getDouble("depositAmount");
	                    bankAccount = new FixedDepositAccount(accntNum, bsbCode, bankName, accntBal, accntOpeningDate, depositAmount, tenure);
	                    bankAccount.setBankAccountType("Fixed Deposit Account");
	                }
	            }
	            
	            customer.setBankAccount(bankAccount);
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    closeConnection(con);
	    return customers;
	}

	@Override
	public CustomerModel retrieve(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CustomerModel stu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public int retrieveLastCustomerIdFromDatabase() {
		Connection con = openConnection();
		String sql = "SELECT last_cust_id FROM customeridtracker;";
		Statement stat;
		int retrieved_last_cust_id = 0;
		try {
			stat = con.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if(rs.next()) {
				retrieved_last_cust_id = rs.getInt("last_cust_id");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		closeConnection(con);
		return retrieved_last_cust_id;
	}




	
	
	
	
	
}
