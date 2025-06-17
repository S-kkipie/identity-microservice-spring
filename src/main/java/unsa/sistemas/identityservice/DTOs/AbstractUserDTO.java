package unsa.sistemas.identityservice.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import unsa.sistemas.identityservice.Models.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbstractUserDTO {
    private String username;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String country;
    private Integer phoneNumber;
    private String imageUrl;
}

