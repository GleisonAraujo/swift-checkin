package com.example.server.api.quarto;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.database.entity.Quarto;

@RestController // Define a classe como controller
@RequestMapping("/quarto/") // Definindo o caminho da API 
public class QuartoController {

    @Autowired // O Spring vai injetar o QuartoService aqui para 
    private QuartoService quartoService;

    // Post: Criar quarto
    @PostMapping("/")
    public ResponseEntity<?> criarQuarto(@RequestBody Quarto quarto) throws InterruptedException, ExecutionException {
        if (quarto.getNumero() == null || 
            quarto.getDescricao() == null || 
            quarto.getTipo() == null || 
            quarto.getPrDiaria() == null ||
            quarto.getDisponibilidade() == null) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("Campos 'numero', 'descricao', 'tipo', 'disponibilidade' e 'valorDiaria' são obrigatórios.");
        }
        CompletableFuture<Quarto> novoQuarto = quartoService.criarQuarto(quarto);

        Quarto result = novoQuarto.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Get: Buscar quarto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaQuarto(@PathVariable Long id) throws InterruptedException, ExecutionException {
        CompletableFuture<Quarto> resultQuarto = quartoService.buscarQuartoById(id);
        Quarto quartoResult = resultQuarto.get();
        if (quartoResult != null) {
            return ResponseEntity.ok(quartoResult);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 (Not Found) se não encontrar o quarto
        }
    }

    // Get: Buscar todos os quartos
    @GetMapping("/")
    public ResponseEntity<List<Quarto>> buscarTdsQuarto() throws InterruptedException, ExecutionException {
        CompletableFuture<List<Quarto>> allQuartos = quartoService.buscarTdsQuarto();
        List<Quarto> listaQuartos = allQuartos.get(); 
        return ResponseEntity.ok(listaQuartos);
    }

    // Patch: Atualizar quarto por ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizaQuarto(@RequestBody Quarto quartoAtualizado, @PathVariable Long id) throws InterruptedException, ExecutionException {
        CompletableFuture<Quarto> quarto = quartoService.atualizarQuarto(id, quartoAtualizado);
        
        Quarto quartoResult = quarto.get();
        
        if (quartoResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(quartoResult);  // Retorna 201 (Created) com o quarto atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 (Not Found) se não encontrar o quarto
        }
    }

    // Delete: Deletar quarto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarQuarto(@PathVariable Long id) throws InterruptedException, ExecutionException {
        CompletableFuture<Quarto> quarto = quartoService.buscarQuartoById(id);
        Quarto result = quarto.get();
        if (result == null) {
            return ResponseEntity.badRequest().body("ID não encontrado");
        } else {
            CompletableFuture<Void> deletar = quartoService.deletarQuarto(id);
            deletar.get();
            return ResponseEntity.ok().body("Quarto deletado"); // Retorna ok 200
        }
    }
}
