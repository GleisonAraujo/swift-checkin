package com.example.server.api.quarto;

import java.util.List;  // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;  // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired;  // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async;  // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service;  // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.Quarto;  // Importa a entidade Quarto do pacote de entidades do banco de dados
import com.example.server.database.repository.QuartoRepository;  // Importa o repositório de Quarto para acessar dados no banco de dados

@Service
public class QuartoService {
    
    private final QuartoRepository quartoRepository;

    @Autowired
    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    // Método criarQuarto assíncrono para criar um novo quarto
    @Async
    public CompletableFuture<Quarto> criarQuarto(Quarto quarto) {
        Quarto quartoSalvo = quartoRepository.save(quarto);
        return CompletableFuture.completedFuture(quartoSalvo);
    }

    // Método buscarQuartoById assíncrono para buscar um quarto pelo ID
    @Async
    public CompletableFuture<Quarto> buscarQuartoById(Long id) {
        Optional<Quarto> quarto = quartoRepository.findById(id);
        return quarto.isPresent() ? CompletableFuture.completedFuture(quarto.get()) :
                                    CompletableFuture.completedFuture(null);
    }

    // Método buscarTdsQuarto assíncrono para retornar todos os quartos
    @Async
    public CompletableFuture<List<Quarto>> buscarTdsQuarto() {
        List<Quarto> quartos = quartoRepository.findAll();
        return CompletableFuture.completedFuture(quartos);
    }

    // Método atualizarQuarto assíncrono para atualizar as informações de um quarto
    @Async
    public CompletableFuture<Quarto> atualizarQuarto(Long id, Quarto quartoAtualizado) {
        // Buscar quarto existente
        Optional<Quarto> quartoExistente = quartoRepository.findById(id);
        if (quartoExistente.isPresent()) {
            Quarto quarto = quartoExistente.get();
            // Atualizar dados do quarto
            if (quartoAtualizado.getNumero() != null) quarto.setNumero(quartoAtualizado.getNumero());
            if (quartoAtualizado.getDescricao() != null) quarto.setDescricao(quartoAtualizado.getDescricao());
            if (quartoAtualizado.getTipo() != null) quarto.setTipo(quartoAtualizado.getTipo());
            if (quartoAtualizado.getPrDiaria() != null) quarto.setPrDiaria(quartoAtualizado.getPrDiaria());

            // Salvar e retornar o quarto atualizado
            Quarto quartoSalvo = quartoRepository.save(quarto);
            return CompletableFuture.completedFuture(quartoSalvo);
        } else {
            // Retorna um CompletableFuture com valor nulo caso não encontre o quarto
            return CompletableFuture.completedFuture(null);
        }
    }

    // Método deletarQuarto assíncrono para deletar um quarto
    @Async
    public CompletableFuture<Void> deletarQuarto(Long id) {
        // Verifica se o quarto existe
        Optional<Quarto> quartoExistente = quartoRepository.findById(id);
        if (quartoExistente.isPresent()) {
            quartoRepository.deleteById(id);
            return CompletableFuture.completedFuture(null);
        } else {
            // Se não encontrado, retorna uma CompletableFuture vazia
            return CompletableFuture.completedFuture(null);
        }
    }
}
