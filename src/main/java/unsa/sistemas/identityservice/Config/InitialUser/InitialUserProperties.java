package unsa.sistemas.identityservice.Config.InitialUser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.initial-user")
public class InitialUserProperties {
    private String username;
    private String password;
}
