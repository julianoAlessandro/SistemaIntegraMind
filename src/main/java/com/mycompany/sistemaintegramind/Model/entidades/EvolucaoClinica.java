package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoAtendimento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoNotaClinica;
import com.mycompany.sistemaintegramind.util.Utilitarios.StatusAgendamento;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;

@Entity
@Table(name = "evolucao_clinica")
public class EvolucaoClinica extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String evolucaoSessao;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_nota_clinica")
    private TipoNotaClinica tiponotaclinica;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_nota_clinica")
    private StatusNotaClinica statusnotaclinica;

    @Column(name = "data_evolucao", nullable = false)
    private LocalDate dataEvolucao;

    @Column(name = "registro_horario_evolucao", nullable = false)
    private LocalTime horario;

    @OneToOne(mappedBy = "notaclinica")
    private InativarNotaClinica inativacao;

    @OneToMany(mappedBy = "notaOriginal")
    private List<RetificarNotaClinica> retificacoesComoNotaOriginal = new ArrayList<>();

    @OneToMany(mappedBy = "notaRetificadora")
    private List<RetificarNotaClinica> retificacoesComoNotaRetificadora = new ArrayList<>();

    public EvolucaoClinica() {

    }

    public EvolucaoClinica(Long id, String evolucaoSessao, String observacoes, Paciente paciente, TipoNotaClinica tiponotaclinica, LocalDate dataEvolucao, LocalTime horario, StatusNotaClinica statusnotaclinica) {
        this.id = id;
        this.evolucaoSessao = evolucaoSessao;
        this.observacoes = observacoes;
        this.paciente = paciente;
        this.tiponotaclinica = tiponotaclinica;
        this.dataEvolucao = dataEvolucao;
        this.horario = horario;
        this.statusnotaclinica = statusnotaclinica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvolucaoSessao() {
        return evolucaoSessao;
    }

    public void setEvolucaoSessao(String evolucaoSessao) {
        this.evolucaoSessao = evolucaoSessao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getDataEvolucao() {
        return dataEvolucao;
    }

    public void setDataEvolucao(LocalDate dataEvolucao) {
        this.dataEvolucao = dataEvolucao;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public TipoNotaClinica getTiponotaclinica() {
        return tiponotaclinica;
    }

    public void setTiponotaclinica(TipoNotaClinica tiponotaclinica) {
        this.tiponotaclinica = tiponotaclinica;
    }

    public StatusNotaClinica getStatusnotaclinica() {
        return statusnotaclinica;
    }

    public void setStatusnotaclinica(StatusNotaClinica statusnotaclinica) {
        this.statusnotaclinica = statusnotaclinica;
    }

    public InativarNotaClinica getInativacao() {
        return inativacao;
    }

    public void setInativacao(InativarNotaClinica inativacao) {
        this.inativacao = inativacao;
    }

    public List<RetificarNotaClinica> getRetificacoesComoNotaOriginal() {
        return retificacoesComoNotaOriginal;
    }

    public void setRetificacoesComoNotaOriginal(
            List<RetificarNotaClinica> retificacoesComoNotaOriginal) {
        this.retificacoesComoNotaOriginal = retificacoesComoNotaOriginal;
    }

    public List<RetificarNotaClinica> getRetificacoesComoNotaRetificadora() {
        return retificacoesComoNotaRetificadora;
    }

    public void setRetificacoesComoNotaRetificadora(
            List<RetificarNotaClinica> retificacoesComoNotaRetificadora) {
        this.retificacoesComoNotaRetificadora = retificacoesComoNotaRetificadora;
    }

}
