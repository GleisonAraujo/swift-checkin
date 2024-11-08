package com.example.server.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.server.database.entity.Usuario;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService autorizaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        // Chama o método assíncrono de login
        CompletableFuture<Usuario> usuarioAsync = autorizaService.login(usuario.getNome(), usuario.getSenha());

        // Aguarda o resultado (isso pode ser feito de forma mais elegante dependendo do seu caso de uso)
        Usuario usuarioResult = usuarioAsync.get();

        // Se o usuário foi encontrado e a senha conferiu
        if (usuarioResult != null) {
            return ResponseEntity.ok(usuarioResult);  // Retorna o usuário com status 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");  // Retorna 401 Unauthorized
        }
    }
}
