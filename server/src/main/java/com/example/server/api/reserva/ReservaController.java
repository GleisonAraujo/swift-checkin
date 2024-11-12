package com.example.server.api.reserva;

import java.sql.Date;
import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture; // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.database.entity.Cliente;
import com.example.server.database.entity.Quarto;
import com.example.server.database.entity.Reserva;
import com.example.server.database.entity.Usuario;

import com.example.server.api.usuario.UsuarioService;
import com.example.server.api.quarto.QuartoService;
import com.example.server.api.cliente.ClienteService;

@RestController // Define a classe como controller
@RequestMapping("/reserva/") // Definindo o caminho da API
public class ReservaController {

    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private ReservaService reservaServico;
    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private UsuarioService usuarioServico;
    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private QuartoService quartoService;
    @Autowired // O Spring vai injetar o UsuarioServico aqui para
    private ClienteService clienteService;

    // Metodos

    // POST criarReserva
    @PostMapping("/")
    public ResponseEntity<?> criarReserva(@RequestBody Reserva novaReserva) {
        // Validar dados da nova reserva
        if (novaReserva.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status da reserva não pode ser nulo");
        }
        if (novaReserva.getQtdDiaria() == null || novaReserva.getQtdDiaria() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade de diárias inválida");
        }
        if (novaReserva.getDataReserva() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data da reserva não pode ser nula");
        }

        // Obter os IDs das entidades relacionadas
        Long usuarioId = novaReserva.getUsuario().getId();
        Long quartoId = novaReserva.getQuarto().getId();
        Long clienteId = novaReserva.getCliente().getId();

        // Consultar as entidades em paralelo
        CompletableFuture<Usuario> usuarioFuture = usuarioServico.buscarUsuario(usuarioId);
        CompletableFuture<Quarto> quartoFuture = quartoService.buscarQuartoById(quartoId);
        CompletableFuture<Cliente> clienteFuture = clienteService.buscarCliente(clienteId);

        // Esperar as consultas completarem
        CompletableFuture.allOf(usuarioFuture, quartoFuture, clienteFuture).join();

        // Verificar se as entidades existem
        Usuario usuario = usuarioFuture.join();
        Quarto quarto = quartoFuture.join();
        Cliente cliente = clienteFuture.join();

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        if (quarto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quarto não encontrado");
        }
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        // Associar as entidades encontradas à nova reserva
        novaReserva.setUsuario(usuario);
        novaReserva.setQuarto(quarto);
        novaReserva.setCliente(cliente);

        // Criar a reserva no banco
        reservaServico.criarReserva(novaReserva);

        // Retornar a nova reserva criada
        return ResponseEntity.ok(novaReserva);
    }

    // GET ALL
    @GetMapping("/")
    public ResponseEntity<?> buscarTdsReserva() throws InterruptedException, ExecutionException {
        CompletableFuture<List<Reserva>> tdsReserva = reservaServico.buscarTdsReserva();
        List<Reserva> result = tdsReserva.get();

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 (No Content) se não houver usuários
        }
        return ResponseEntity.ok(result);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaReservById(@PathVariable long id) throws InterruptedException, ExecutionException {
        if (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);

        // Espera a resposta da operação assíncrona
        Reserva result = reserva.get(); // Aguarda o retorno

        if (result != null) {
            return ResponseEntity.ok(result); // Retorna 200 (OK) com o usuário
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // Retorna bad request 400
        }
    }

    // PATCH BY ID
    @PatchMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReservaById(@RequestBody Reserva reserva, @PathVariable long id)
            throws InterruptedException, ExecutionException {
        // Chama o serviço para atualizar o usuário de forma assíncrona
        CompletableFuture<Reserva> reservaAtualizado = reservaServico.atualizarReservaById(reserva, id);

        // Espera a resposta da operação assíncrona
        Reserva reservaResult = reservaAtualizado.get(); // Aguarda o retorno

        if (reservaResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // Retorna 201 (Created) com o usuário
                                                                                  // atualizado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 (Not Found) se não encontrar o
                                                                        // usuário
        }
    }

    // PATCH para registrar dataCheckIn
    @PatchMapping("/checkin/{id}")
    public ResponseEntity<Reserva> registrarDataCheckIn(
            @PathVariable long id,
            @RequestBody Date dataCheckIn) throws InterruptedException, ExecutionException {

        CompletableFuture<Reserva> reservaAtualizada = reservaServico.registrarDataCheckIn(id, dataCheckIn);
        Reserva reservaResult = reservaAtualizada.get();

        if (reservaResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // Retorna 201 (Created) com a reserva
                                                                                  // atualizada
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 (Not Found) se a reserva não for
                                                                        // encontrada
        }
    }

    // PATCH para registrar dataCheckOut e definir status como false
    @PatchMapping("/checkout/{id}")
    public ResponseEntity<Reserva> registrarDataCheckOut(
            @PathVariable long id,
            @RequestBody Date dataCheckOut) throws InterruptedException, ExecutionException {

        CompletableFuture<Reserva> reservaAtualizada = reservaServico.registrarDataCheckOut(id, dataCheckOut);
        Reserva reservaResult = reservaAtualizada.get();

        if (reservaResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // Retorna 201 (Created) com a reserva
                                                                                  // atualizada
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 (Not Found) se a reserva não for
                                                                        // encontrada
        }
    }

    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarReservaById(@PathVariable Long id)
            throws InterruptedException, ExecutionException {
        if (id <= 0) {
            // Retorna 400 (Bad Request) se algum campo estiver nulo
            return ResponseEntity.badRequest().body("ID inválido");
        }
        // Chama o serviço para buscar o usuário de forma assíncrona
        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);
        // Resultado da busca armazenado no 'usuarioResult'
        Reserva reservaResult = reserva.get();

        // válidação
        if (reservaResult == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // Retorna bad request 400
        } else {
            CompletableFuture<Void> delete = reservaServico.deletarReservaById(id);
            delete.get();
            return ResponseEntity.ok().body("Usuario deletado"); // Retorna ok 2000
        }
    }

}
