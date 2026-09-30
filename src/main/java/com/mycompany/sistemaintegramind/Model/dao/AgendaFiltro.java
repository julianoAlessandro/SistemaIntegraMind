/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.FrequenciaAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.time.LocalDate;


public class AgendaFiltro {
    private LocalDate dataAgendamento;
    private StatusPagamento statuspagamento;
    private FrequenciaAtendimento frequenciaAtendimento;
    private TipoAtendimento tipoatendimento;
    private Paciente paciente;

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public StatusPagamento getStatuspagamento() {
        return statuspagamento;
    }

    public void setStatuspagamento(StatusPagamento statuspagamento) {
        this.statuspagamento = statuspagamento;
    }

    public FrequenciaAtendimento getFrequenciaAtendimento() {
        return frequenciaAtendimento;
    }

    public void setFrequenciaAtendimento(FrequenciaAtendimento frequenciaAtendimento) {
        this.frequenciaAtendimento = frequenciaAtendimento;
    }

    public TipoAtendimento getTipoatendimento() {
        return tipoatendimento;
    }

    public void setTipoatendimento(TipoAtendimento tipoatendimento) {
        this.tipoatendimento = tipoatendimento;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    
    
    
}
