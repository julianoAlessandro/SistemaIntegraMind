/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

public class FichaDocumentosFiltro {

    private String nomeArquivo;

    private Long pacienteId;

    public FichaDocumentosFiltro() {

    }

    public FichaDocumentosFiltro(String nomeArquivo, Long pacienteId) {
        this.nomeArquivo = nomeArquivo;
        this.pacienteId = pacienteId;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
 
    
}
