package com.example.server.api.hospede;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional; // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture; // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async; // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service; // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.Cliente;
import com.example.server.database.entity.HospedesAtivos; // Importa a entidade Cliente do pacote de entidades do banco de dados
import com.example.server.database.repository.HospedesAtivosRepository; // Importa o repositório de Cliente para acessar dados no banco de dados

@Service
public class HospedeService {

    private final HospedesAtivosRepository hospedesAtivosRepository;

    @Autowired
    public HospedeService(HospedesAtivosRepository hospedesAtivosRepository) {
        this.hospedesAtivosRepository = hospedesAtivosRepository;
    }

    // METODOS

    // ALL HOSPEDES 
    @Async
    public CompletableFuture<List<HospedesAtivos>> tdsHospede() {
        List<HospedesAtivos> hospedes = hospedesAtivosRepository.findAll();
        return CompletableFuture.completedFuture(hospedes);
    }

    @Async
    public CompletableFuture<List<HospedesAtivos>> tdsHospedeAtivos() {
        List<HospedesAtivos> hospedes = hospedesAtivosRepository.findByStatus(true);
        return CompletableFuture.completedFuture(hospedes);
    }

    // checkInHospede
    @Async
    public CompletableFuture<HospedesAtivos> checkInHospede(HospedesAtivos hospede) {
        HospedesAtivos criar = hospedesAtivosRepository.save(hospede);
        return CompletableFuture.completedFuture(criar);
    }

    // checkOutHospede
    @Async
    public CompletableFuture<HospedesAtivos> checkOutHospede(HospedesAtivos hospede) {
        HospedesAtivos criar = hospedesAtivosRepository.save(hospede);
        return CompletableFuture.completedFuture(criar);
    }

}
