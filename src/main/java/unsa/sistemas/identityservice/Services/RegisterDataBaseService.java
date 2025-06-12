package unsa.sistemas.identityservice.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.Config.MultiTenantImpl.DataSourceBasedMultiTenantConnectionProviderImpl;
import unsa.sistemas.identityservice.Config.HibernateProperties;
import unsa.sistemas.identityservice.DTOs.RegisterDataBaseDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterDataBaseService {
    private final DataSourceBasedMultiTenantConnectionProviderImpl connectionProvider;
    private final HibernateProperties hibernateProperties;
    private final SchemaService schemaService;

    public String registerNewDataBase(RegisterDataBaseDTO dataBaseDTO) throws SQLException, DuplicateKeyException {
        DataSource newDataSource = createDataSource(dataBaseDTO);

        schemaService.createSchemaForTenant(newDataSource, dataBaseDTO.getPassword());
        connectionProvider.addDataSource(dataBaseDTO.getOrgCode(), newDataSource);

        log.debug("New database: {} registered for code {}", dataBaseDTO.getOrgCode() + RegisterDataBaseDTO.DBNAME, dataBaseDTO.getOrgCode());

        return "Successfully register new database : " + dataBaseDTO.getOrgCode() + RegisterDataBaseDTO.DBNAME;
    }


    public String registerExistingDataBase(RegisterDataBaseDTO dataBaseDTO) throws SQLException {
        DataSource newDataSource = createDataSource(dataBaseDTO);

        connectionProvider.addDataSource(dataBaseDTO.getOrgCode(), newDataSource);

        log.debug("Database: {} registered for code {}", dataBaseDTO.getOrgCode() + RegisterDataBaseDTO.DBNAME, dataBaseDTO.getOrgCode());

        return "Successfully register new database : " + dataBaseDTO.getOrgCode() + RegisterDataBaseDTO.DBNAME;

    }

    private DataSource createDataSource(RegisterDataBaseDTO dataBaseDTO) throws SQLException {
        if(!dataBaseDTO.getUrl().contains(RegisterDataBaseDTO.DBNAME)){
            throw new SQLException("Invalid URL");
        }

        DataSource newDataSource = DataSourceBuilder.create()
                .driverClassName(hibernateProperties.getDriverClass())
                .url(dataBaseDTO.getUrl())
                .password(dataBaseDTO.getPassword())
                .username(dataBaseDTO.getUsername())
                .build();

        try (Connection conn = newDataSource.getConnection()) {
            if (conn.isValid(5)) {
                log.info("Successfully connected to tenant database: {}", dataBaseDTO.getOrgCode());
            } else {
                throw new SQLException("Invalid connection for tenant: " + dataBaseDTO.getOrgCode());
            }
        }

        return newDataSource;
    }

}
