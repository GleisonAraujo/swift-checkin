package com.example.server.api.auth;

import java.util.Optional;
import java.util.concurrent.CompletableFuture; // Importe para funções assincronas

import org.springframework.beans.factory.annotation.Autowired; // Importado Autowired para pode injetar o UsuarioRepository
import org.springframework.stereotype.Service; // Indica que a classe é um componente de lógica de negócios que deve ser gerenciado pelo contêiner do Spring
import org.springframework.scheduling.annotation.Async; // Marcar as funções como assincronas

import com.example.server.database.entity.Usuario;
import com.example.server.database.repository.UsuarioRepository; // Importado do UsuarioRepository

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Async
    public CompletableFuture<Usuario> login(String nomeUsuario, String senha){
        Optional<Usuario> usuarioBusca = usuarioRepository.findByNome(nomeUsuario);
        if (usuarioBusca.isPresent()) {
            Usuario usuario = usuarioBusca.get();
            // Verificando a senha (isso seria ajustado de acordo com a lógica de autenticação)
            if (usuario.getSenha().equals(senha)) {
                return CompletableFuture.completedFuture(usuario);  // Se a senha for correta, retorna o usuário
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
