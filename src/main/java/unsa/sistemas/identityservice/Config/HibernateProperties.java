package unsa.sistemas.identityservice.Config;


import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import lombok.Getter;

import java.util.Properties;


@Configuration
@PropertySource("classpath:hibernate.properties")
@Getter
public class HibernateProperties {

    @Value("${connection.baseUrl}")
    private String baseUrl;

    @Value("${connection.username}")
    private String username;

    @Value("${connection.password}")
    private String password;

    @Value("${connection.driver_class}")
    private String driverClass;

    @Value("${platform.database}")
    private String platformDatabase;

    @Value("${tenant.database}")
    private String tenantDatabase;

    @Value("${dialect}")
    private String dialect;

    @Value("${hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${show_sql}")
    private String showSql;

    @Value("${format_sql}")
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
        return getPrivateProperties(props);
    }

    private Properties getPrivateProperties(Properties props) {
        props.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, getDriverClass());
        props.setProperty(AvailableSettings.DIALECT, getDialect());
        props.setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, getHbm2ddlAuto());
        props.setProperty(AvailableSettings.SHOW_SQL, getShowSql());
        props.setProperty(AvailableSettings.FORMAT_SQL, getFormatSql());
        return props;
    }

    public Properties getCommonProperties() {
        Properties props = new Properties();
        return getPrivateProperties(props);
    }
}