package com.example.InternshipMileStone.config;
import com.example.InternshipMileStone.filters.JwtFilter;
import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationProvider authProvider() {

        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        //allow all to access auth , allow admin and employee to access what their roles specify , else authenticate
        http.csrf(AbstractHttpConfigurer::disable);//disable csrf
        http.authorizeHttpRequests(request-> request

                .requestMatchers("/auth/**","/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
//        http.formLogin(Customizer.withDefaults()); if you want a form
//        http.httpBasic(Customizer.withDefaults());//use basic http auth

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;//make the authentication stateless (each time you give a request you get a new session)

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config)
    {
        return config.getAuthenticationManager();
    }

}
