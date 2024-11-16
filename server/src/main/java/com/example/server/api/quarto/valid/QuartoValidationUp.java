package com.example.server.api.quarto.valid;

import com.example.server.database.entity.Quarto;

public class QuartoValidationUp {
    
    public static String validar(Quarto quarto) {
        // Verificar o número (máximo de 3 caracteres numéricos)
        if (quarto.getNumero() != null && !quarto.getNumero().isEmpty()) {
            if (!quarto.getNumero().matches("\\d{1,3}")) {
                return "Campo 'numero' deve conter apenas números e no máximo 3 caracteres.";
            }
        }
    
        // Verificar a descrição (não pode ser nula ou vazia se fornecida)
        if (quarto.getDescricao() != null && !quarto.getDescricao().isEmpty()) {
            if (quarto.getDescricao().isEmpty()) {
                return "Campo 'descricao' não pode estar vazio.";
            }
        }
    
        // Verificar o tipo (deve ser 'quarto', 'casal' ou 'standard', se fornecido)
        if (quarto.getTipo() != null && !quarto.getTipo().isEmpty()) {
            if (!quarto.getTipo().equalsIgnoreCase("quarto") && 
                !quarto.getTipo().equalsIgnoreCase("casal") && 
                !quarto.getTipo().equalsIgnoreCase("standard")) {
                return "Campo 'tipo' deve ser 'quarto', 'casal' ou 'standard'.";
            }
        }
    
        // Verificar o preço da diária (campo 'valorDiaria', tipo Double)
        if (quarto.getPrDiaria() != null) {
            // Converter o preço da diária para String e verificar o comprimento (máximo 6 caracteres)
            String prDiariaString = quarto.getPrDiaria().toString();
            if (prDiariaString.length() > 6) {
                return "Campo 'valorDiaria' deve ter no máximo 6 caracteres.";
            }
    
            // Verificar se o preço da diária tem no máximo 2 casas decimais
            if (prDiariaString.contains(".") && prDiariaString.split("\\.")[1].length() > 2) {
                return "Campo 'valorDiaria' deve ter no máximo 2 casas decimais.";
            }
        }
        // Se passou por todas as validações, retorna null (sem erro)
        return null;
    }
    
}
