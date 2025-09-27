package com.productivity_suite.LifeCanvas.Utils;

import com.productivity_suite.LifeCanvas.Services.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private AppUserDetailsService userDetails;

    @Autowired
    private JWTUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/v1/login",
            "/api/auth/v1/signup",
            "/api/auth/v1/send-reset-otp",
            "/api/auth/v1/reset-password",
            "/api/auth/v1/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if(PUBLIC_URLS.contains(path)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt = null;
        String email = null;


        // 1. Check Authorization Header
        final String authorizeHeader = request.getHeader("Authorization");

        if(authorizeHeader != null && authorizeHeader.startsWith("Bearer ")){
            jwt = authorizeHeader.substring(7);
        }

        //2. If not found in Header, search Cookie

        if(jwt == null){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie: cookies){
                    if("jwt".equals(cookie.getName())){
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 3. Validate the Token and set the Security Context

        if(jwt != null){
            email = jwtUtil.extractEmail(jwt);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails user = userDetails.loadUserByUsername(email);

                if(jwtUtil.validateToken(jwt, user)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
        }
        filterChain.doFilter(request,response);

    }
}
