package net.engineeringdigest.midwares;

import io.jsonwebtoken.Claims;
import net.engineeringdigest.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;

    public JwtFilter(JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. No token → continue (public routes allowed)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. Extract token
            String token = authHeader.substring(7);

            // 3. Validate token (checks expiry internally)
            Claims claims = jwtUtil.validateToken(token);

            String email = jwtUtil.getEmail(claims);
            String role = jwtUtil.getRole(claims);

            // 4. Create authentication object
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.emptyList() // roles later
                    );

            // 5. Set into SecurityContext (CRITICAL STEP)
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 6. Optional: attach claims to request (like req.user)
            request.setAttribute("uid", jwtUtil.getUid(claims));
            request.setAttribute("role", role);
            request.setAttribute("email", email);

        } catch (Exception e) {
            // Token invalid / expired
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}