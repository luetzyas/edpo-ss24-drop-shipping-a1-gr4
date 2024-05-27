package io.flowing.retail.crm.application;

import io.flowing.retail.crm.messages.CustomerProducer;
import io.flowing.retail.crm.messages.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import io.flowing.retail.crm.domain.Customer;
import io.flowing.retail.crm.persistence.CrmRepository;

@Service
public class CrmService {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CrmRepository crmRepository;

    @Autowired
    private CustomerProducer customerProducer;

    /**
     * Processes a new customer by checking if they already exist in the database
     * and saving them if they do not.
     *
     * @param customer the customer to process
     */
    public void processNewCustomer(Customer customer) {
        try {
            if (!crmRepository.existsByEmail(customer.getEmail())) {
                crmRepository.save(customer);
            } else {
                // Optionally handle logic if customer already exists
                // e.g., update records, log information, or throw a custom exception
                handleExistingCustomer(customer);
            }
        } catch (DataAccessException e) {
            // Log the error or handle it according to your error handling policy
            System.err.println("Error accessing database: " + e.getMessage());
            // Re-throw or handle exception as necessary
            throw new RuntimeException("Failed to process new customer", e);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("Failed to process new customer", e);
        }
    }

    /**
     * Handles business logic when a customer already exists in the database.
     *
     * @param customer the existing customer
     */
    private void handleExistingCustomer(Customer customer) {
        // Optionally handle logic if customer already exists
        System.out.println("Customer already exists: " + customer.getEmail());
    }

    /**
     * Handles business logic to update customer data in the database.
     *
     * @param newCustomerData the new customer data
     */
    public void updateCustomer(Customer newCustomerData) {
        try {
            Customer existingCustomer = crmRepository.findByEmail(newCustomerData.getEmail());
            if (existingCustomer != null) {
                existingCustomer.setName(newCustomerData.getName());
                existingCustomer.setAddress(newCustomerData.getAddress());
                crmRepository.save(existingCustomer);
                customerProducer.sendCustomerUpdate(existingCustomer);
            } else {
                // Handle the case where customer does not exist in the DB
                System.err.println("Customer not found with email: " + newCustomerData.getEmail());
                // Optionally, you could choose to create a new record
                // crmRepository.save(newCustomerData);
            }
        } catch (DataAccessException e) {
            System.err.println("Error accessing database while updating customer: " + e.getMessage());
            throw new RuntimeException("Failed to update customer", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating customer: " + e.getMessage());
            throw new RuntimeException("Failed to update customer", e);
        }
    }
}
