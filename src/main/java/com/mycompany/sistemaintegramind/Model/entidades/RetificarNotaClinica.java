package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
@Table(name = "Retificar_Nota_Clinica")
public class RetificarNotaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_original_id", nullable = false)
    private EvolucaoClinica notaOriginal;

    @ManyToOne
    @JoinColumn(name = "nota_retificadora_id", nullable = false)
    private EvolucaoClinica notaRetificadora;

    @Column(name = "motivo_retificacao", nullable = false, columnDefinition = "TEXT")
    private String motivoRetificacao;

    @Column(name = "data_retificacao", nullable = false)
    private LocalDate dataRetificacao;

    @Column(name = "horario_retificacao", nullable = false)
    private LocalTime horarioRetificacao;

    public RetificarNotaClinica() {
    }

    public RetificarNotaClinica(Long id, EvolucaoClinica notaOriginal, EvolucaoClinica notaRetificadora, String motivoRetificacao, LocalDate dataRetificacao, LocalTime horarioRetificacao) {
        this.id = id;
        this.notaOriginal = notaOriginal;
        this.notaRetificadora = notaRetificadora;
        this.motivoRetificacao = motivoRetificacao;
        this.dataRetificacao = dataRetificacao;
        this.horarioRetificacao = horarioRetificacao;
    }

    public Long getId() {
        return id;
    }

    public EvolucaoClinica getNotaOriginal() {
        return notaOriginal;
    }

    public void setNotaOriginal(EvolucaoClinica notaOriginal) {
        this.notaOriginal = notaOriginal;
    }

    public EvolucaoClinica getNotaRetificadora() {
        return notaRetificadora;
    }

    public void setNotaRetificadora(EvolucaoClinica notaRetificadora) {
        this.notaRetificadora = notaRetificadora;
    }

    public String getMotivoRetificacao() {
        return motivoRetificacao;
    }

    public void setMotivoRetificacao(String motivoRetificacao) {
        this.motivoRetificacao = motivoRetificacao;
    }

    public LocalDate getDataRetificacao() {
        return dataRetificacao;
    }

    public void setDataRetificacao(LocalDate dataRetificacao) {
        this.dataRetificacao = dataRetificacao;
    }

    public LocalTime getHorarioRetificacao() {
        return horarioRetificacao;
    }

    public void setHorarioRetificacao(LocalTime horarioRetificacao) {
        this.horarioRetificacao = horarioRetificacao;
    }
}
