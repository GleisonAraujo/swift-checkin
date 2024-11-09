package com.example.server.api.usuario;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)


import com.example.server.database.entity.Usuario;

@RestController // Define a classe como controller
@RequestMapping("/usuario/") // Definindo o caminho da API 
public class UsuarioController {

    // METODOS DE RESPOSTA HTTP
    // 204 (No Content) -- A busca retornou, porém nada
    // 200 (OK) --  A busca retornou algo
    // 404 (Not Found) --  Não foi possível encontrar
    // 201 (Created)  -- Informa que foi criado e beseado no body
    // 500 (internal erro server)
    // 400 (bad request)

    @Autowired // O Spring vai injetar o UsuarioServico aqui para 
    private UsuarioService usuarioServico;
    
    // 01 - Endpoint para criar um usuário
    // post create - cria um usuario e retorna o usuario criado
    @PostMapping("/usuario") // Define que será um método POST e o caminho da request
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        if (usuario.getNome() == null || usuario.getFullName() == null || usuario.getSenha() == null) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("Campos 'nome', 'fullName' e 'senha' são obrigatórios.");
        }
        // Chama o serviço para criar o usuário de forma assíncrona
        CompletableFuture<Usuario> novoUsuario = usuarioServico.criarUsuario(usuario);

        // Espera a resposta da operação assíncrona
        Usuario usuarioResult = novoUsuario.get();  // Aguarda o retorno

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResult);  // Retorna 201 (Created) com o novo usuário
    }

    // 02 - Endpoint para encontrar todos os usuários 
    // get all - retorna todo os usuarios
    @GetMapping("/usuarios") // Define que será um método GET e o caminho o caminho da request
    public ResponseEntity<List<Usuario>> buscaTodos() throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar todos os usuários de forma assíncrona
        CompletableFuture<List<Usuario>> usuarios = usuarioServico.buscaTodos();
        
        // Espera a resposta da operação assíncrona
        List<Usuario> listaUsuarios = usuarios.get();  // Aguarda o retorno

        if (listaUsuarios.isEmpty()) {
            return ResponseEntity.noContent().build();  // Retorna 204 (No Content) se não houver usuários
        }
        return ResponseEntity.ok(listaUsuarios);  // Retorna 200 (OK) com a lista de usuários
    }
    
    // 03 - Endpoint para encontrar um usuário específico
    // get find by id - retorna um usuario baseado no id
    @GetMapping("/{id}") // Define que será um método GET e que o caminho da request precisa de um parametro de busca
    public ResponseEntity<?> buscarUsuario(@PathVariable long id) throws InterruptedException, ExecutionException {
        if  (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuario = usuarioServico.buscarUsuario(id);
        
        // Espera a resposta da operação assíncrona
        Usuario usuarioResult = usuario.get();  // Aguarda o retorno

        if (usuarioResult != null) {
            return ResponseEntity.ok(usuarioResult);  // Retorna 200 (OK) com o usuário
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // Retorna bad request 400
        }
    }

    // 04 - Endpoint para atualizar um usuário
    // patch update by id - atualiza um usuario e retorna o que foi atualizado
    @PatchMapping("/usuario/{id}") // Define que será um método PATH e que o caminho da request precisa de um parametro de busca
    public ResponseEntity<Usuario> atualizaUsuario(@RequestBody Usuario usuario, @PathVariable long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para atualizar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuarioAtualizado = usuarioServico.atualizarUsuario(usuario, id);

        // Espera a resposta da operação assíncrona
        Usuario usuarioResult = usuarioAtualizado.get();  // Aguarda o retorno

        if (usuarioResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResult);  // Retorna 201 (Created) com o usuário atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 (Not Found) se não encontrar o usuário
        }
    }

    // 05 - Endpoint para deletar um usuário
    // delete by id - deleta um usuario beseado no id fornecido na request
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) throws InterruptedException, ExecutionException {
        if  (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Usuario> usuario = usuarioServico.buscarUsuario(id);
        // Resultado da busca armazenado no 'usuarioResult'
        Usuario usuarioResult = usuario.get();
        
        //válidação 
        if (usuarioResult == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Retorna bad request 400
        } else {
            CompletableFuture<Void> delete = usuarioServico.deleteUsuario(id);
            delete.get();
            return ResponseEntity.ok().body("Usuario deletado"); // Retorna ok 2000
        }
    }
    // 06 - Endpoint para buscar o usuário logado
    @GetMapping("/me/{usuarioId}")  // Adicionando endpoint para buscar o usuário logado
    public ResponseEntity<?> buscaMe(@PathVariable Long usuarioId) throws InterruptedException, ExecutionException {
        if (usuarioId <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");  // Valida o ID
        }
        // Chama o serviço para buscar o usuário logado (buscaMe)
        CompletableFuture<Usuario> usuario = usuarioServico.buscaMe(usuarioId);
        Usuario usuarioResult = usuario.get();  // Aguarda o retorno

        if (usuarioResult != null) {
            return ResponseEntity.ok(usuarioResult);  // Retorna 200 (OK) com o usuário
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");  // Retorna 404 (Not Found) se não encontrar o usuário
        }
    }
}