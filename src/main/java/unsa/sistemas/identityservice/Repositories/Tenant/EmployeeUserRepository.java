package unsa.sistemas.identityservice.Repositories.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsa.sistemas.identityservice.Models.Tenant.EmployeeUser;

import java.util.Optional;

@Repository
public interface EmployeeUserRepository extends JpaRepository<EmployeeUser, String> {
    Optional<EmployeeUser> findByUsername(String username);
}
