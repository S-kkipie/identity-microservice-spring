package unsa.sistemas.identityservice.Config.MultiTenantImpl;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import unsa.sistemas.identityservice.Config.Context.OrgContext;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String orgCode = OrgContext.getOrgCode();
        return (orgCode != null) ? orgCode : "default";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}