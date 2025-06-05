package unsa.sistemas.identityservice.Security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import unsa.sistemas.identityservice.Config.TenantContext;

@Service
@AllArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader("X-Tenant-Id");

        //TODO implement tenantService
        boolean hasAccess = true;

        if (tenantId != null && !tenantId.isBlank() && hasAccess) {
            TenantContext.setTenantId(tenantId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}