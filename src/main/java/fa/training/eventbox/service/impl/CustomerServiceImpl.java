package fa.training.eventbox.service.impl;

import fa.training.eventbox.model.entity.Customer;
import fa.training.eventbox.repository.CustomerRepository;
import fa.training.eventbox.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmailAndDeletedFalse(email);
    }
}
