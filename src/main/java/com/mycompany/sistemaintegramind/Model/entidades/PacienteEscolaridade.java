/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.EstadosBrasileiros;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPeriodoEscolar;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoEscola;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Juliano
 *
 */
@Entity
@Table(name = "Paciente_Escolaridade")
public class PacienteEscolaridade extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Série", length = 240)
    private String serie;

    @Column(name = "Escola")
    private String nomeEscola;

    @Column(name = "CEP")
    private String cepEscolar;

    @Column(name = "Bairro")
    private String bairroEscolar;

    @Column(name = "Rua")
    private String ruaEscolar;

    @Column(name = "Número")
    private String numeroEscolar;

    @Column(name = "Cidade")
    private String cidadeEscolar;

    @Column(name = "Estado")
    @Enumerated(EnumType.STRING)
    private EstadosBrasileiros estado;

   
    @Enumerated(EnumType.STRING)
    @Column(name = "Período",length = 50, nullable = false)
    private StatusPeriodoEscolar periodoescolar;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Escola",length = 50, nullable = false)
    private TipoEscola tipoescola;
    
    @Column(name = "Telefone")
    private String telefoneCelular;

    @OneToOne
    @JoinColumn(name = "Paciente_id")
    private Pacientes paciente;

    public PacienteEscolaridade(Long id, String serie, String nomeEscola, String cepEscolar, String bairroEscolar, String ruaEscolar, String numeroEscolar, String cidadeEscolar, EstadosBrasileiros estado, StatusPeriodoEscolar periodoescolar, TipoEscola tipoescola, String telefoneCelular, Pacientes paciente) {
        this.id = id;
        this.serie = serie;
        this.nomeEscola = nomeEscola;
        this.cepEscolar = cepEscolar;
        this.bairroEscolar = bairroEscolar;
        this.ruaEscolar = ruaEscolar;
        this.numeroEscolar = numeroEscolar;
        this.cidadeEscolar = cidadeEscolar;
        this.estado = estado;
        this.periodoescolar = periodoescolar;
        this.tipoescola = tipoescola;
        this.telefoneCelular = telefoneCelular;
        this.paciente = paciente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNomeEscola() {
        return nomeEscola;
    }

    public void setNomeEscola(String nomeEscola) {
        this.nomeEscola = nomeEscola;
    }

    public String getCepEscolar() {
        return cepEscolar;
    }

    public void setCepEscolar(String cepEscolar) {
        this.cepEscolar = cepEscolar;
    }

    public String getBairroEscolar() {
        return bairroEscolar;
    }

    public void setBairroEscolar(String bairroEscolar) {
        this.bairroEscolar = bairroEscolar;
    }

    public String getRuaEscolar() {
        return ruaEscolar;
    }

    public void setRuaEscolar(String ruaEscolar) {
        this.ruaEscolar = ruaEscolar;
    }

    public String getNumeroEscolar() {
        return numeroEscolar;
    }

    public void setNumeroEscolar(String numeroEscolar) {
        this.numeroEscolar = numeroEscolar;
    }

    public String getCidadeEscolar() {
        return cidadeEscolar;
    }

    public void setCidadeEscolar(String cidadeEscolar) {
        this.cidadeEscolar = cidadeEscolar;
    }

    public EstadosBrasileiros getEstado() {
        return estado;
    }

    public void setEstado(EstadosBrasileiros estado) {
        this.estado = estado;
    }

    public StatusPeriodoEscolar getPeriodoescolar() {
        return periodoescolar;
    }

    public void setPeriodoescolar(StatusPeriodoEscolar periodoescolar) {
        this.periodoescolar = periodoescolar;
    }

    public TipoEscola getTipoescola() {
        return tipoescola;
    }

    public void setTipoescola(TipoEscola tipoescola) {
        this.tipoescola = tipoescola;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public Pacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }

}
