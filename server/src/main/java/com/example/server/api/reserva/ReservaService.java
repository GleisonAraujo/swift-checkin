package com.example.server.api.reserva;

import java.sql.Date;
import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional; // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture; // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async; // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service; // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.api.hospede.HospedeService;
import com.example.server.database.entity.HospedesAtivos;
import com.example.server.database.entity.Reserva; // Importa a entidade Reserva do pacote de entidades do banco de dados
import com.example.server.database.repository.HospedesAtivosRepository;
import com.example.server.database.repository.ReservaRepository; // Importa o repositório de Reserva para acessar dados no banco de dados

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HospedesAtivosRepository hospedesAtivosRepository;
    private final HospedeService hospedeService;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository,
            HospedesAtivosRepository hospedesAtivosRepository,
            HospedeService hospedeService) {
        this.reservaRepository = reservaRepository;
        this.hospedesAtivosRepository = hospedesAtivosRepository;
        this.hospedeService = hospedeService; // Agora o hospedeService também está sendo injetado
    }

    // METODOS

    // criarReserva
    public Reserva testecriarReserva(Reserva reserva) {

        Reserva atualiza = reservaRepository.save(reserva);
        return atualiza;
    }

    @Async
    public CompletableFuture<Reserva> criarReserva(Reserva reserva) {
        Reserva novaReseva = reservaRepository.save(reserva);
        return CompletableFuture.completedFuture(novaReseva);
    }

    // buscarTdsReserva
    @Async
    public CompletableFuture<List<Reserva>> buscarTdsReserva() {
        List<Reserva> tdsReserva = reservaRepository.findAll();
        return CompletableFuture.completedFuture(tdsReserva);
    }

    // buscarReservaById
    @Async
    public CompletableFuture<Reserva> buscaReservById(Long id) {
        Optional<Reserva> busca = reservaRepository.findById(id);
        return busca.isPresent() ? CompletableFuture.completedFuture(busca.get())
                : CompletableFuture.completedFuture(null);
    }

    // atualizarReservaById *N da pra alterar cliente, não da pra altera usarios,
    // apenas quartos e o resto da infos.
    @Async
    public CompletableFuture<Reserva> atualizarReservaById(Reserva novaReserva, Long id) {
        Optional<Reserva> reservaExistente = reservaRepository.findById(id);
        if (reservaExistente.isPresent()) { // status, quarto, qtdDiaria, dataReserva, DataCheckOut, DataCheckIn
            Reserva reserva = reservaExistente.get();
            if (novaReserva.getStatus() != null)
                reserva.setStatus(novaReserva.getStatus());
            if (novaReserva.getQuarto() != null)
                reserva.setQuarto(novaReserva.getQuarto());
            if (novaReserva.getQtdDiaria() != null)
                reserva.setQtdDiaria(novaReserva.getQtdDiaria());
            if (novaReserva.getDataReserva() != null)
                reserva.setDataReserva(novaReserva.getDataReserva());
            if (novaReserva.getDataCheckOut() != null)
                reserva.setDataCheckOut(novaReserva.getDataCheckOut());
            if (novaReserva.getDataCheckIn() != null)
                reserva.setDataCheckIn(novaReserva.getDataCheckIn());
            reservaRepository.save(novaReserva);
            return CompletableFuture.completedFuture(reserva);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Reserva> registrarDataCheckIn(Long id, Date dataCheckIn) {
        Optional<Reserva> reservaExistente = reservaRepository.findById(id);

        // Verifica se a reserva existe
        if (reservaExistente.isPresent()) {
            Reserva reserva = reservaExistente.get();

            // Verifica se dataCheckIn não é null e se é uma data válida (não o valor
            // padrão)
            if (dataCheckIn != null && dataCheckIn.getTime() != 0) {
                // Se a data de check-in ainda estiver nula, define a data
                if (reserva.getDataCheckIn() == null) {
                    reserva.setDataCheckIn(dataCheckIn);
                    // Salva a reserva com a data de check-in
                    reservaRepository.save(reserva);

                    // Implementar o registro do hóspede
                    HospedesAtivos hospede = new HospedesAtivos();
                    hospede.setReserva(reserva);
                    hospede.setStatus(true);
                    hospedeService.checkInHospede(hospede);
                    return CompletableFuture.completedFuture(reserva);
                }
                // Retorna a reserva
                return CompletableFuture.completedFuture(null);
            }
        }
        // Se a reserva não for encontrada, retorna null
        return CompletableFuture.completedFuture(null);
    }

    @Async
public CompletableFuture<Reserva> registrarDataCheckOut(Long id, Date dataCheckOut) {
    Optional<Reserva> reservaExistente = reservaRepository.findById(id);
    if (!reservaExistente.isPresent()) {
        // Se a reserva não existir, retornamos null para indicar que o ID não foi encontrado
        return CompletableFuture.completedFuture(null);
    }
    
    Reserva reserva = reservaExistente.get();

    // Verifica se a data de check-out é válida
    if (dataCheckOut != null && dataCheckOut.getTime() != 0) {
        // Verifica se o check-out ainda não foi feito e o check-in já foi realizado
        if (reserva.getDataCheckOut() == null && reserva.getDataCheckIn() != null) {
            reserva.setDataCheckOut(dataCheckOut);
            reserva.setStatus(false); // Define o status como false ao registrar o check-out

            // Registra o check-out
            reservaRepository.save(reserva);

            // Verifica se o hóspede está ativo e realiza o check-out
            HospedesAtivos hospede = hospedesAtivosRepository.findByReserva(reserva).orElse(null);
            if (hospede != null) {
                hospede.setStatus(false); // Marca o hóspede como "check-out"
                hospede.setReserva(reserva); // Vincula a reserva ao hóspede
                hospedeService.checkOutHospede(hospede); // Chama o serviço de check-out
            }
            return CompletableFuture.completedFuture(reserva);
        } else if (reserva.getDataCheckOut() != null) {
            // Se o check-out já foi feito, retorna null para indicar que não é necessário fazer nada
            return CompletableFuture.completedFuture(null);
        } else {
            // Caso o check-in não tenha sido realizado
            return CompletableFuture.completedFuture(null);
        }
    }
    // Caso a data de check-out seja inválida, retornamos null
    return CompletableFuture.completedFuture(null);
}


    // deletarReservaById
    @Async
    public CompletableFuture<Void> deletarReservaById(Long id) {
        reservaRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);

    }
}
