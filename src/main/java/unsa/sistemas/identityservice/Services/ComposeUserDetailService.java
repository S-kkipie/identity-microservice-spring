package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unsa.sistemas.identityservice.Config.TenantContext;
import unsa.sistemas.identityservice.DTOs.RegisterRequest;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Models.Role;
import unsa.sistemas.identityservice.Models.Tenant.TenantUser;
import unsa.sistemas.identityservice.Security.SecurityPrincipal;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ComposeUserDetailService implements UserDetailsService {
    private final PrincipalUserService principalUserService;
    private final TenantUserService tenantUserService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityPrincipal securityPrincipal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (TenantContext.getTenantId() != null) {
            return tenantUserService.loadUserByUsername(username);
        } else {
            return principalUserService.loadUserByUsername(username);
        }
    }

    public UserDetails createUser(RegisterRequest request) throws IllegalStateException {
        if (TenantContext.getTenantId() != null) {
            log.debug("Creating new tenant user: {} in {}", request.getUsername(), TenantContext.getTenantId());
            return tenantUserService.createUser(TenantUser.builder()
                    .username(request.getUsername())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build());
        } else {
            log.debug("Creating new Principal user: {} in paltform_db", request.getUsername());
            return principalUserService.createUser(PrincipalUser.builder()
                    .username(request.getUsername())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build());
        }
    }

    public UserDetails findCurrentUser() {
        UserDetails user = securityPrincipal.getLoggedInPrincipal();
        if (user == null) {
            throw new RuntimeException("No authenticated user found");
        }

        return user;
    }
}
