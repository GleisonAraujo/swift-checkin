package com.example.server.api.cliente;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.api.cliente.valid.ClienteValidation;
import com.example.server.database.entity.Cliente;

@RestController // Define a classe como controller
@RequestMapping("/cliente/") // Definindo o caminho da API 
public class ClienteController {

    @Autowired // O Spring vai injetar o ClienteService aqui para utilizar os métodos
    private ClienteService clienteService;

    // Endpoint 'POST' para criar cliente
    @PostMapping("/") 
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) throws InterruptedException, ExecutionException {
        // Validação do body
        String validacao = ClienteValidation.validar(cliente); 
        if (validacao != null) {
            return ResponseEntity.badRequest().body(validacao); // Status 400 caso de erro 
        }

        // Verifica se o cliente com o mesmo CPF já existe
        if (clienteService.usuarioExiste(cliente.getCpf())) { 
            return ResponseEntity.badRequest().body("Cpf de usuário já está cadastrado.");  // Status 400 caso de erro
        } 
        
        // Método assíncrono para criar o cliente
        CompletableFuture<Cliente> novoCliente = clienteService.criarCliente(cliente);

        Cliente result = novoCliente.get(); // Aguarda o retorno da operação anterior e armazena em result
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // Status 201 ao criar                                                                
    }

    // Endpoint 'GET' retorna um cliente de acordo com o Parametro 'id'
    @GetMapping("/{id}") 
    public ResponseEntity<?> buscaCliente(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o cliente de forma assíncrona
        CompletableFuture<Cliente> resultCliente = clienteService.buscarCliente(id);
        Cliente result = resultCliente.get(); // Aguarda o retorno da operação anterior e armazena em result
        if (result != null) {
            return ResponseEntity.ok(result); // Status 200 'ok' retornando todo os usuario existente
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // Status 204 'noContent' caso não encontre o usuario
        }
    }

    // Endpoint 'GET' retorna todos os clientes em uma lista
    @GetMapping("/") 
    public ResponseEntity<List<Cliente>> buscarTdsCliente() throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar todos os clientes de forma assíncrona
        CompletableFuture<List<Cliente>> allCliente = clienteService.todosClientes();
        List<Cliente> listaClientes = allCliente.get(); // Aguarda o retorno da operação anterior e armazena em listaClientes
        return ResponseEntity.ok(listaClientes); // Status 200 'ok' retornando todos os clientes
    }

    // Endpoint 'PATCH' atualiza um cliente de acordo com o Parametro 'id'
    @PatchMapping("/{id}") 
    public ResponseEntity<?> atualizaCliente(@RequestBody Cliente clienteAtualizado, @PathVariable Long id) throws InterruptedException, ExecutionException {
        String validacao = ClienteValidation.validar(clienteAtualizado); 
        if (validacao != null) {
            return ResponseEntity.badRequest().body(validacao); // Status 400 caso de erro 
        }
        // Chama o serviço para atualizar o cliente de forma assíncrona
        CompletableFuture<Cliente> cliente = clienteService.atualizarCliente(id, clienteAtualizado);
        
        Cliente clienteResult = cliente.get(); // Aguarda o retorno da operação anterior e armazena em clienteResult

        if (clienteResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteResult);  // Status 201 'created' retornando o cliente atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Status 404 'not found' caso não encontre o cliente
        }
    }
    
    // Endpoint 'DELETE' deleta um cliente de acordo com o Parametro 'id'
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o cliente de forma assíncrona
        CompletableFuture<Cliente> cliente = clienteService.buscarCliente(id);
        Cliente result = cliente.get(); // Aguarda o retorno da operação anterior e armazena em result
        if (result == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Status '400' caso não encontre o cliente
        } else {
            // Chama o serviço para deletar o cliente de forma assíncrona
            CompletableFuture<Void> deletar = clienteService.deletarCliente(id);
            deletar.get(); // Aguarda a exclusão do cliente
            return ResponseEntity.ok().body("Cliente deletado"); // Status '200' caso o cliente tenha sido deletado com sucesso
        }           
    }    
}
