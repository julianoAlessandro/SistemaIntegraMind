/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Paciente;

public class FichaProntuarioPacienteFiltro {

    private String evolucaoSessao;
    private Long pacienteId;
    
    public FichaProntuarioPacienteFiltro(){
        
    }

    public FichaProntuarioPacienteFiltro(String evolucaoSessao, Long pacienteId) {
        this.evolucaoSessao = evolucaoSessao;
        this.pacienteId = pacienteId;
    }

    public String getEvolucaoSessao() {
        return evolucaoSessao;
    }

    public void setEvolucaoSessao(String evolucaoSessao) {
        this.evolucaoSessao = evolucaoSessao;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

   
  
    
    
}
