package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "resposta_anamnese")
public class RespostaAnamnese extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formulario_id", nullable = false)
    private FormularioAnamnese formulario;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "conteudo_respostas", columnDefinition = "TEXT")
    private String conteudoRespostas;

    public RespostaAnamnese() {
    }

    public RespostaAnamnese(FormularioAnamnese formulario, Paciente paciente, String conteudoRespostas) {
        this.formulario = formulario;
        this.paciente = paciente;
        this.conteudoRespostas = conteudoRespostas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FormularioAnamnese getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioAnamnese formulario) {
        this.formulario = formulario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getConteudoRespostas() {
        return conteudoRespostas;
    }

    public void setConteudoRespostas(String conteudoRespostas) {
        this.conteudoRespostas = conteudoRespostas;
    }
}
