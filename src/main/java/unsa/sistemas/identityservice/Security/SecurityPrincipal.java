package unsa.sistemas.identityservice.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Models.Tenant.TenantUser;
import unsa.sistemas.identityservice.Repositories.Principal.PrincipalUserRepository;
import unsa.sistemas.identityservice.Repositories.Tenant.TenantUserRepository;

import java.util.Collection;


@RequiredArgsConstructor
@Service
public class SecurityPrincipal {

    private final PrincipalUserRepository principalUserRepository;
    private final TenantUserRepository tenantUserRepository;

    public UserDetails getLoggedInPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof TenantUser tenantUser) {
                return tenantUserRepository.findByUsername(tenantUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException(tenantUser.getUsername()));
            }

            if (principal instanceof PrincipalUser principalUser) {
                return principalUserRepository.findByUsername(principalUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException(principalUser.getUsername()));
            }
        }

        return null;
    }

    public Collection<?> getLoggedInPrincipalAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                return userDetails.getAuthorities();
            }
        }

        return null;
  }

}
