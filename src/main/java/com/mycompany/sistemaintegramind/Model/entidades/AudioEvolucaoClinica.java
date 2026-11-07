package com.mycompany.sistemaintegramind.Model.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Audio_Evolucao_Clinica")
public class AudioEvolucaoClinica {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_Arquivo")
    private String nomeArquivo;

    @Column(name = "caminho_do_arquivo")
    private String caminhoArquivo;

    private String formato;

    @Column(name = "data_gravaçao")
    private LocalDateTime dataGravacao;

    @OneToOne
    @JoinColumn(name = "evolucao_clinica_id", unique = true, nullable = false)
    private EvolucaoClinica evolucaoClinica;

    public AudioEvolucaoClinica() {

    }

    public AudioEvolucaoClinica(Long id, String nomeArquivo, String caminhoArquivo, String formato, LocalDateTime dataGravacao, EvolucaoClinica evolucaoclinica) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.caminhoArquivo = caminhoArquivo;
        this.formato = formato;
        this.dataGravacao = dataGravacao;
        this.evolucaoClinica = evolucaoclinica;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public LocalDateTime getDataGravacao() {
        return dataGravacao;
    }

    public void setDataGravacao(LocalDateTime dataGravacao) {
        this.dataGravacao = dataGravacao;
    }

    public EvolucaoClinica getEvolucaoclinica() {
        return evolucaoClinica;
    }

    public void setEvolucaoclinica(EvolucaoClinica evolucaoClinica) {
        this.evolucaoClinica = evolucaoClinica;
    }

}
