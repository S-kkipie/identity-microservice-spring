package unsa.sistemas.identityservice.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @Column(name = "username", unique = true)
    protected String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String firstName;
    private String lastName;
    private String country;
    private Integer phoneNumber;
    private Boolean enabled;


    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
