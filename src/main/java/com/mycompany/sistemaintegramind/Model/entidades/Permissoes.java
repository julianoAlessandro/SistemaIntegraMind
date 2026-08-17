package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "permissao")
public class Permissoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;

    public Permissoes() {
    }

    public Permissoes(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}