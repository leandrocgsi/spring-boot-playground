package br.com.erudio.services.implementations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.models.Customer;
import br.com.erudio.repository.CustomerRepository;
import br.com.erudio.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	 private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Creating a customer");
    	}
		return customerRepository.save(customer);
    }

    @Override
    public Customer findById(String customerId) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Finding a customer by ID");
    	}
        return customerRepository.findByIdCustomer(customerId);
    }

    @Override
    public List<Customer> findAll() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Finding all customers");
    	}
        return customerRepository.findAll();
    }
    
    @Override
    public Customer update(Customer customer) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Updating a customer");
    	}
    	return customerRepository.save(customer);
    }

    @Override
    public void delete(String customerId) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Deleting a customer");
    	}
    	customerRepository.delete(new Customer());;
    }
}
