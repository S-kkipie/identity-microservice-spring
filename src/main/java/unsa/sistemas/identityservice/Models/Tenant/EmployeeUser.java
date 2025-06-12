package unsa.sistemas.identityservice.Models.Tenant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import unsa.sistemas.identityservice.Models.AbstractUser;

import java.util.Collection;
import java.util.List;

@Data
@SuperBuilder
@Entity
@Table(name = "users")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeUser extends AbstractUser implements  UserDetails {
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }
}