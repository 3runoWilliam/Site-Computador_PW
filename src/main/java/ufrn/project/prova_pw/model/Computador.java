package ufrn.project.prova_pw.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Computador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String modelo;
    String marca;
    String preco;
    String imagem;
    String descricao;
    Date deleted;
}