package unsa.sistemas.identityservice.Config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {
    private final DataSource defaultDataSource;
    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    public DataSourceBasedMultiTenantConnectionProviderImpl(
            @Qualifier("tenantDefaultDataSource") DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
        dataSources.put("default", defaultDataSource);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return defaultDataSource;
    }

    public void addDataSource(String tenantId, DataSource dataSource) {
        dataSources.put(tenantId, dataSource);
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        log.debug("Selecting data source: {}", tenantId);
        if (!dataSources.containsKey(tenantId)) {
            throw new RuntimeException("Tenant " + tenantId + " not found");
        }
        return dataSources.get(tenantId);
    }

}
