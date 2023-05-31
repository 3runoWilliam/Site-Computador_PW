package ufrn.project.prova_pw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.project.prova_pw.model.Computador;

public interface ComputadorRepository extends JpaRepository<Computador, Long> {
    List<Computador> findByDeletedIsNull();
}
