package ufrn.project.prova_pw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig  implements WebMvcConfigurer {
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> { auth
                                                    .requestMatchers("/login","/", "/index").permitAll()
                                                    .requestMatchers("/admin", "/cadastrar", "/salvar", "/editar", "/deletar").hasRole("ADMIN")
                                                    .requestMatchers("/verCarrinho", "/adicionarCarrinho/{id}", "/finalizarCompra").hasRole("USER")
                                                    .anyRequest().authenticated();
                })
                .formLogin( login -> login.loginPage("/login").permitAll())
                .logout( logout -> logout.logoutUrl("/logout"))
                .build();
    }

    @Bean
    BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}