package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "cargo")
public class Cargo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;

    // Carrega as permissões automaticamente ao carregar o Cargo (FetchType.EAGER)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cargo_permissao",
            joinColumns = @JoinColumn(name = "cargo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private List<Permissoes> permissoes;

    public Cargo() {
    }

    public Cargo(String nome, List<Permissoes> permissoes) {
        this.nome = nome;
        this.permissoes = permissoes;
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

    public List<Permissoes> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissoes> permissoes) {
        this.permissoes = permissoes;
    }

    @Override
    public String toString() {
        return this.nome != null ? this.nome : "";
    }
}
