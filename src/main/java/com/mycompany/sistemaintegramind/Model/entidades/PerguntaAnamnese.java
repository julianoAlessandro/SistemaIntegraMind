package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoPerguntaAnamnese;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "pergunta_anamnese")
public class PerguntaAnamnese extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enunciado", nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(name = "ordem")
    private Integer ordem;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pergunta", nullable = false)
    private TipoPerguntaAnamnese tipoPergunta = TipoPerguntaAnamnese.MULTIPLA_ESCOLHA;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pergunta_anamnese_opcoes", joinColumns = @JoinColumn(name = "pergunta_id"))
    @OrderColumn(name = "indice_opcao")
    @Column(name = "texto_opcao")
    private List<String> opcoes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", nullable = false)
    private FormularioAnamnese formulario;

    public PerguntaAnamnese() {
    }

    public PerguntaAnamnese(String enunciado, Integer ordem, TipoPerguntaAnamnese tipoPergunta, List<String> opcoes) {
        this.enunciado = enunciado;
        this.ordem = ordem;
        this.tipoPergunta = tipoPergunta;
        if (opcoes != null) {
            this.opcoes = new ArrayList<>(opcoes);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public TipoPerguntaAnamnese getTipoPergunta() {
        return tipoPergunta;
    }

    public void setTipoPergunta(TipoPerguntaAnamnese tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes != null ? new ArrayList<>(opcoes) : new ArrayList<>();
    }

    public FormularioAnamnese getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioAnamnese formulario) {
        this.formulario = formulario;
    }
}
