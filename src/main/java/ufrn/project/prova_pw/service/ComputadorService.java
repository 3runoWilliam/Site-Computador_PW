package ufrn.project.prova_pw.service;

import org.springframework.stereotype.Service;

import ufrn.project.prova_pw.model.Computador;
import ufrn.project.prova_pw.repository.ComputadorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ComputadorService {

    private final ComputadorRepository repository;

    public ComputadorService(ComputadorRepository repository) {
        this.repository = repository;
    }

    public Computador create(Computador c){
        return repository.save(c);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public Computador update(Computador c){
        return repository.saveAndFlush(c);
    }

    public List<Computador> findByDeletedIsNull(){
        return repository.findByDeletedIsNull();
    }

    public Computador findById(Long id){
        Optional<Computador> ComputadorOptional = repository.findById(id);
        return ComputadorOptional.orElse(null);
    }

    public List<Computador> findAll(){
        return repository.findAll();
    }
}