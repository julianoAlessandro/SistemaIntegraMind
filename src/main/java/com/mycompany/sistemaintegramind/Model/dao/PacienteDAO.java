/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import java.util.List;

/**
 *
 * @author Juliano
 */
public interface PacienteDAO {

    public void CadastrarCliente(Paciente cliente);
    public List<Paciente> listarPacientes();
    public List<Paciente> filtrarPacientes(PacienteFiltro clientefiltro);
    public void deletarCliente(Paciente cliente);
    public void atualizarPaciente(Paciente cliente);
    
}
