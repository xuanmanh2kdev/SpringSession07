package fa.training.eventbox.security;

import fa.training.eventbox.model.entity.Customer;
import fa.training.eventbox.model.enums.UserRole;
import fa.training.eventbox.service.CustomerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    public UserDetailsServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerService.findByEmail(username);
        Customer customer = customerOptional
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        List<GrantedAuthority> authority = Collections.singletonList(
                new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.name()));
        // UserRole.ROLE_CUSTOMER.name() -> "ROLE_CUSTOMER"
        return new User(username, customer.getPassword(), authority);
    }
}
