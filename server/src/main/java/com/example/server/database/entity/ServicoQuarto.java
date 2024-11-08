package com.example.server.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;


@Entity
public class ServicoQuarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "preco", nullable = false)
    private Double preco;

    @Column(name = "tipo_servico", nullable = false, length = 50)
    private String tipoServico;

    @Column(name = "data_solicitacao")
    private Date dataSolicitacao;

    // Construtores, getters e setters
    public ServicoQuarto() {}

    public ServicoQuarto(Reserva reserva, String nome, String descricao, Double preco, String tipoServico, Date dataSolicitacao) {
        this.reserva = reserva;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tipoServico = tipoServico;
        this.dataSolicitacao = dataSolicitacao;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
}
