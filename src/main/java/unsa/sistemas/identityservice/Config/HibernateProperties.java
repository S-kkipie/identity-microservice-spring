package unsa.sistemas.identityservice.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.hibernate.cfg.AvailableSettings;
import lombok.Getter;

import java.util.Properties;


@Configuration
@PropertySource("classpath:hibernate.properties")
@Getter
public class HibernateProperties {

    @Value("${hibernate.connection.url}")
    private String baseUrl;

    @Value("${hibernate.connection.username}")
    private String username;

    @Value("${hibernate.connection.password}")
    private String password;

    @Value("${hibernate.connection.driver_class}")
    private String driverClass;

    @Value("${hibernate.platform.database}")
    private String platformDatabase;

    @Value("${hibernate.tenant.database}")
    private String tenantDatabase;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${hibernate.show_sql}")
    private String showSql;

    @Value("${hibernate.format_sql}")
    private String formatSql;

    public String getPlatformUrl() {
        return getBaseUrl() + "/" + getPlatformDatabase();
    }

    public String getTenantUrl() {
        return getBaseUrl() + "/" + getTenantDatabase();
    }

    public Properties getPlatformProperties() {
        Properties props = new Properties();
        props.setProperty(AvailableSettings.JAKARTA_JDBC_URL, getPlatformUrl());
        return getProperties(props);
    }

    public Properties getTenantProperties() {
        Properties props = new Properties();
        props.setProperty(AvailableSettings.JAKARTA_JDBC_URL, getTenantUrl());
        return getProperties(props);
    }

    private Properties getProperties(Properties props) {
        props.setProperty(AvailableSettings.JAKARTA_JDBC_USER, getUsername());
        props.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, getPassword());
        props.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, getDriverClass());
        props.setProperty(AvailableSettings.DIALECT, getDialect());
        props.setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, getHbm2ddlAuto());
        props.setProperty(AvailableSettings.SHOW_SQL, getShowSql());
        props.setProperty(AvailableSettings.FORMAT_SQL, getFormatSql());
        return props;
    }
}