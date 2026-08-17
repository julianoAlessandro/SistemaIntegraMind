package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario", length = 50, nullable = false, unique = true)
    private String usuario;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    //2026-07-18 Guilherme: Relacionamento com a nova tabela de Cargos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    public Usuarios() {
    }

    public Usuarios(String usuario, String senha, Cargo cargo) {
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getPerfil() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}