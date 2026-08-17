/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.EstadosBrasileiros;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.Sexo;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPaciente;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Juliano
 */
@Entity
@Table(name = "Pacientes")
public class Pacientes extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //2026-01-13 Juliano: alteração do tipo int para Long para não haver perdar durante o salvamento no banco de dados

    @Column(length = 240)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private Sexo sexo;

    @Column(length = 14, nullable = true)
    private String cpf;

    private LocalDate dataNascimento;


    private String telefoneCelular;

    private String telefoneComercial;

    private String telefoneFixo;

    private String email;

    private String cep;

    private String bairro;

    private String rua;

    private String numero;

    private String complemento;

    private String cidade;

    @Enumerated(EnumType.STRING)
    private EstadosBrasileiros estado;
    
    
    @Enumerated(EnumType.STRING)
    private StatusPaciente statuspaciente;

    public Pacientes() {

    }

    public Pacientes(String nome, Sexo sexo, String cpf, LocalDate dataNascimento, String nomeFantasia, String cnpj, String telefoneCelular, String telefoneComercial, String telefoneFixo, String email, String cep, String bairro, String rua, String numero, String complemento, String cidade, EstadosBrasileiros estado, StatusPaciente statuspaciente) {
        this.nome = nome;
        this.sexo = sexo;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
        this.telefoneFixo = telefoneFixo;
        this.email = email;
        this.cep = cep;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.statuspaciente = statuspaciente;
        
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public EstadosBrasileiros getEstado() {
        return estado;
    }

    public void setEstado(EstadosBrasileiros estado) {
        this.estado = estado;
    }

    public StatusPaciente getStatuspaciente() {
        return statuspaciente;
    }

    public void setStatuspaciente(StatusPaciente statuspaciente) {
        this.statuspaciente = statuspaciente;
    }

    
    

}
