package com.example.server.api.servicoquarto;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.api.reserva.ReservaService;
import com.example.server.database.entity.Reserva;
import com.example.server.database.entity.ServicoQuarto;  // Importa a entidade Usuario do pacote de entidades do banco de dados



@RestController // Define a classe como controller
@RequestMapping("/servicoquarto/") // Definindo o caminho da API
public class ServicoQuartoController {

    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private ServicoQuartoService servicoQuartoService;
    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private ReservaService reservaService;

    // POST criarReserva
    @PostMapping("/")
    public ResponseEntity<?> criarServicoQuarto(@RequestBody ServicoQuarto novaServicoQuarto) {
        // Validar dados da nova reserva 
        //ReservaId - getNome() - getPreco() - getTipoServico() - getDataSolicitacao()
        if (novaServicoQuarto.getNome() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome não pode ser nulo");
        }
        if (novaServicoQuarto.getPreco() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preco não pode ser nulo");
        }
        if (novaServicoQuarto.getTipoServico() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TipoServico não pode ser nulo");
        }
        if (novaServicoQuarto.getDataSolicitacao() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data não pode ser nula");
        }


        // Obter os IDs das entidades relacionadas
        Long reservaId = novaServicoQuarto.getReserva().getId();


        // Consultar as entidades em paralelo
        CompletableFuture<Reserva> verificaReservaId= reservaService.buscaReservById(reservaId);

        // Esperar as consultas completarem
        CompletableFuture.allOf(verificaReservaId).join();

        // Verificar se as entidades existem
        Reserva reserva = verificaReservaId.join();

        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva não encontrada");
        }
        
        // Associar as entidades encontradas à nova reserva
        novaServicoQuarto.setReserva(reserva);

        // Criar a reserva no banco
        servicoQuartoService.criarServicoQuarto(novaServicoQuarto);

        // Retornar a nova reserva criada
        return ResponseEntity.ok(novaServicoQuarto);
    }

    // GET ALL
    @GetMapping("/")
    public ResponseEntity<?> buscarTdsReserva() throws InterruptedException, ExecutionException {
        CompletableFuture<List<ServicoQuarto>> tdsServicoQuarto = servicoQuartoService.BuscaTdsQuarto();
        List<ServicoQuarto> resultServicoQuarto = tdsServicoQuarto.get();
        
        if (resultServicoQuarto.isEmpty()) {
            return ResponseEntity.noContent().build();  // Retorna 204 (No Content) se não houver usuários
        }
        return ResponseEntity.ok(resultServicoQuarto);
    }


    // GET BY ID 
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaServicoQuartoById(@PathVariable long id) throws InterruptedException, ExecutionException {
        if  (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<ServicoQuarto> servico = servicoQuartoService.buscaByIdQuarto(id);
        
        // Espera a resposta da operação assíncrona
        ServicoQuarto servicoResult = servico.get();  // Aguarda o retorno

        if (servicoResult != null) {
            return ResponseEntity.ok(servicoResult);  // Retorna 200 (OK) com o usuário
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // Retorna bad request 400
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServicoQuarto> atualizarReservaById(@RequestBody ServicoQuarto novaServicoQuarto, @PathVariable long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para atualizar o usuário de forma assíncrona
        CompletableFuture<ServicoQuarto> servicoQuarto = servicoQuartoService.atualizarServicoQuartoById(novaServicoQuarto, id);

        // Espera a resposta da operação assíncrona
        ServicoQuarto servicoQuartoResult = servicoQuarto.get();  // Aguarda o retorno

        if (servicoQuartoResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(servicoQuartoResult);  // Retorna 201 (Created) com o usuário atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 (Not Found) se não encontrar o usuário
        }
    }

    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarServicoQuartoId(@PathVariable Long id) throws InterruptedException, ExecutionException {
        if  (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<ServicoQuarto> servico = servicoQuartoService.buscaByIdQuarto(id);
        // Resultado da busca armazenado no 'usuarioResult'
        ServicoQuarto servicoResult = servico.get();
        
        //válidação 
        if (servicoResult == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Retorna bad request 400
        } else {
            CompletableFuture<Void> delete = servicoQuartoService.deletarQuarto(id);
            delete.get();
            return ResponseEntity.ok().body("Usuario deletado"); // Retorna ok 2000
        }
    }
}