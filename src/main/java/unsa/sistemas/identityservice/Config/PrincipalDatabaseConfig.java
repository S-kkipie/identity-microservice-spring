package unsa.sistemas.identityservice.Config;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "unsa.sistemas.identityservice.Repositories.Principal",
        entityManagerFactoryRef = "platformEntityManager",
        transactionManagerRef = "platformTransactionManager"
)
@AllArgsConstructor
public class PrincipalDatabaseConfig {
    private HibernateProperties hibernateProperties;

    @Primary
    @Bean(name = "platformDataSource")
    public DataSource platformDataSource() {
        return DataSourceBuilder.create()
                .url(hibernateProperties.getPlatformUrl())
                .username(hibernateProperties.getUsername())
                .password(hibernateProperties.getPassword())
                .driverClassName(hibernateProperties.getDriverClass())
                .build();
    }

    @Primary
    @Bean(name = "platformEntityManager")
    public LocalContainerEntityManagerFactoryBean platformEntityManager(
            @Qualifier("platformDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("unsa.sistemas.identityservice.Models.Principal");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties.getPlatformProperties());

        return em;
    }

    @Primary
    @Bean(name = "platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(
            @Qualifier("platformEntityManager") LocalContainerEntityManagerFactoryBean platformEntityManager) {
        return new JpaTransactionManager(platformEntityManager.getObject());
    }
}
