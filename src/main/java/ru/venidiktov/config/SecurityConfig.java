package ru.venidiktov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.venidiktov.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    //Этот метод настраивает SpringSecurity авторизацию, ошибки, страницу Login
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers("/admin").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                                .anyRequest().hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ADMIN.name())
//                        .anyRequest().authenticated() То же самое что и выше
                )
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");

        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    //Указываем каким алгоритмом шифруем пароль (Пока его не шифруем)
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
