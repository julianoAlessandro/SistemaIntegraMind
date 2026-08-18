/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.Sexo;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPaciente;

/**
 *
 * @author Juliano
 */
public class PacienteFiltro {

    private Long id;
    private String nome;
    private Sexo sexo;
    private StatusPaciente statuspaciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public StatusPaciente getStatuspaciente() {
        return statuspaciente;
    }

    public void setStatuspaciente(StatusPaciente statuspaciente) {
        this.statuspaciente = statuspaciente;
    }
    
    
    
}
