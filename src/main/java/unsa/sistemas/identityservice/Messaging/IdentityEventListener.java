package unsa.sistemas.identityservice.Messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import unsa.sistemas.identityservice.Config.MultiTenantImpl.DataSourceBasedMultiTenantConnectionProviderImpl;
import unsa.sistemas.identityservice.Config.HibernateProperties;
import unsa.sistemas.identityservice.DTOs.CreateDataBaseEvent;
import unsa.sistemas.identityservice.Services.PrincipalUserService;
import unsa.sistemas.identityservice.Services.SchemaService;
import unsa.sistemas.identityservice.Utils.EncryptionUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class IdentityEventListener {
    private final HibernateProperties hibernateProperties;
    private final PrincipalUserService principalUserService;
    private final SchemaService schemaService;
    private final DataSourceBasedMultiTenantConnectionProviderImpl connectionProvider;
    private final EncryptionUtil encryptionUtil;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleNewDatabase(CreateDataBaseEvent event) throws Exception {
        String decryptedJson = encryptionUtil.decrypt(event.getEncryptedPayload());
        //The database always are orgCode+_identity_db
        // The username provided is actually the id of the user
        Map<String, String> dbInfo = new ObjectMapper().readValue(decryptedJson, new TypeReference<>() {
        });

        log.debug(dbInfo.toString());

        String baseUrl = dbInfo.get("url");
        String username = dbInfo.get("username");
        String password = dbInfo.get("password");
        String code = dbInfo.get("orgCode");
        UserDetails user = principalUserService.loadUserById(username);

        String dbName = code + "_identity_db";

        String url = baseUrl + "/" + dbName;

        try {
            DataSource newDataSource = DataSourceBuilder.create()
                    .url(url)
                    .username(username)
                    .password(password)
                    .driverClassName(hibernateProperties.getDriverClass())
                    .build();

            try (Connection conn = newDataSource.getConnection()) {
                if (conn.isValid(5)) {
                    log.info("Successfully connected to tenant database: {}", code);
                } else {
                    throw new SQLException("Invalid connection for tenant: " + code);
                }
            }

            schemaService.createSchemaForTenant(newDataSource, password);

            connectionProvider.addDataSource(code, newDataSource);

        } catch (Exception e) {
            log.error("Failed to create or verify DataSource for tenant {}: {}", code, e.getMessage(), e);
            throw new RuntimeException("Could not establish connection to the tenant database: " + code, e);
        }


        log.debug("Message received, database: {} registered for user {}", dbName, user.getUsername());
    }
}

