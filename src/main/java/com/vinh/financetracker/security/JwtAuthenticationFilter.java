package com.vinh.financetracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Để Spring quản lý Filter này như một Bean
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Kiểm tra Header Authorization có hợp lệ không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Cho đi tiếp (nhưng không có quyền)
            return;
        }

        // 2. Trích xuất Token (bỏ chữ "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Lấy Email từ Token thông qua JwtService
        userEmail = jwtService.extractUsername(jwt);

        // 4. Nếu có Email và User chưa được xác thực trong SecurityContext
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Lấy thông tin User từ Database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Nếu Token vẫn hợp lệ (đúng User và chưa hết hạn)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Tạo đối tượng Authentication để báo cho Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // QUAN TRỌNG: Lưu thông tin User vào Context của Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Cho phép Request đi tiếp tới Controller
        filterChain.doFilter(request, response);
    }
}
