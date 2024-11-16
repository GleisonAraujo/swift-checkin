package com.example.server.api.cliente.valid;

import com.example.server.database.entity.Cliente;

public class ClienteValidation {

        public static String validar(Cliente cliente) {
            // Verificar se o nome está presente e é válido
            if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
                return "Campo 'nome' é obrigatório.";
            }
            if (!cliente.getNome().matches("[A-Za-z\\s]+") || cliente.getNome().length() > 100) {
                return "Campo 'nome' deve conter apenas letras e no máximo 100 caracteres.";
            }
    
            // Verificar se o telefone está presente e é válido
            if (cliente.getTelefone() == null || cliente.getTelefone().isEmpty()) {
                return "Campo 'telefone' é obrigatório.";
            }
            if (!cliente.getTelefone().matches("\\d{1,11}")) {
                return "Campo 'telefone' deve conter apenas números e no máximo 11 caracteres.";
            }
    
            // Verificar se o email está presente e é válido
            if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
                return "Campo 'email' é obrigatório.";
            }
            if (cliente.getEmail().length() > 100 || !cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return "Campo 'email' deve ser um email válido e ter no máximo 100 caracteres.";
            }
    
            // Verificar se a data de nascimento está presente
            if (cliente.getDataNascimento() == null) {
                return "Campo 'DataNascimento' é obrigatório.";
            }
    
            // Verificar se o CPF está presente e é válido
            if (cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
                return "Campo 'cpf' é obrigatório.";
            }
            if (!cliente.getCpf().matches("\\d{11}")) {
                return "Campo 'cpf' deve conter apenas números e ter exatamente 11 caracteres.";
            }
    
            // Se passou por todas as validações, retorna null (sem erro)
            return null;
        }
    }
    