package ufrn.project.prova_pw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ufrn.project.prova_pw.model.Usuario;
import ufrn.project.prova_pw.repository.ComputadorRepository;
import ufrn.project.prova_pw.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ProvaPwApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ProvaPwApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ComputadorRepository sapatoRepository, UsuarioRepository usuarioRepository, PasswordEncoder encoder){
        return args -> {
            List<Usuario> users = Stream.of(
                    new Usuario(1L, "Bruno", "Bruno", encoder.encode("1111"), true),
					new Usuario(2L, "Taniro", "Taniro", encoder.encode("2222"), false),
                    new Usuario(3L, "Tobias", "Tobias", encoder.encode("3333"), false)
            ).collect(Collectors.toList());
            usuarioRepository.saveAll(users);
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
    }
}