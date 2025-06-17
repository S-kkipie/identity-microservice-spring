package unsa.sistemas.identityservice.Security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import unsa.sistemas.identityservice.Config.MultiTenantImpl.DataSourceBasedMultiTenantConnectionProviderImpl;
import unsa.sistemas.identityservice.Config.Context.OrgContext;

@Slf4j
@Service
@AllArgsConstructor
public class TenantFilter extends OncePerRequestFilter {
    DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProviderImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String orgCode = request.getHeader("X-Org-Code");

        if (orgCode != null && !orgCode.isBlank()) {
            orgCode = orgCode.trim();
            log.debug("Request Org Code : {}", orgCode);

            if (!dataSourceBasedMultiTenantConnectionProviderImpl.getDataSources().containsKey(orgCode)) {
                log.debug("Org Code {} is not registered in the system", orgCode);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid  Org Code");
                return;
            }

            OrgContext.setOrgCode(orgCode);

        } else {
            log.debug("X-Org-Code header is missing");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            OrgContext.clear();
        }
    }
}