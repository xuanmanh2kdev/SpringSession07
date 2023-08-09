package fa.training.eventbox.service;

import fa.training.eventbox.model.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findByEmail(String email);
}
