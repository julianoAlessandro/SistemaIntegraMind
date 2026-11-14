package com.mycompany.sistemaintegramind.Model.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "resposta_anamnese")
public class RespostaAnamnese extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "formulario_id", nullable = false)
    private FormularioAnamnese formulario;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "conteudo_respostas", columnDefinition = "TEXT")
    private String conteudoRespostas;

    @Column(name = "dados_campos", columnDefinition = "TEXT")
    private String dadosCampos;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa = Boolean.TRUE;

    @Column(name = "data_hora_desativacao")
    private LocalDateTime dataHoraDesativacao;

    @Column(name = "chave_paciente")
    private String chavePaciente;

    public RespostaAnamnese() {
    }

    public RespostaAnamnese(
            FormularioAnamnese formulario,
            Paciente paciente,
            LocalDate dataAbertura,
            String conteudoRespostas,
            String dadosCampos,
            Boolean ativa) {
        this.formulario = formulario;
        this.paciente = paciente;
        this.dataAbertura = dataAbertura != null ? dataAbertura : LocalDate.now();
        this.conteudoRespostas = conteudoRespostas;
        this.dadosCampos = dadosCampos;
        this.ativa = ativa != null ? ativa : Boolean.TRUE;
    }

    public RespostaAnamnese(FormularioAnamnese formulario, Paciente paciente, LocalDate dataAbertura, String conteudoRespostas) {
        this(formulario, paciente, dataAbertura, conteudoRespostas, null, Boolean.TRUE);
    }

    public RespostaAnamnese(FormularioAnamnese formulario, Paciente paciente, String conteudoRespostas) {
        this(formulario, paciente, LocalDate.now(), conteudoRespostas, null, Boolean.TRUE);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public FormularioAnamnese getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioAnamnese formulario) {
        this.formulario = formulario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getConteudoRespostas() {
        return conteudoRespostas;
    }

    public void setConteudoRespostas(String conteudoRespostas) {
        this.conteudoRespostas = conteudoRespostas;
    }

    public String getDadosCampos() {
        return dadosCampos;
    }

    public void setDadosCampos(String dadosCampos) {
        this.dadosCampos = dadosCampos;
    }

    public Boolean getAtiva() {
        return ativa != null ? ativa : Boolean.TRUE;
    }

    public boolean isAtiva() {
        return ativa == null || Boolean.TRUE.equals(ativa);
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa != null ? ativa : Boolean.FALSE;
    }

    public LocalDateTime getDataHoraDesativacao() {
        return dataHoraDesativacao;
    }

    public void setDataHoraDesativacao(LocalDateTime dataHoraDesativacao) {
        this.dataHoraDesativacao = dataHoraDesativacao;
    }

    public String getChavePaciente() {
        return chavePaciente;
    }

    public void setChavePaciente(String chavePaciente) {
        this.chavePaciente = chavePaciente;
    }

    @Override
    public String toString() {
        String titulo = (formulario != null && formulario.getTitulo() != null && !formulario.getTitulo().isBlank())
                ? formulario.getTitulo()
                : "Anamnese";
        if (!isAtiva() && dataHoraDesativacao != null) {
            return titulo + " - Desativada em " + dataHoraDesativacao.format(FORMATO_DATA_HORA);
        }
        String data = (dataAbertura != null) ? dataAbertura.format(FORMATO_DATA) : "";
        return data.isEmpty() ? titulo : (titulo + " (" + data + ")");
    }
}
