package com.example.server.api.usuario;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture; // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.api.usuario.valid.UsuarioValidator;
import com.example.server.api.usuario.valid.UsuarioValidatorUp;
import com.example.server.database.entity.Usuario;

@RestController // Define a classe como controller
@RequestMapping("/usuario/") // Definindo o caminho da API
public class UsuarioController {

    @Autowired // O Spring vai injetar o UsuarioServico aqui para utilizar os metodos
    private UsuarioService usuarioServico;

    // Endpoint 'POST' para criar usuario
    @PostMapping("/") 
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        // Validação do body
        String validacao = UsuarioValidator.validarUsuario(usuario); 
        if (validacao != null) {
            return ResponseEntity.badRequest().body(validacao); // Status 400 caso de erro 
        }
        
        // Verifica se o usuario com o mesmo nome já existe
        if (usuarioServico.usuarioExiste(usuario.getNome())) { 
            return ResponseEntity.badRequest().body("Nome de usuário já existe.");  // Status 400 caso de erro
        }                                
        
        // Metodo assincrono para criar o usuario
        CompletableFuture<Usuario> novoUsuario = usuarioServico.criarUsuario(usuario); 
        Usuario usuarioResult = novoUsuario.get(); // Aguarda o retorno da operação anterior e armazena em usuarioResult
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResult); // Status 201 ao criar                                                                
    }
    
    // Endpoint 'GET' retorna um usuario de acordo com o Parametro 'id'
    @GetMapping("/{id}") 
    public ResponseEntity<?> buscarUsuario(@PathVariable long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuario = usuarioServico.buscarUsuario(id);
        Usuario result = usuario.get(); // Aguarda o retorno da operação anterior e armazena em result
        if (result != null) {
            return ResponseEntity.ok(result); // Status 200 'ok' retornando todo os usuario existente
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // Status 204 'noContent' caso não encontre o usuario
        }
    }
    
    // Endpoint 'GET' busca um usuario de acordo com o Parametro 'id'
    @GetMapping("/me/{id}") // Adicionando endpoint para buscar o usuário logado
    public ResponseEntity<?> buscaMe(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o usuário de forma assíncrona passando o id
        CompletableFuture<Usuario> usuario = usuarioServico.buscaMe(id);
        Usuario result = usuario.get(); // Aguarda o retorno da operação anterior e armazena em 'result'
        if (result != null) {
            return ResponseEntity.ok(result); // Status '200' encontrado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"); // Status '400' caso não encontre
        }
    }
    
    // Endpoint 'GET' retorna todos os usuarios em uma lista
    @GetMapping("/") 
    public ResponseEntity<List<Usuario>> buscaTodos() throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar todos os usuários de forma assíncrona
        CompletableFuture<List<Usuario>> usuarios = usuarioServico.buscaTodos();
        List<Usuario> result = usuarios.get(); // Aguarda o retorno da operação anterior e armazena em result
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // Status 204 'noContent' caso não tenha nenhuma usuario
        }
        return ResponseEntity.ok(result); // Status 200 'ok' retornando todo os usuarios existentes
    }

    // Endpoint 'PATCH' atualiza um usuario de acordo com o Parametro 'id'
    @PatchMapping("/{id}") 
    public ResponseEntity<?> atualizaUsuario(@RequestBody Usuario usuario, @PathVariable long id) throws InterruptedException, ExecutionException {
        // Validação do body de acordo com o que foi passado
        String validacao = UsuarioValidatorUp.validarUsuario(usuario);
        if (validacao != null) {
            return ResponseEntity.badRequest().body(validacao); // Status 400 caso de erro 
        }
        // Verifica se o usuario com o mesmo nome já existe
        if (usuarioServico.usuarioExiste(usuario.getNome())) {
            return ResponseEntity.badRequest().body("Nome de usuário já existe.");  // Status 400 caso de erro
        }
        // Chama o serviço para atualizar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuarioAtualizado = usuarioServico.atualizarUsuario(usuario, id);
        Usuario result = usuarioAtualizado.get(); 
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result); // Status 201 'created' atualizado e retorna o usuario
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Status 404 'not found' não encontrou o usuario para att
        }
    }

    // Endpoint 'DELETE' deleta um usuario de acordo com o Parametro 'id'
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuario = usuarioServico.buscarUsuario(id);
        Usuario result = usuario.get(); // Aguarda o retorno da operação anterior e armazena em 'result'
        if (result == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Status '400' caso não encontre
        } else {
            CompletableFuture<Void> delete = usuarioServico.deleteUsuario(id);
            delete.get();
            return ResponseEntity.ok().body("Usuario deletado"); // Status '200' deletado
        }
    }

}