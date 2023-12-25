package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findByEmailIgnoreCase(String email);

    @Modifying
    @Transactional
    @Query(value = "delete from customer_flights cf where cf.customer_id = :c_id ; " +
                    "delete from reservations r where r.customer_id = :c_id ; " +
                    "delete from customers c where c.customer_id = :c_id", nativeQuery = true)
    void deleteCustomerById(@Param("c_id") Integer customerId);
}
