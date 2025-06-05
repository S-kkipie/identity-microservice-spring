package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Repositories.Principal.PrincipalUserRepository;

@Service
@AllArgsConstructor
public class PrincipalUserService implements UserDetailsService {
    private final PrincipalUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails createUser(PrincipalUser principalUser) {
        if(repository.findByUsername(principalUser.getUsername()).isPresent()){
            throw new IllegalStateException("This username is already taken: "+ principalUser.getUsername());
        }
        return repository.save(principalUser);
    }
}
