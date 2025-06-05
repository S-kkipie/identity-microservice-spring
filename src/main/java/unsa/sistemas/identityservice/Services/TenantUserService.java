package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unsa.sistemas.identityservice.Models.Tenant.TenantUser;
import unsa.sistemas.identityservice.Repositories.Tenant.TenantUserRepository;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "tenantTransactionManager")
public class TenantUserService implements UserDetailsService {
    private final TenantUserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails createUser(TenantUser tenantUser) {
        if(repository.findByUsername(tenantUser.getUsername()).isPresent()){
            throw new IllegalStateException(tenantUser.getUsername());
        }
        TenantUser user = repository.save(tenantUser);
        repository.flush();
        return user;
    }
}
