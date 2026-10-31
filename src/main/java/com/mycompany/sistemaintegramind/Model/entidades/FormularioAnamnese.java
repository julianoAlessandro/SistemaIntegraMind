package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoModeloAnamnese;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "formulario_anamnese")
public class FormularioAnamnese extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "data_abertura")
    private LocalDate dataAbertura = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_modelo", nullable = false)
    private TipoModeloAnamnese tipoModelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_formulario")
    private StatusPacienteAgendamentoDocumento statusFormulario = StatusPacienteAgendamentoDocumento.ATIVO;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("ordem ASC")
    private List<PerguntaAnamnese> perguntas = new ArrayList<>();

    public FormularioAnamnese() {
    }

    public FormularioAnamnese(Long id, String titulo, TipoModeloAnamnese tipoModelo) {
        this.id = id;
        this.titulo = titulo;
        this.tipoModelo = tipoModelo;
        this.dataAbertura = LocalDate.now();
        this.statusFormulario = StatusPacienteAgendamentoDocumento.ATIVO;
    }

    public void adicionarPergunta(PerguntaAnamnese pergunta) {
        if (pergunta != null) {
            pergunta.setFormulario(this);
            if (pergunta.getOrdem() == null) {
                pergunta.setOrdem(this.perguntas.size() + 1);
            }
            this.perguntas.add(pergunta);
        }
    }

    public void removerPergunta(PerguntaAnamnese pergunta) {
        if (pergunta != null) {
            this.perguntas.remove(pergunta);
            pergunta.setFormulario(null);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public TipoModeloAnamnese getTipoModelo() {
        return tipoModelo;
    }

    public void setTipoModelo(TipoModeloAnamnese tipoModelo) {
        this.tipoModelo = tipoModelo;
    }

    public StatusPacienteAgendamentoDocumento getStatusFormulario() {
        return statusFormulario;
    }

    public void setStatusFormulario(StatusPacienteAgendamentoDocumento statusFormulario) {
        this.statusFormulario = statusFormulario;
    }

    public List<PerguntaAnamnese> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<PerguntaAnamnese> perguntas) {
        this.perguntas.clear();
        if (perguntas != null) {
            for (PerguntaAnamnese p : perguntas) {
                adicionarPergunta(p);
            }
        }
    }

    @Override
    public String toString() {
        return titulo != null && !titulo.isBlank() ? titulo : "Sem título";
    }
}
