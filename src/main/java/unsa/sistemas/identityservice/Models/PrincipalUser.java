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
@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "principal_users")

public class PrincipalUser extends AbstractUser implements UserDetails{
    private boolean isAdmin = true;



    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }
}
