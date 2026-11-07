/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusEnvioMensagem;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Envio_Mensagem_Agendamento")
public class EnvioMensagemAgendamento {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(name = "Data_Hora_Envio", nullable = false)
    private LocalDateTime dataHorarioEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_envio_mensagem", nullable = false)
    private StatusEnvioMensagem statusMensagemEnviada;

    @Column(name = "quantidade_tentativas_enviar")
    private Integer quantidadeTentativas;

    @Column(name = "mensagem_enviada", columnDefinition = "TEXT")
    private String mensagemEnviada;

    @Column(name = "erroEnvio", columnDefinition = "TEXT")
    private String erroEnvio;

    public EnvioMensagemAgendamento() {

    }

    public EnvioMensagemAgendamento(Long id, Agendamento agendamento, LocalDateTime dataHorarioEnvio, String mensagemEnviada, StatusEnvioMensagem statusMensagemEnviada, Integer quantidadeTentativas, String erroEnvio) {
        this.id = id;
        this.agendamento = agendamento;
        this.dataHorarioEnvio = dataHorarioEnvio;
        this.mensagemEnviada = mensagemEnviada;
        this.statusMensagemEnviada = statusMensagemEnviada;
        this.quantidadeTentativas = quantidadeTentativas;
        this.erroEnvio = erroEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public LocalDateTime getDataHorarioEnvio() {
        return dataHorarioEnvio;
    }

    public void setDataHorarioEnvio(LocalDateTime dataHorarioEnvio) {
        this.dataHorarioEnvio = dataHorarioEnvio;
    }

    public String getMensagemEnviada() {
        return mensagemEnviada;
    }

    public void setMensagemEnviada(String mensagemEnviada) {
        this.mensagemEnviada = mensagemEnviada;
    }

    public StatusEnvioMensagem getStatusMensagemEnviada() {
        return statusMensagemEnviada;
    }

    public void setStatusMensagemEnviada(StatusEnvioMensagem statusMensagemEnviada) {
        this.statusMensagemEnviada = statusMensagemEnviada;
    }

    public Integer getQuantidadeTentativas() {
        return quantidadeTentativas;
    }

    public void setQuantidadeTentativas(Integer quantidadeTentativas) {
        this.quantidadeTentativas = quantidadeTentativas;
    }

    public String getErroEnvio() {
        return erroEnvio;
    }

    public void setErroEnvio(String erroEnvio) {
        this.erroEnvio = erroEnvio;
    }

}
