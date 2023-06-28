package ru.venidiktov.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.venidiktov.security.JWTUtil;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); //Получаем значение заголовка
        //Если токен не пуст и начинается с Bearer
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7); //Берем только токен без Bearer
            if (jwt.isBlank()) { //Если jwt пустой то выдаем ответ 400
                response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Invalid JWT token in Authorization Header");
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt); //Валидируем токен
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Ищем нашего пользователя в БД (у нас он в БД)
                    //Создаем новый объект аутентификационного токена
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities()
                            );
                    //Если в контексте нет Authentication мы его туда кладем
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
                            "Invalid JWT token");
                } catch (UsernameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //ПРОДВИГАЕМ ЗАПРОС ДАЛЬШЕ ПО ЦЕПОЧКЕ ФИЛЬТРОВ
        filterChain.doFilter(request, response);
    }
}
