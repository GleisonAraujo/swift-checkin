package com.example.server.api.cliente;

import java.util.List;  // Importa a classe List para trabalhar com listas de objetos
import java.util.Optional;  // Importa a classe Optional para tratar valores que podem ou não existir (como um valor nulo)
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas

import org.springframework.beans.factory.annotation.Autowired;  // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.scheduling.annotation.Async;  // Importa a anotação Async para marcar métodos que devem ser executados de forma assíncrona
import org.springframework.stereotype.Service;  // Importa a anotação Service para registrar a classe como um componente de serviço no Spring

import com.example.server.database.entity.Cliente;  // Importa a entidade Cliente do pacote de entidades do banco de dados
import com.example.server.database.repository.ClienteRepository;  // Importa o repositório de Cliente para acessar dados no banco de dados

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Metodos
    
    // Método criarCliente assíncrono para criar cliente
    @Async
    public CompletableFuture<Cliente> criarCliente(Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return CompletableFuture.completedFuture(clienteSalvo);
    }

    // Método buscarCliente assíncrono para buscar um cliente pelo ID
    @Async
    public CompletableFuture<Cliente> buscarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.isPresent() ? CompletableFuture.completedFuture(cliente.get()) :
                                     CompletableFuture.completedFuture(null);
    }

    // Método todosClientes assíncrono para retornar todos os clientes
    @Async
    public CompletableFuture<List<Cliente>> todosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return CompletableFuture.completedFuture(clientes);
    }

    // Método atualizarCliente assíncrono para atualizar um cliente
    @Async
    public CompletableFuture<Cliente> atualizarCliente(Long id, Cliente clienteAtualizado) {
        // Buscar cliente existente
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            // Atualizar dados do cliente
            if (clienteAtualizado.getNome() != null) cliente.setNome(clienteAtualizado.getNome());
            if (clienteAtualizado.getCpf() != null) cliente.setCpf(clienteAtualizado.getCpf());
            if (clienteAtualizado.getEmail() != null) cliente.setEmail(clienteAtualizado.getEmail());
            if (clienteAtualizado.getTelefone() != null) cliente.setTelefone(clienteAtualizado.getTelefone());
            if (clienteAtualizado.getDataNascimento() != null) cliente.setDataNascimento(clienteAtualizado.getDataNascimento());

            // Salvar e retornar o cliente atualizado
            Cliente clienteSalvo = clienteRepository.save(cliente);
            return CompletableFuture.completedFuture(clienteSalvo);
        } else {
            // Retorna um CompletableFuture com um valor vazio ou null
            return CompletableFuture.completedFuture(null);
        }
    }

    // Método deletarCliente assíncrono para deletar um cliente
    @Async
    public CompletableFuture<Void> deletarCliente(Long id) {
        // Verifica se o cliente existe
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            clienteRepository.deleteById(id);
            return CompletableFuture.completedFuture(null);
        } else {
            // Se não encontrado, retorna uma CompletableFuture vazia
            return CompletableFuture.completedFuture(null);
        }
    }
    
}
