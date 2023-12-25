package com.example.ticketmanagement.services;

import com.example.ticketmanagement.models.Customer;
import com.example.ticketmanagement.repositories.CustomerRepository;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Set<Customer> getCustomers() {
        Set<Customer> customersList = new LinkedHashSet<>();
        Iterable<Customer> customers = customerRepository.findAll();
        if(customers != null) {
            customers.forEach(customer -> customersList.add(customer));
            return customersList;
        }
        return null;
    }

    public Customer getCustomerById(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    public Customer getCustomerByEmail(String email) {
        if(Util.validateEmail(email))
            return customerRepository.findByEmailIgnoreCase(email);
        return null;
    }

    public boolean deleteCustomerById(Integer customerId) {
        if(Util.validateNumber(customerId)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
            if(optionalCustomer.isEmpty())
                throw new NoSuchElementException("Customer does not exists with id = " +customerId);
            customerRepository.deleteCustomerById(customerId);
            return true;
        }
        return false;
    }

}
