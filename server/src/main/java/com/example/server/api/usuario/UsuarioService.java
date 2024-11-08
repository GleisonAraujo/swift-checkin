package com.example.server.api.usuario;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture; // Importe para funções assincronas

import org.springframework.beans.factory.annotation.Autowired; // Importado Autowired para pode injetar o UsuarioRepository
import org.springframework.scheduling.annotation.Async; // Marcar as funções como assincronas
import org.springframework.stereotype.Service;

import com.example.server.database.entity.Usuario;
import com.example.server.database.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Async
    public CompletableFuture<Usuario> criarUsuario(Usuario usuario) {
        Usuario novoUsuario = usuarioRepository.save(usuario);
        return CompletableFuture.completedFuture(novoUsuario);
    }

    @Async
    public CompletableFuture<Usuario> buscarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.isPresent() ? CompletableFuture.completedFuture(usuario.get()) :
                                     CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Usuario>> buscaTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return CompletableFuture.completedFuture(usuarios);
    }


    @Async
    public CompletableFuture<Usuario> atualizarUsuario(Usuario usuario, Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuarioAtualizado = usuarioExistente.get();
            if (usuario.getNome() != null) usuarioAtualizado.setNome(usuario.getNome());
            if (usuario.getFullName() != null) usuarioAtualizado.setFullName(usuario.getFullName());
            if (usuario.getSenha() != null) usuarioAtualizado.setSenha(usuario.getSenha());
            usuarioRepository.save(usuarioAtualizado);
            return CompletableFuture.completedFuture(usuarioAtualizado);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
