package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unsa.sistemas.identityservice.Models.Tenant.EmployeeUser;
import unsa.sistemas.identityservice.Repositories.Tenant.EmployeeUserRepository;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "tenantTransactionManager")
public class EmployeeUserService implements UserDetailsService {
    private final EmployeeUserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails createUser(EmployeeUser employeeUser) {
        if(repository.findByUsername(employeeUser.getUsername()).isPresent()){
            throw new IllegalStateException(employeeUser.getUsername());
        }
        EmployeeUser user = repository.save(employeeUser);
        repository.flush();
        return user;
    }
}
