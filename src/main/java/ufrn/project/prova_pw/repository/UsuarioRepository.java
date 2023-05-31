package ufrn.project.prova_pw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufrn.project.prova_pw.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByLogin(String username);
}