package com.example.server.api.cliente.valid;

import com.example.server.database.entity.Cliente;

public class ClienteValidationUp {

    public static String validar(Cliente cliente) {
        // Validar o nome (se fornecido)
        if (cliente.getNome() != null && !cliente.getNome().isEmpty()) {
            if (!cliente.getNome().matches("[A-Za-z\\s]+") || cliente.getNome().length() > 100) {
                return "Campo 'nome' deve conter apenas letras e no máximo 100 caracteres.";
            }
        }

        // Validar o telefone (se fornecido)
        if (cliente.getTelefone() != null && !cliente.getTelefone().isEmpty()) {
            if (!cliente.getTelefone().matches("\\d{1,11}")) {
                return "Campo 'telefone' deve conter apenas números e no máximo 11 caracteres.";
            }
        }

        // Validar o email (se fornecido)
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (cliente.getEmail().length() > 100 || !cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return "Campo 'email' deve ser um email válido e ter no máximo 100 caracteres.";
            }
        }

        // Validar a data de nascimento (se fornecida)
        if (cliente.getDataNascimento() != null) {
            // Adicione qualquer validação específica para a data de nascimento, se necessário
        }

        // Validar o CPF (se fornecido)
        if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
            if (!cliente.getCpf().matches("\\d{11}")) {
                return "Campo 'cpf' deve conter apenas números e ter exatamente 11 caracteres.";
            }
        }

        // Se passou por todas as validações, retorna null (sem erro)
        return null;
    }
}
