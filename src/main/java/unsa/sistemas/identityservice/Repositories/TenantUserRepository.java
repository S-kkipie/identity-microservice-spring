package unsa.sistemas.identityservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.Models.TenantUser;

import java.util.Optional;

@Repository
public interface TenantUserRepository extends JpaRepository<TenantUser, String> {
    Optional<TenantUser> findByUsername(String username);
}
