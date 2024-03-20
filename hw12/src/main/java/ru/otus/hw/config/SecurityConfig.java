package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( ( authorize ) -> authorize
                        .requestMatchers(  "/error", "/*.css", "/*.png").permitAll()
                        .requestMatchers( "/" ).authenticated()
                        .requestMatchers( "/list", "/book*" ).hasAnyRole("ADMIN", "USER")
                        .requestMatchers( "/book/*" ).hasAnyRole("ADMIN")
                  )
                .formLogin(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var users = new ArrayList<UserDetails>();
        List<ru.otus.hw.models.entities.User> usersDB = userRepository.findAll();

        for (ru.otus.hw.models.entities.User user: usersDB) {
            if (user.getLock() != true) {
                users.add(User
                        .builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole())
                        .build());
            }
        }
        return new InMemoryUserDetailsManager(users);
    }
}
