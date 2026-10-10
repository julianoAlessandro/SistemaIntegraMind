/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoDocumentoPaciente;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@Entity
@Table(name = "Documento")
public class Documento extends Entidade {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "caminho_arquivo")
    private String caminhoArquivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumentoPaciente tipoDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_documento")
    private StatusPacienteAgendamentoDocumento statusdocumento;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    public Documento() {

    }

    public Documento(Long id, String nomeArquivo, String caminhoArquivo, TipoDocumentoPaciente tipoDocumento, Paciente paciente) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.caminhoArquivo = caminhoArquivo;
        this.tipoDocumento = tipoDocumento;
        this.paciente = paciente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public TipoDocumentoPaciente getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoPaciente tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public StatusPacienteAgendamentoDocumento getStatusdocumento() {
        return statusdocumento;
    }

    public void setStatusdocumento(StatusPacienteAgendamentoDocumento statusdocumento) {
        this.statusdocumento = statusdocumento;
    }

    
}
