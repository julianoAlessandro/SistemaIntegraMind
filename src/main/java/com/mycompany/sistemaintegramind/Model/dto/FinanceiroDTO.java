package com.mycompany.sistemaintegramind.Model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime; //2026-06-16 Juliano:Alterado de java.util.Date para java.time.LocalDateTime
import java.time.ZoneId;
import java.util.Date;

public class FinanceiroDTO {

    private long idVenda; //2026-06-16 Juliano: Alterado de Long para long primitivo conforme o erro exigiu
    private String cliente;
    private LocalDateTime dataVenda; // Alterado para LocalDateTime
    private String produto;
    private BigDecimal valorUnitario;
    private BigDecimal quantidade;
    private BigDecimal subtotal;

    //2026-06-16 Juliano: Construtor com a assinatura exata esperada pelo Hibernate
    public FinanceiroDTO(long idVenda,
            String cliente,
            LocalDateTime dataVenda,
            String produto,
            BigDecimal valorUnitario,
            BigDecimal quantidade,
            BigDecimal subtotal) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.dataVenda = dataVenda;
        this.produto = produto;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(long idVenda) {
        this.idVenda = idVenda;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Date getDataVendaRelatorio() {

        if (dataVenda == null) {
            return null;
        }

        return Date.from(
                dataVenda.atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
