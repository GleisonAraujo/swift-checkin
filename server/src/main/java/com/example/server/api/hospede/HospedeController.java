package com.example.server.api.hospede;

import java.util.List; // Importa a classe List para trabalhar com listas de objetos
import java.util.concurrent.CompletableFuture;  // Importa a classe CompletableFuture para trabalhar com funções assíncronas
import java.util.concurrent.ExecutionException; // Importa a classe ExecutionException para lidar com exceções ao esperar o resultado de tarefas assíncronas

import org.springframework.beans.factory.annotation.Autowired; // Importa o Autowired para permitir a injeção de dependências no Spring
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para trabalhar com códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para criar respostas HTTP personalizadas

import org.springframework.web.bind.annotation.*; // Importa todas as anotações relacionadas a controladores REST no Spring MVC (como @RestController, @GetMapping, @PostMapping, etc.)

import com.example.server.api.cliente.ClienteService;
import com.example.server.database.entity.Cliente;
import com.example.server.database.entity.HospedesAtivos;

@RestController // Define a classe como controller
@RequestMapping("/hospede/") // Definindo o caminho da API 
public class HospedeController {
    
    @Autowired // O Spring vai injetar o UsuarioServico aqui para 
    private HospedeService hospedeService;

    //Get all
    @GetMapping("/")
    public ResponseEntity<List<HospedesAtivos>> tdsHospede() throws InterruptedException, ExecutionException {
        CompletableFuture<List<HospedesAtivos>> tdsHopedes = hospedeService.tdsHospede();
        List<HospedesAtivos> listHopedes = tdsHopedes.get(); 
        return ResponseEntity.ok(listHopedes);
        
    }

    //Get all TRUE
    @GetMapping("/ativos/")
    public ResponseEntity<List<HospedesAtivos>> tdsHospedeAtivos() throws InterruptedException, ExecutionException {
        CompletableFuture<List<HospedesAtivos>> tdsHopedes = hospedeService.tdsHospedeAtivos();
        List<HospedesAtivos> listHopedes = tdsHopedes.get(); 
        return ResponseEntity.ok(listHopedes);
        
    }

}
