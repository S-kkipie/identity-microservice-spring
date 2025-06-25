package unsa.sistemas.identityservice.Repositories.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;

import java.util.Optional;

@Repository
public interface PrincipalUserRepository extends JpaRepository<PrincipalUser, String> {
    Optional<PrincipalUser> findByUsername(String username);
    Page<PrincipalUser> findByFirstNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String email,String lastName, Pageable pageable);
}
