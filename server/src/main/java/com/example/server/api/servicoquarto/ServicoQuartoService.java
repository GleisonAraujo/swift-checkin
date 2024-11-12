package com.example.server.api.servicoquarto;

import java.util.List;  // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;  // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;  // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async;  // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service;  // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.ServicoQuarto;  // Importa a entidade Usuario do pacote de entidades do banco de dados
import com.example.server.database.repository.ServicoQuartoRepository;  // Importa o repositório de Usuario para acessar dados no banco de dados

@Service // Marcação da classe como serviço 
public class ServicoQuartoService {
    
    // Instanciando ServicoQuartoRepository
    private final ServicoQuartoRepository servicoQuartoRepository;
    
    // Injetando ServicoQuartoRepository na classe ServicoQuartoService
    @Autowired // Marca para injetar
    public ServicoQuartoService(ServicoQuartoRepository servicoQuartoRepository) {
        this.servicoQuartoRepository = servicoQuartoRepository;
    }
    // Metodos

    // criarQuarto
    @Async // Indicando ao Spring que o método deve ser executado assíncronamente
    public CompletableFuture<ServicoQuarto> criarServicoQuarto(ServicoQuarto servicoQuarto) {
        ServicoQuarto novoServicoQuarto = servicoQuartoRepository.save(servicoQuarto);
        return CompletableFuture.completedFuture(novoServicoQuarto);
    }
    //exemplo de função sincrona
    public ServicoQuarto sincronoCriarQuarto(ServicoQuarto servicoQuarto){
        ServicoQuarto novoServicoQuarto = servicoQuartoRepository.save(servicoQuarto);
        return novoServicoQuarto;
    }
    
    // BuscaTdsQuarto
    @Async
    public CompletableFuture<List<ServicoQuarto>> BuscaTdsQuarto() {
        List<ServicoQuarto> servicoQuartos = servicoQuartoRepository.findAll();
        return CompletableFuture.completedFuture(servicoQuartos);
    }
    
    // BuscaByIdQuarto
    @Async
    public CompletableFuture<ServicoQuarto> buscaByIdQuarto(Long id) {
        Optional<ServicoQuarto> servicoQuartos = servicoQuartoRepository.findById(id);
        return servicoQuartos.isPresent() ? CompletableFuture.completedFuture(servicoQuartos.get()) :
                                            CompletableFuture.completedFuture(null);
    }

    // attQuarto
      //getNome() - getPreco() - getTipoServico() - getDataSolicitacao()
    @Async
    
public CompletableFuture<ServicoQuarto> atualizarServicoQuartoById(ServicoQuarto novoServicoQuarto, Long id) {
    Optional<ServicoQuarto> servicoQuartoExistente = servicoQuartoRepository.findById(id);
    
    if (servicoQuartoExistente.isPresent()) {
        ServicoQuarto servicoQuarto = servicoQuartoExistente.get();
        
        if (novoServicoQuarto.getNome() != null) servicoQuarto.setNome(novoServicoQuarto.getNome());
        if (novoServicoQuarto.getPreco() != null) servicoQuarto.setPreco(novoServicoQuarto.getPreco());
        if (novoServicoQuarto.getTipoServico() != null) servicoQuarto.setTipoServico(novoServicoQuarto.getTipoServico());
        if (novoServicoQuarto.getDataSolicitacao() != null) servicoQuarto.setDataSolicitacao(novoServicoQuarto.getDataSolicitacao());
        
        servicoQuartoRepository.save(servicoQuarto);
        return CompletableFuture.completedFuture(servicoQuarto);
    }
    
    return CompletableFuture.completedFuture(null);
}


    
    

    // deletarQuarto
    @Async
    public CompletableFuture<Void> deletarQuarto(Long id) {
        servicoQuartoRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }

}
