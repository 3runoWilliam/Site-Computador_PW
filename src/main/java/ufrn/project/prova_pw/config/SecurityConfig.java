package ufrn.project.prova_pw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> { auth
                                                    .requestMatchers("/login","/", "/index").permitAll()
                                                    .requestMatchers("/admin", "/cadastrar", "/salvar", "/editar/**", "/deletar").hasRole("ADMIN")
                                                    .requestMatchers("/verCarrinho", "/adicionarCarrinho/{id}", "/finalizarCompra").hasRole("USER")
                                                    .anyRequest().authenticated();
                })
                .formLogin((form) -> form
                    .defaultSuccessUrl("/", true)
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}