package com.wileyedge.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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
	

	@Override
	public void create(CustomerModel customer) {
		int id = customer.getCustId();
		String fname = student.getFname();
		String lname = student.getLname();
		
		Connection con = openConnection();
		try {
			String sql = "INSERT INTO student (id,fname,lname) VALUES (?,?,?)";		
			PreparedStatement pstat = con.prepareStatement(sql);
			pstat.setInt(1, id);
			pstat.setString(2, fname);
			pstat.setString(3, lname);
			int n = pstat.executeUpdate();
			
			System.out.println("Number of rows inserted: " + n);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		closeConnection(con);

	}

	@Override
	public List<CustomerModel> retrieveAll() {
		// TODO Auto-generated method stub
		return null;
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

}
