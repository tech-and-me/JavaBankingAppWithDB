package com.wileyedge.dao;

import java.util.List;

import com.wileyedge.model.customer.CustomerModel;

public interface ICustomerDAO {
	void create(CustomerModel customer);
	List<CustomerModel> retrieveAll();
	CustomerModel retrieve(int id);
	void update(CustomerModel stu);
	void delete(int id);
}
