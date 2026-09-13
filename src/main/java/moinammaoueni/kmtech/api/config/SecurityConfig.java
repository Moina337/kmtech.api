
package moinammaoueni.kmtech.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CustomUserDetailsService;
import moinammaoueni.kmtech.api.auth.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Encodage des mots de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provider utilisé par AuthenticationManager
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    /**
     * AuthenticationManager utilisé lors du login
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configuration principale de Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider)
            throws Exception {

        http

            // API JWT = pas de session HTTP
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // Provider pour l'authentification email/password
            .authenticationProvider(authenticationProvider)

            // Gestion des autorisations
            .authorizeHttpRequests(auth -> auth

                    // =========================
                    // AUTHENTIFICATION PUBLIQUE
                    // =========================
                    .requestMatchers(
                            "/api/auth/**"
                    ).permitAll()

                    // =========================
                    // SWAGGER / OPENAPI
                    // =========================
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**"
                    ).permitAll()

                    // =========================
                    // ADMIN
                    // =========================
                    .requestMatchers(
                            "/api/admin/**"
                    ).authenticated()

                    // =========================
                    // UTILISATEUR CONNECTÉ
                    // =========================
                    .requestMatchers(
                            "/api/users/me"
                    ).authenticated()
                    .requestMatchers("/api/media/**").permitAll() // TODO: à sécuriser plus tard

                    // =========================
                    // CONSULTATION PUBLIQUE
                    // =========================
                    .requestMatchers(
                            "/api/users/{slug}",
                            "/api/organizations/**",
                            "/api/projects/**",
                            "/api/applications/**",
                            "/api/opportunities/**"
                    ).permitAll()

                    // =========================
                    // TOUT LE RESTE
                    // =========================
                    .anyRequest().authenticated()
            )

            // JWT avant le filtre Username/Password
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
