package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unsa.sistemas.identityservice.DTOs.AbstractUserDTO;
import unsa.sistemas.identityservice.Models.Tenant.EmployeeUser;
import unsa.sistemas.identityservice.Repositories.Tenant.EmployeeUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "tenantTransactionManager")
public class EmployeeUserService implements UserDetailsService {
    private final EmployeeUserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public EmployeeUser loadUserById(String id) throws UsernameNotFoundException {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(id));
    }

    public EmployeeUser createUser(EmployeeUser user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("This username is already taken: " + user.getUsername());
        }
        return repository.save(user);
    }

    public EmployeeUser updateUser(String id, AbstractUserDTO dto) {
        EmployeeUser existingUser = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        existingUser.setUsername(dto.getUsername());
        existingUser.setPassword(dto.getPassword());
        existingUser.setRole(dto.getRole());
        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setCountry(dto.getCountry());
        existingUser.setPhoneNumber(dto.getPhoneNumber());
        existingUser.setImageUrl(dto.getImageUrl());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return repository.save(existingUser);
    }

    public void deleteUser(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        repository.deleteById(id);
    }

    public List<EmployeeUser> getAllUsers() {
        return repository.findAll();
    }
}
