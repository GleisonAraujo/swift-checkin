package com.example.server.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import java.util.Date;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_check_in")
    private Date dataCheckIn;

    @Column(name = "data_check_out")
    private Date dataCheckOut;

    @Column(name = "qtdDiaria", nullable = false)
    private Integer qtdDiaria;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "data_reserva")
    private Date dataReserva;

    @Column(name = "valorFinal", nullable = false)
    private Double valorFinal;

    // Construtores, getters e setters
    public Reserva() {}

    public Reserva(Cliente cliente, Quarto quarto, Usuario usuario, Date dataCheckIn, Date dataCheckOut, Integer qtdDiaria, Boolean status, Date dataReserva, Double valorFinal) {
        this.cliente = cliente;
        this.quarto = quarto;
        this.usuario = usuario;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.qtdDiaria = qtdDiaria;
        this.status = status;
        this.dataReserva = dataReserva;
        this.valorFinal = valorFinal;
    }
    @PrePersist
    public void prePersist() {
        if (this.valorFinal == null) {
            this.valorFinal = 0.0;  
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(Date dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public Date getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(Date dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public Integer getQtdDiaria() {
        return qtdDiaria;
    }

    public void setQtdDiaria(Integer qtdDiaria) {
        this.qtdDiaria = qtdDiaria;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }
    
    
}