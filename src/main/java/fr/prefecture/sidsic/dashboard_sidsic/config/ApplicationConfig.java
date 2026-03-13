package fr.prefecture.sidsic.dashboard_sidsic.config;

import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final MembreRepository membreRepository;

    public ApplicationConfig(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> membreRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Membre non trouvé avec l'email : " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}