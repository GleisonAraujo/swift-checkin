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

    @Autowired // O Spring vai injetar o ReservaService aqui para utilizar os métodos
    private ReservaService reservaServico;
    @Autowired // O Spring vai injetar o UsuarioService aqui para utilizar os métodos
    private UsuarioService usuarioServico;
    @Autowired // O Spring vai injetar o QuartoService aqui para utilizar os métodos
    private QuartoService quartoService;
    @Autowired // O Spring vai injetar o ClienteService aqui para utilizar os métodos
    private ClienteService clienteService;

    // Métodos

    // Endpoint 'POST' para criar reserva
    @PostMapping("/")
    public ResponseEntity<?> criarReserva(@RequestBody Reserva novaReserva) {
        // Validar dados da nova reserva
        if (novaReserva.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status da reserva não pode ser nulo"); // 400 -  
        }
        if (novaReserva.getQtdDiaria() == null || novaReserva.getQtdDiaria() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade de diárias inválida"); // 400 - Bad
        }
        if (novaReserva.getDataReserva() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data da reserva não pode ser nula"); // 400 - Bad
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"); // 404 - Not Found
        }
        if (quarto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quarto não encontrado"); // 404 - Not Found
        }
        if (Boolean.FALSE.equals(quarto.getDisponibilidade())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quarto indisponível"); // 404 - Not Found
        }
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"); // 404 - Not Found
        }

        // Associar as entidades encontradas à nova reserva
        novaReserva.setUsuario(usuario);
        novaReserva.setQuarto(quarto);
        novaReserva.setCliente(cliente);

        Double valorreserva = novaReserva.getQtdDiaria() * quarto.getPrDiaria();
        novaReserva.setValorFinal(valorreserva);

        // Criar a reserva no banco
        reservaServico.criarReserva(novaReserva);

        // Retornar a nova reserva criada
        return ResponseEntity.ok(novaReserva); // 200 - OK
    }

    // Endpoint 'GET' para retornar todas as reservas
    @GetMapping("/")
    public ResponseEntity<?> buscarTdsReserva() throws InterruptedException, ExecutionException {
        CompletableFuture<List<Reserva>> tdsReserva = reservaServico.buscarTdsReserva();
        List<Reserva> result = tdsReserva.get();

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 - No Content
        }
        return ResponseEntity.ok(result); // 200 - OK
    }

    // Endpoint 'GET' para retornar uma reserva pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscaReservById(@PathVariable long id) throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar a reserva de forma assíncrona
        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);

        // Espera a resposta da operação assíncrona
        Reserva result = reserva.get(); // Aguarda o retorno

        if (result != null) {
            return ResponseEntity.ok(result); // 200 - OK
        } else {
            return ResponseEntity.badRequest().body("Não encontrado"); // 400 - Bad Request
        }
    }

    // Endpoint 'PATCH' para atualizar uma reserva pelo ID
    @PatchMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReservaById(@RequestBody Reserva reserva, @PathVariable long id)
            throws InterruptedException, ExecutionException {
        // Chama o serviço para atualizar a reserva de forma assíncrona
        CompletableFuture<Reserva> reservaAtualizado = reservaServico.atualizarReservaById(reserva, id);

        // Espera a resposta da operação assíncrona
        Reserva reservaResult = reservaAtualizado.get(); // Aguarda o retorno

        if (reservaResult != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // 201 - Created
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 - Not Found
        }
    }

    // Endpoint 'PATCH' para registrar data de CheckIn
    @PatchMapping("/checkin/{id}")
    public ResponseEntity<?> registrarDataCheckIn(
            @PathVariable long id,
            @RequestBody Date dataCheckIn) throws InterruptedException, ExecutionException {

        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);

        // Espera a resposta da operação assíncrona
        Reserva result = reserva.get(); // Aguarda o retorno

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não encontrado"); // 404 - Not Found
        }
        if (result.getStatus() == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva indisponível"); // 404 - Not Found
        }

        CompletableFuture<Reserva> reservaAtualizada = reservaServico.registrarDataCheckIn(id, dataCheckIn);
        Reserva reservaResult = reservaAtualizada.get();

        if (reservaResult != null) {
            // Atualiza a disponibilidade do quarto
            Long quartoId = reservaResult.getQuarto().getId();
            CompletableFuture<Quarto> quartoFuture = quartoService.buscarQuartoById(quartoId);
            Quarto quarto = quartoFuture.join();

            if (quarto != null) {
                // Define a disponibilidade do quarto como verdadeira (disponível)
                quarto.setDisponibilidade(false);
                quartoService.atualizarQuarto(quartoId, quarto);
            }

            // Retorna a resposta com a reserva atualizada
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // 201 - Created
        } else {
            return ResponseEntity.badRequest().body("Check-in já realizado"); // 400 - Bad Request
        }
    }

    // Endpoint 'PATCH' para registrar data de CheckOut e definir status como false
    @PatchMapping("/checkout/{id}")
    public ResponseEntity<?> registrarDataCheckOut(
            @PathVariable long id,
            @RequestBody Date dataCheckOut) throws InterruptedException, ExecutionException {

        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);

        // Espera a resposta da operação assíncrona
        Reserva result = reserva.get(); // Aguarda o retorno
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não encontrado"); // 404 - Not Found
        }
        if (result.getStatus() == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva indisponível"); // 404 - Not Found
        }

        // Realiza a atualização da data de CheckOut
        CompletableFuture<Reserva> reservaAtualizada = reservaServico.registrarDataCheckOut(id, dataCheckOut);
        Reserva reservaResult = reservaAtualizada.get();

        if (reservaResult != null) {
            // Atualiza a disponibilidade do quarto
            Long quartoId = reservaResult.getQuarto().getId();
            CompletableFuture<Quarto> quartoFuture = quartoService.buscarQuartoById(quartoId);
            Quarto quarto = quartoFuture.join();

            if (quarto != null) {
                // Define a disponibilidade do quarto como verdadeira (disponível)
                quarto.setDisponibilidade(true);
                quartoService.atualizarQuarto(quartoId, quarto);
            }

            // Retorna a resposta com a reserva atualizada
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaResult); // 201 - Created
        } else {
            return ResponseEntity.badRequest().body("Check-in não realizado ou data de check-out inválida"); // 400 - bad                                                                                                 
        }
    }

    // Endpoint 'DELETE' para deletar uma reserva pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarReservaById(@PathVariable Long id)
            throws InterruptedException, ExecutionException {
        // Chama o serviço para buscar a reserva de forma assíncrona
        CompletableFuture<Reserva> reserva = reservaServico.buscaReservById(id);
        // Resultado da busca armazenado no 'reservaResult'
        Reserva reservaResult = reserva.get();

        // Validação
        if (reservaResult == null) {
            return ResponseEntity.badRequest().body("ID não encontrado"); // 400 - Bad Request
        } else {
            // Chama o serviço para deletar a reserva de forma assíncrona
            CompletableFuture<Void> delete = reservaServico.deletarReservaById(id);
            delete.get(); // Aguarda a exclusão da reserva
            return ResponseEntity.ok().body("Reserva deletada"); // 200 - OK
        }
    }
}
