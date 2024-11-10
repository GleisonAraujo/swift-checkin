package com.example.server.api.reserva;

import java.util.List;  // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;  // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired;  // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async;  // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service;  // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.Reserva;  // Importa a entidade Reserva do pacote de entidades do banco de dados
import com.example.server.database.repository.ReservaRepository;  // Importa o repositório de Reserva para acessar dados no banco de dados

@Service
public class ReservaService {
    
    private final ReservaRepository  reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    //METODOS

    //criarReserva
    public Reserva testecriarReserva(Reserva reserva) {
        Reserva atualiza = reservaRepository.save(reserva);
        return atualiza;
    }
    
    @Async
    public CompletableFuture<Reserva> criarReserva(Reserva reserva) {
        Reserva novaReseva = reservaRepository.save(reserva);
        return CompletableFuture.completedFuture(novaReseva);
    }

    //buscarTdsReserva
    @Async
    public CompletableFuture<List<Reserva>> buscarTdsReserva() {
        List<Reserva> tdsReserva = reservaRepository.findAll();
        return CompletableFuture.completedFuture(tdsReserva);
    }

    //buscarReservaById
    @Async
    public CompletableFuture<Reserva> buscaReservById(Long id) {
        Optional<Reserva> busca = reservaRepository.findById(id);
        return busca.isPresent() ? CompletableFuture.completedFuture(busca.get()) :
                                   CompletableFuture.completedFuture(null);
    }

    //atualizarReservaById *N da pra alterar cliente, não da pra altera usarios, apenas quartos e o resto da infos.
    @Async
    public CompletableFuture<Reserva> atualizarReservaById(Reserva novaReserva, Long id) {
        Optional<Reserva> reservaExistente = reservaRepository.findById(id);
        if (reservaExistente.isPresent()){ // status, quarto, qtdDiaria, dataReserva, DataCheckOut, DataCheckIn
            Reserva reserva = reservaExistente.get();
            if (novaReserva.getStatus() != null) reserva.setStatus(novaReserva.getStatus());
            if (novaReserva.getQuarto() != null) reserva.setQuarto(novaReserva.getQuarto());
            if (novaReserva.getQtdDiaria() != null) reserva.setQtdDiaria(novaReserva.getQtdDiaria());
            if (novaReserva.getDataReserva() != null) reserva.setDataReserva(novaReserva.getDataReserva());
            if (novaReserva.getDataCheckOut() != null) reserva.setDataCheckOut(novaReserva.getDataCheckOut());
            if (novaReserva.getDataCheckIn() != null) reserva.setDataCheckIn(novaReserva.getDataCheckIn());
            reservaRepository.save(novaReserva);
            return CompletableFuture.completedFuture(reserva);
        }
        return CompletableFuture.completedFuture(null);
    }

    //deletarReservaById
    @Async
    public CompletableFuture<Void> deletarReservaById(Long id) {
        reservaRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);

    }

    
}
