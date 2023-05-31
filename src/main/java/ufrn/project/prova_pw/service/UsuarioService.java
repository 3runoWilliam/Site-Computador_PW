package ufrn.project.prova_pw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ufrn.project.prova_pw.model.Usuario;
import ufrn.project.prova_pw.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> user = repository.findByLogin(username);
        if (user.isPresent()){
            return user.get();
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public List<Usuario> listAll(){
        return  repository.findAll();
    }
}