package com.example.server.api.cliente;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.database.entity.Cliente;

@RestController // Define a classe como controller
@RequestMapping("/cliente/") // Definindo o caminho da API 
public class ClienteController {

    @Autowired // O Spring vai injetar o UsuarioServico aqui para 
    private ClienteService clienteService;

    //Rotas
    //Post
    @PostMapping("/")
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) throws InterruptedException, ExecutionException{
        if (cliente.getNome() == null || 
            cliente.getTelefone() == null || 
            cliente.getEmail() == null || 
            cliente.getDataNascimento() == null || 
            cliente.getCpf() == null) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("Campos 'nome', 'telefone', 'email', 'DataNascimento', e 'cpf' são obrigatórios.");
        }
        CompletableFuture<Cliente> novoCliente = clienteService.criarCliente(cliente);

        Cliente result = novoCliente.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    //Get by Id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaCliente(@PathVariable Long id) throws InterruptedException, ExecutionException {
        CompletableFuture<Cliente> resultCliente = clienteService.buscarCliente(id);
        Cliente clienteResult = resultCliente.get();
        return ResponseEntity.ok(clienteResult);
        // falta tratar mais a excessões
    }

    //Get all
    @GetMapping("/")
    public ResponseEntity<List<Cliente>> buscarTdsCliente() throws InterruptedException, ExecutionException {
        CompletableFuture<List<Cliente>> allCliente = clienteService.todosClientes();
        List<Cliente> listaClientes = allCliente.get(); 
        return ResponseEntity.ok(listaClientes);
        
    }
    //Patch
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizaCliente(@RequestBody Cliente clienteAtualizado, @PathVariable Long id) throws InterruptedException, ExecutionException{
        CompletableFuture<Cliente> cliente = clienteService.atualizarCliente(id, clienteAtualizado);
        
        Cliente clienteResult = cliente.get();
     
        if (clienteResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteResult);  // Retorna 201 (Created) com o usuário atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 (Not Found) se não encontrar o usuário
        }
    }
    
    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) throws InterruptedException, ExecutionException {
        CompletableFuture<Cliente> cliente = clienteService.buscarCliente(id);
        Cliente result = cliente.get();
        if(result ==  null) {
            return ResponseEntity.badRequest().body("ID não encontrado");
        }else{
            CompletableFuture<Void> deletar = clienteService.deletarCliente(id);
            deletar.get();
            return ResponseEntity.ok().body("Usuario deletado"); // Retorna ok 2000
        }           
    }    
}
