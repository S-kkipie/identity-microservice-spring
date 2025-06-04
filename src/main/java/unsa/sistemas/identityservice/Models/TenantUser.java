package unsa.sistemas.identityservice.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@SuperBuilder
@Entity
@Table(name = "tenant_users")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantUser extends AbstractUser implements  UserDetails {
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }
}