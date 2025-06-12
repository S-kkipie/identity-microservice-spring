package unsa.sistemas.identityservice.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.Config.Context.OrgContext;
import unsa.sistemas.identityservice.DTOs.RegisterRequest;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Models.Role;
import unsa.sistemas.identityservice.Models.Tenant.EmployeeUser;
import unsa.sistemas.identityservice.Security.SecurityPrincipal;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ComposeUserDetailService implements UserDetailsService {
    private final PrincipalUserService principalUserService;
    private final EmployeeUserService employeeUserService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityPrincipal securityPrincipal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (OrgContext.getOrgCode() != null) {
            return employeeUserService.loadUserByUsername(username);
        } else {
            return principalUserService.loadUserByUsername(username);
        }
    }

    public UserDetails createUser(RegisterRequest request) throws IllegalStateException {
        log.debug("Using context : {}", OrgContext.getOrgCode());
        if (OrgContext.getOrgCode() != null) {
            log.debug("Creating new tenant user: {} in {}", request.getUsername(), OrgContext.getOrgCode());
            return employeeUserService.createUser(EmployeeUser.builder()
                    .username(request.getUsername())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .phoneNumber(request.getPhoneNumber())
                    .country(request.getCountry())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build());
        } else {
            log.debug("Creating new Principal user: {} in platform_db", request.getUsername());
            return principalUserService.createUser(PrincipalUser.builder()
                    .username(request.getUsername())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .firstName(request.getFirstName())
                    .phoneNumber(request.getPhoneNumber())
                    .country(request.getCountry())
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
