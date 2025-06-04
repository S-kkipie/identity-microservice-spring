package unsa.sistemas.identityservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.Models.PrincipalUser;

import java.util.Optional;

@Repository
public interface PrincipalUserRepository extends JpaRepository<PrincipalUser, String> {
    Optional<PrincipalUser> findByUsername(String username);
}
