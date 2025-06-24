package unsa.sistemas.identityservice.Config.InitialUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Models.Role;
import unsa.sistemas.identityservice.Services.PrincipalUserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUserLoader implements ApplicationRunner {

    private final PrincipalUserService principalUserService;
    private final PasswordEncoder passwordEncoder;
    private final InitialUserProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        try {
            PrincipalUser user = principalUserService.loadUserByUsername(properties.getUsername());
            log.debug("Loaded user: {}", user);
        } catch (Exception e) {
            log.debug("User doesn't exist: {}", properties.getUsername());
            PrincipalUser user = PrincipalUser.builder()
                    .username(properties.getUsername())
                    .password(passwordEncoder.encode(properties.getPassword()))
                    .role(Role.ROLE_PRINCIPAL_ADMIN)
                    .hasPremiumAccess(true)
                    .build();

            principalUserService.createUser(user);
            log.info("Initial user created: {}", user.getUsername());
        }
    }
}
