package com.mycompany.sistemaintegramind.SegurancaLicenca;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "licenca")
public class Licenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2000)
    private String token;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false)
    private LocalDate dataAtivacao;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Long clinicaId;

    @Column(nullable = false)
    private Long pagamentoId;

    public Licenca() {
    }

    public Licenca(Long id, String token, LocalDate dataInicio, LocalDate dataVencimento, LocalDate dataAtivacao, String status, Long clinicaId, Long pagamentoId) {
        this.id = id;
        this.token = token;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.dataAtivacao = dataAtivacao;
        this.status = status;
        this.clinicaId = clinicaId;
        this.pagamentoId = pagamentoId;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(LocalDate dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getClinicaId() {
        return clinicaId;
    }

    public void setClinicaId(Long clinicaId) {
        this.clinicaId = clinicaId;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }
    
    
}
