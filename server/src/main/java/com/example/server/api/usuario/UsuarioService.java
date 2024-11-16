package com.example.server.api.usuario;

import java.util.List;  // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;  // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired;  // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async;  // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service;  // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.Usuario;  // Importa a entidade Usuario do pacote de entidades do banco de dados
import com.example.server.database.repository.UsuarioRepository;  // Importa o repositório de Usuario para acessar dados no banco de dados

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
    public boolean usuarioExiste(String nome) {
        return usuarioRepository.existsByNome(nome);  
    }

    @Async
    public CompletableFuture<Usuario> buscarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.isPresent() ? CompletableFuture.completedFuture(usuario.get()) :
        CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Usuario> buscaMe(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
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
