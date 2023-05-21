package com.wileyedge.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.wileyedge.model.customer.CustomerModel;

public interface ICustomerDAO {
	void create(CustomerModel customer);
	CustomerModel retrieve(int id);
	Map<Integer, CustomerModel> retrieveAllCustomersFromDatabase();
	void updateAllCustomersToDatabase(Map<Integer, CustomerModel> customers);
	void update(CustomerModel stu);
	int retrieveLastCustomerIdFromDatabase();
	void delete(int id);
//	void updateLastCustomerIdToDatabase(int lastCustomerId);

}
