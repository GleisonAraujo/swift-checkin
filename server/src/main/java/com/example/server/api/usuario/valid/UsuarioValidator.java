package com.example.server.api.usuario.valid;

import com.example.server.database.entity.Usuario;

public class UsuarioValidator {

    public static String validarUsuario(Usuario usuario) {

        // Validação do campo 'nome'
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            return "Campo 'nome' é obrigatório.";
        }
        
        if (usuario.getNome().length() < 3 || usuario.getNome().length() > 20) {
            return "Campo 'nome' deve ter entre 3 e 20 caracteres.";
        }

        if (!usuario.getNome().matches("^[A-Za-z]+$")) {
            return "Campo 'nome' deve conter apenas letras (sem números ou caracteres especiais).";
        }

        // Validação do campo 'fullName'
        if (usuario.getFullName() == null || usuario.getFullName().isEmpty()) {
            return "Campo 'fullName' é obrigatório.";
        }

        if (usuario.getFullName().length() < 10 || usuario.getFullName().length() > 50) {
            return "Campo 'fullName' deve ter entre 10 e 50 caracteres.";
        }

        // Expressão regular para permitir apenas letras e espaços
        if (!usuario.getFullName().matches("^[A-Za-z\\s]+$")) {
            return "Campo 'fullName' deve conter apenas letras e espaços (sem números ou caracteres especiais).";
        }

        // Validação do campo 'senha'
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            return "Campo 'senha' é obrigatório.";
        }

        if (usuario.getSenha().length() != 8) {
            return "Campo 'senha' deve ter exatamente 8 caracteres.";
        }

        if (!usuario.getSenha().matches("^[0-9]{8}$")) {
            return "Campo 'senha' deve conter apenas números e ter exatamente 8 caracteres.";
        }

        // Se todos os campos passarem nas validações
        return null;
    }
}
