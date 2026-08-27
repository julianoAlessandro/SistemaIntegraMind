/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.EstadosBrasileiros;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.Sexo;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Micro
 */
@Entity
@Table(name = "Paciente_Responsavel")
public class PacienteResponsavel {

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

    private int idade;

    private String email;

    private String localTrabalhoPai;

    private String localTrabalhoMae;

    private String observacao;

    private String grauParentesco;

    private String naturalidade;

    private String profissao;

    private String motivoEncaminhamento;

    private String encaminhadoPor;

    @ManyToOne
    @JoinColumn(name = "Paciente_id",nullable = false)
    private Pacientes paciente;

}
