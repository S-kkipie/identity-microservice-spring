package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.Config.AppProperties;
import unsa.sistemas.identityservice.DTOs.AbstractUserDTO;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Repositories.Principal.PrincipalUserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PrincipalUserService implements UserDetailsService {
    private final PrincipalUserRepository repository;
    private final AppProperties properties;

    @Override
    public PrincipalUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public PrincipalUser loadUserById(String id) throws UsernameNotFoundException {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(id));
    }

    public PrincipalUser createUser(PrincipalUser principalUser) {
        if (repository.findByUsername(principalUser.getUsername()).isPresent()) {
            throw new IllegalStateException("This username is already taken: " + principalUser.getUsername());
        }
        return repository.save(principalUser);
    }

    public PrincipalUser updateUser(String id, AbstractUserDTO dto) {
        PrincipalUser existingUser = repository.findById(id)
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
        // No se actualiza createdAt ni hasPremiumAccess aquí
        return repository.save(existingUser);
    }

    public void deleteUser(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        repository.deleteById(id);
    }

    public Page<PrincipalUser> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page, properties.getPageSize());
        return repository.findAll(pageable);
    }
}
