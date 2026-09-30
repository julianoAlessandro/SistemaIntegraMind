package com.mycompany.sistemaintegramind.Model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime; 
import java.time.ZoneId;
import java.util.Date;

public class FinanceiroDTO {

    private long codigoPaciente; 
    private String nomePaciente;
    private LocalDateTime dataPagamento; 
    private BigDecimal consultaValor;
   
    

    public FinanceiroDTO(long codigoPaciente, String nomePaciente, LocalDateTime dataPagamento, BigDecimal consultaValor) {
        this.codigoPaciente = codigoPaciente;
        this.nomePaciente = nomePaciente;
        this.dataPagamento = dataPagamento;
        this.consultaValor = consultaValor;
    }

    public long getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(long codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getConsultaValor() {
        return consultaValor;
    }

    public void setConsultaValor(BigDecimal consultaValor) {
        this.consultaValor = consultaValor;
    }
 
       
    public Date getDataVendaRelatorio() {

        if (dataPagamento == null) {
            return null;
        }

        return Date.from(dataPagamento.atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
