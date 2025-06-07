package gitoli.java.projects.com.rcs_visits_ms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This filter runs once per request to:
 * - Read and validate JWT from Authorization header
 * - Set authentication in SecurityContext if valid
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Reject if no Authorization header or if it doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);  // Remove "Bearer " prefix

        try {
            final String username = jwtService.extractUsername(jwt);

            // 2. If username exists and user not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Extract roles from the token
                List<String> roles = jwtService.extractRoles(jwt);

                // Convert roles to Spring Security authorities
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefix "ROLE_"
                        .collect(Collectors.toList());

                // 3. Create auth token (without password) and set in context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Authenticated user: {} with roles: {}", username, roles);
            }
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
            SecurityContextHolder.clearContext(); // Clear context if error
        }

        // 4. Continue down the filter chain
        chain.doFilter(request, response);
    }
}
