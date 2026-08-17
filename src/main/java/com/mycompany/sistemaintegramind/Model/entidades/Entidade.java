package com.mycompany.sistemaintegramind.Model.entidades;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
public abstract class Entidade {

    @Column(name = "data_criacao", nullable = false, updatable = false)
    protected LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    protected LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        LocalDateTime agora = LocalDateTime.now();
        this.dataCriacao = agora;
        this.dataAtualizacao = agora;
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}


