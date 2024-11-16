package com.example.server.api.quarto;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.api.quarto.valid.QuartoReqValidation;
import com.example.server.api.quarto.valid.QuartoValidationUp;
import com.example.server.database.entity.Quarto;

@RestController // Define a classe como controller
@RequestMapping("/quarto/") // Definindo o caminho da API 
public class QuartoController {

    @Autowired // O Spring vai injetar o QuartoService aqui para utilizar os metodos
    private QuartoService quartoService;

    // Endpoint 'POST' para criar quarto
    @PostMapping("/")
    public ResponseEntity<?> criarQuarto(@RequestBody Quarto quarto) throws InterruptedException, ExecutionException {
        // Validação do body
        String validation = QuartoReqValidation.validar(quarto);
        if(validation != null) {
            return ResponseEntity.badRequest().body(validation); // Status 400 caso de erro 
        }

        // Verifica se o nuemro do quarto já existe
        if(quartoService.numeroExiste(quarto.getNumero())){
            return ResponseEntity.badRequest().body("Numero de quarto já existe."); // Status 400 caso de erro
        }
        
        // Metodo assincrono para criar o quarto
        CompletableFuture<Quarto> novoQuarto = quartoService.criarQuarto(quarto);
        Quarto result = novoQuarto.get(); // Aguarda o retorno da operação anterior e armazena em result
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // Status 201 ao criar    
    }

    // Endpoint 'GET' retorna um usuario de acordo com o Parametro 'id'
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaQuarto(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o usuário de forma assíncrona passando o id
        CompletableFuture<Quarto> result = quartoService.buscarQuartoById(id);
        Quarto quartoResult = result.get(); // Aguarda o retorno da operação anterior e armazena em 'result'
        if (quartoResult != null) {
            return ResponseEntity.ok(quartoResult); // Status '200' encontrado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quarto não encontrado"); // Status '400' caso não encontre
        }
    }

    // Endpoint 'GET' retorna todos os quartos em uma lista
    @GetMapping("/")
    public ResponseEntity<List<Quarto>> buscarTdsQuarto() throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar todos os usuários de forma assíncrona
        CompletableFuture<List<Quarto>> allQuartos = quartoService.buscarTdsQuarto();
        List<Quarto> result = allQuartos.get();  // Aguarda o retorno da operação anterior e armazena em result
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // Status 204 'noContent' caso não tenha nenhuma quarto
        }
        return ResponseEntity.ok(result); // Status 200 'ok' retornando todo os quartos existentes
    }

    // Endpoint 'PATCH' atualiza um quarto de acordo com o Parametro 'id'
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizaQuarto(@RequestBody Quarto quartoAtualizado, @PathVariable Long id) throws InterruptedException, ExecutionException {
        // Validação do body de acordo com o que foi passado
        String validation = QuartoValidationUp.validar(quartoAtualizado);
        if(validation != null) {
            return ResponseEntity.badRequest().body(validation); // Status 400 caso de erro 
        }
        // Verifica se o quarto com o mesmo nuemro já existe
        if(quartoService.numeroExiste(quartoAtualizado.getNumero())){
            return ResponseEntity.badRequest().body("Numero de quarto já existe."); // Status 400 caso de erro
        }
        // Chama o serviço para atualizar o usuário de forma assíncrona
        CompletableFuture<Quarto> quarto = quartoService.atualizarQuarto(id, quartoAtualizado);
        Quarto quartoResult = quarto.get();
        if (quartoResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(quartoResult);  // Status 201 'created' atualizado e retorna o quarto
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Status 404 'not found' não encontrou o quarto
        }
    }

    // Endpoint 'DELETE' deleta um usuario de acordo com o Parametro 'id'
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarQuarto(@PathVariable Long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Quarto> quarto = quartoService.buscarQuartoById(id);
        Quarto result = quarto.get(); // Aguarda o retorno da operação anterior e armazena em 'result'
        if (result == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Status '400' caso não encontre
        } else {
            CompletableFuture<Void> deletar = quartoService.deletarQuarto(id);
            deletar.get();
            return ResponseEntity.ok().body("Quarto deletado"); // Status '200' deletado
        }
    }
}
