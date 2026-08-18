/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Pacientes;
import java.util.List;

/**
 *
 * @author Juliano
 */
public interface PacienteDAO {

    public void CadastrarCliente(Pacientes cliente);
    public List<Pacientes> listarPacientes();
    public List<Pacientes> filtrarPacientes(PacienteFiltro clientefiltro);
    public void deletarCliente(Pacientes cliente);
    public void atualizarCliente(Pacientes cliente);
    
}
