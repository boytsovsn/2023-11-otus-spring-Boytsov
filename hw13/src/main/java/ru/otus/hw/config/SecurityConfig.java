package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

//    @Autowired
//    UserRepository userRepository;

    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( ( authorize ) -> authorize
                        .requestMatchers(  "/error", "/*.css", "/*.png", "/h2-console*", "/h2-console/**").permitAll()
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

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        var users = new ArrayList<UserDetails>();
//        List<ru.otus.hw.models.entities.User> usersDB = userRepository.findAll();
//
//        for (ru.otus.hw.models.entities.User user: usersDB) {
//            if (user.getLock() != true) {
//                users.add(User
//                        .builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole())
//                        .build());
//            }
//        }
//        return new InMemoryUserDetailsManager(users);
//    }
}
