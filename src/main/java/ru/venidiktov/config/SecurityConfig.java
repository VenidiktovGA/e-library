package ru.venidiktov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.venidiktov.enums.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    //Этот метод настраивает SpringSecurity авторизацию, ошибки, страницу Login
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//Отключаем csrf так как мы используем JWT token для аутентификации
                .authorizeHttpRequests(requests -> requests
//                                .requestMatchers("/admin").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                                .anyRequest().hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ADMIN.name())
//                        .anyRequest().authenticated() То же самое что и выше
                )
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Говорим springSecurity не создавать больше Сессий у нас JWT

        return http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build(); //Добавляем фильтр
    }

    @Bean
    @SuppressWarnings("deprecation")
    //Указываем каким алгоритмом шифруем пароль
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Тут есть метод который предоставляет SpringSecurity для проверки логина и пароля (так же как и на странице login по умолчанию)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
