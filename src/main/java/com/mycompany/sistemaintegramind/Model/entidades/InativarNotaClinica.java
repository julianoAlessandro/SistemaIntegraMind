
package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inativacao_nota_clinica")
public class InativarNotaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String motivoInativarNota;

    @Column(name = "data_inativacao", nullable = false)
    private LocalDate dataInativarNota;

    @Column(name = "horario_inativacao", nullable = false)
    private LocalTime horario;

    @OneToOne
    @JoinColumn(name = "nota_clinica_id", nullable = false, unique = true)
    private EvolucaoClinica notaclinica;

    public InativarNotaClinica(){
        
    }
    public InativarNotaClinica(Long id, String motivoInativarNota, LocalDate dataInativarNota, LocalTime horario, EvolucaoClinica notaclinica) {
        this.id = id;
        this.motivoInativarNota = motivoInativarNota;
        this.dataInativarNota = dataInativarNota;
        this.horario = horario;
        this.notaclinica = notaclinica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivoInativarNota() {
        return motivoInativarNota;
    }

    public void setMotivoInativarNota(String motivoInativarNota) {
        this.motivoInativarNota = motivoInativarNota;
    }

    public LocalDate getDataInativarNota() {
        return dataInativarNota;
    }

    public void setDataInativarNota(LocalDate dataInativarNota) {
        this.dataInativarNota = dataInativarNota;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public EvolucaoClinica getNotaclinica() {
        return notaclinica;
    }

    public void setNotaclinica(EvolucaoClinica notaclinica) {
        this.notaclinica = notaclinica;
    }
    
    

}
