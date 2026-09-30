/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.FrequenciaAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.util.Utilitarios.StatusAgendamento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Agendamento")
public class Agendamento extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "Data", nullable = false)
    private LocalDate dataAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_Atendimento")
    private TipoAtendimento tipoatendimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento")
    private StatusAgendamento statusagendamento;

    @Column(name = "Observação", length = 700, nullable = true)
    private String observacao;

    @Column(name = "Horário", nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    private StatusPagamento statuspagamento;

    @Column(name = "valor_da_consulta")
    private BigDecimal valorDaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status_Agendamento_Paciente")
    private StatusPacienteAgendamento statuspacienteagendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "Frequência_Atendimento_Paciente")
    private FrequenciaAtendimento frequenciaatendimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public TipoAtendimento getTipoatendimento() {
        return tipoatendimento;
    }

    public void setTipoatendimento(TipoAtendimento tipoatendimento) {
        this.tipoatendimento = tipoatendimento;
    }

    public StatusAgendamento getStatusagendamento() {
        return statusagendamento;
    }

    public void setStatusagendamento(StatusAgendamento statusagendamento) {
        this.statusagendamento = statusagendamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public StatusPagamento getStatuspagamento() {
        return statuspagamento;
    }

    public void setStatuspagamento(StatusPagamento statuspagamento) {
        this.statuspagamento = statuspagamento;
    }

    public BigDecimal getValorDaConsulta() {
        return valorDaConsulta;
    }

    public void setValorDaConsulta(BigDecimal valorDaConsulta) {
        this.valorDaConsulta = valorDaConsulta;
    }

    public StatusPacienteAgendamento getStatusPacienteAgendamento() {
        return statuspacienteagendamento;
    }

    public void setStatusPacienteAgendamento(StatusPacienteAgendamento statuspacienteagendamento) {
        this.statuspacienteagendamento = statuspacienteagendamento;
    }

    public StatusPacienteAgendamento getStatuspacienteagendamento() {
        return statuspacienteagendamento;
    }

    public void setStatuspacienteagendamento(StatusPacienteAgendamento statuspacienteagendamento) {
        this.statuspacienteagendamento = statuspacienteagendamento;
    }

    public FrequenciaAtendimento getFrequenciaatendimento() {
        return frequenciaatendimento;
    }

    public void setFrequenciaatendimento(FrequenciaAtendimento frequenciaatendimento) {
        this.frequenciaatendimento = frequenciaatendimento;
    }

}
