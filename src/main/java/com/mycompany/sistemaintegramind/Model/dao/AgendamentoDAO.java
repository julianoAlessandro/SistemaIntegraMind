/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Cargo;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.util.List;

/**
 *
 * @author Micro
 */
public interface AgendamentoDAO {

    public void cadastrarAgendamento(Agendamento agendamento);

    public List<Agendamento> listarAgendamentos();

    public void atualizarAgendamento(Agendamento agendamento);

    public Agendamento buscarPorId(Long id);
}
