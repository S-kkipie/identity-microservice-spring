package unsa.sistemas.identityservice.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import unsa.sistemas.identityservice.Config.Context.OrgContext;
import unsa.sistemas.identityservice.Services.ComposeUserDetailService;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class UserAuthFilter extends OncePerRequestFilter {
    private final ComposeUserDetailService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String username = request.getHeader("X-User-Name");
        String role = request.getHeader("X-User-Role");

        if (username != null && !username.isBlank() && role != null && !role.isBlank()) {
            username = username.trim();
            role = role.trim();
            log.debug("Request User ID: {}, Role: {}", username, role);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);


        } else {
            log.debug("X-User-Id or X-User-Role header is missing");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            OrgContext.clear();
        }
    }
}
