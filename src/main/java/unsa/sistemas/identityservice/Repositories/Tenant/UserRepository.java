package unsa.sistemas.identityservice.Repositories.Tenant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.Models.Tenant.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Page<User> findByFirstNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String email,String lastName, Pageable pageable);
}
