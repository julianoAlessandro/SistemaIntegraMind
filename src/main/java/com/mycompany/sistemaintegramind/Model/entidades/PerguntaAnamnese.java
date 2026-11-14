package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoPerguntaAnamnese;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pergunta_anamnese")
public class PerguntaAnamnese extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String SEPARADOR_OPCOES = "\n|||\n";

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

    @Column(name = "opcoes_texto", columnDefinition = "TEXT")
    private String opcoesTexto;

    @Transient
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
        setOpcoes(opcoes);
    }

    private void sincronizarTextoParaLista() {
        if (this.opcoesTexto == null || this.opcoesTexto.isEmpty()) {
            if (this.opcoes == null) {
                this.opcoes = new ArrayList<>();
            }
            return;
        }
        this.opcoes = new ArrayList<>(Arrays.asList(this.opcoesTexto.split(PatternQuote(SEPARADOR_OPCOES), -1)));
    }

    private void sincronizarListaParaTexto() {
        if (this.opcoes == null || this.opcoes.isEmpty()) {
            this.opcoesTexto = "";
        } else {
            this.opcoesTexto = String.join(SEPARADOR_OPCOES, this.opcoes);
        }
    }

    private static String PatternQuote(String s) {
        return java.util.regex.Pattern.quote(s);
    }

    @PostLoad
    protected void aoCarregarDoBanco() {
        sincronizarTextoParaLista();
    }

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        sincronizarListaParaTexto();
    }

    @Override
    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
        sincronizarListaParaTexto();
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
        if ((this.opcoes == null || this.opcoes.isEmpty())
                && this.opcoesTexto != null && !this.opcoesTexto.isEmpty()) {
            sincronizarTextoParaLista();
        }
        if (this.opcoes == null) {
            this.opcoes = new ArrayList<>();
        }
        return opcoes;
    }

    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes != null ? new ArrayList<>(opcoes) : new ArrayList<>();
        sincronizarListaParaTexto();
    }

    public String getOpcoesTexto() {
        sincronizarListaParaTexto();
        return opcoesTexto;
    }

    public void setOpcoesTexto(String opcoesTexto) {
        this.opcoesTexto = opcoesTexto;
        sincronizarTextoParaLista();
    }

    public FormularioAnamnese getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioAnamnese formulario) {
        this.formulario = formulario;
    }
}
