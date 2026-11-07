package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.AnamneseDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoModeloAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoPerguntaAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.FormularioAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.PerguntaAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.RespostaAnamnese;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AnamneseJPA implements AnamneseDAO {

    private static final File ARQUIVO_FORMULARIOS_DISCO = new File(
            System.getProperty("user.home"), ".integramind_formularios_anamnese.properties"
    );
    private static final File ARQUIVO_RESPOSTAS_DISCO = new File(
            System.getProperty("user.home"), ".integramind_respostas_anamnese.properties"
    );
    private static final List<FormularioAnamnese> MEMORIA_FORMULARIOS = new ArrayList<>();
    private static final List<RespostaAnamnese> MEMORIA_RESPOSTAS = new ArrayList<>();
    private static long proximoIdMemoria = 100L;
    private static long proximoIdRespostaMemoria = 500L;

    static {
        inicializarModelosPadraoMemoria();
        carregarFormulariosPersonalizadosDoDisco();
        carregarRespostasDoDisco();
    }

    private static void inicializarModelosPadraoMemoria() {
        if (!MEMORIA_FORMULARIOS.isEmpty()) {
            return;
        }
        FormularioAnamnese infantil = criarTemplatePadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
        infantil.setId(1L);
        MEMORIA_FORMULARIOS.add(infantil);

        FormularioAnamnese adulto = criarTemplatePadrao(TipoModeloAnamnese.ADULTO);
        adulto.setId(2L);
        MEMORIA_FORMULARIOS.add(adulto);
    }

    public static String gerarChavePaciente(Paciente paciente) {
        if (paciente == null) {
            return "PAC_GERAL";
        }
        if (paciente.getId() != null) {
            return "PAC_ID_" + paciente.getId();
        }
        if (paciente.getCpf() != null && !paciente.getCpf().isBlank()) {
            return "PAC_CPF_" + normalizarTextoChave(paciente.getCpf());
        }
        if (paciente.getNome() != null && !paciente.getNome().isBlank()) {
            return "PAC_NOME_" + normalizarTextoChave(paciente.getNome());
        }
        return "PAC_GERAL";
    }

    private static String normalizarTextoChave(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toLowerCase().replaceAll("[^a-z0-9à-ú]+", "_");
    }

    private static boolean respostaPertenceAoPaciente(RespostaAnamnese r, Paciente paciente) {
        if (r == null) {
            return false;
        }
        String chaveAlvo = gerarChavePaciente(paciente);
        if (r.getChavePaciente() != null && r.getChavePaciente().equals(chaveAlvo)) {
            return true;
        }
        Paciente rp = r.getPaciente();
        if (paciente == null) {
            return rp == null || "PAC_GERAL".equals(r.getChavePaciente());
        }
        if (rp != null) {
            if (paciente.getId() != null && rp.getId() != null && paciente.getId().equals(rp.getId())) {
                return true;
            }
            if (paciente.getCpf() != null && !paciente.getCpf().isBlank()
                    && rp.getCpf() != null && paciente.getCpf().trim().equalsIgnoreCase(rp.getCpf().trim())) {
                return true;
            }
            if (paciente.getNome() != null && !paciente.getNome().isBlank()
                    && rp.getNome() != null && paciente.getNome().trim().equalsIgnoreCase(rp.getNome().trim())) {
                return true;
            }
        }
        return false;
    }

    private static synchronized void carregarFormulariosPersonalizadosDoDisco() {
        if (!ARQUIVO_FORMULARIOS_DISCO.exists()) {
            return;
        }
        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(ARQUIVO_FORMULARIOS_DISCO), StandardCharsets.UTF_8)) {
            props.load(reader);
            int qtd = Integer.parseInt(props.getProperty("form.count", "0"));
            long maiorId = proximoIdMemoria;

            for (int i = 0; i < qtd; i++) {
                String pref = "form." + i + ".";
                Long id = Long.parseLong(props.getProperty(pref + "id", String.valueOf(proximoIdMemoria++)));
                if (id >= maiorId) {
                    maiorId = id + 1;
                }
                String titulo = props.getProperty(pref + "titulo", "Personalizado");
                String dataStr = props.getProperty(pref + "dataAbertura", LocalDate.now().toString());
                LocalDate dataAb;
                try {
                    dataAb = LocalDate.parse(dataStr);
                } catch (Exception ex) {
                    dataAb = LocalDate.now();
                }
                String tipoStr = props.getProperty(pref + "tipoModelo", TipoModeloAnamnese.PERSONALIZADO.name());
                TipoModeloAnamnese tipoMod;
                try {
                    tipoMod = TipoModeloAnamnese.valueOf(tipoStr);
                } catch (Exception ex) {
                    tipoMod = TipoModeloAnamnese.PERSONALIZADO;
                }
                String statusStr = props.getProperty(pref + "status", StatusPacienteAgendamentoDocumento.ATIVO.name());
                StatusPacienteAgendamentoDocumento statusMod;
                try {
                    statusMod = StatusPacienteAgendamentoDocumento.valueOf(statusStr);
                } catch (Exception ex) {
                    statusMod = StatusPacienteAgendamentoDocumento.ATIVO;
                }

                FormularioAnamnese form = new FormularioAnamnese();
                form.setId(id);
                form.setTitulo(titulo);
                form.setDataAbertura(dataAb);
                form.setTipoModelo(tipoMod);
                form.setStatusFormulario(statusMod);

                int pCount = Integer.parseInt(props.getProperty(pref + "perguntas.count", "0"));
                for (int j = 0; j < pCount; j++) {
                    String pPref = pref + "p." + j + ".";
                    String enunciado = props.getProperty(pPref + "enunciado", "Pergunta");
                    int ordem = Integer.parseInt(props.getProperty(pPref + "ordem", String.valueOf(j + 1)));
                    String tPergStr = props.getProperty(pPref + "tipo", TipoPerguntaAnamnese.TEXTO_LIVRE.name());
                    TipoPerguntaAnamnese tPerg;
                    try {
                        tPerg = TipoPerguntaAnamnese.valueOf(tPergStr);
                    } catch (Exception ex) {
                        tPerg = TipoPerguntaAnamnese.TEXTO_LIVRE;
                    }
                    int opCount = Integer.parseInt(props.getProperty(pPref + "opcoes.count", "0"));
                    List<String> opcoes = new ArrayList<>();
                    for (int k = 0; k < opCount; k++) {
                        opcoes.add(props.getProperty(pPref + "op." + k, ""));
                    }
                    form.adicionarPergunta(new PerguntaAnamnese(enunciado, ordem, tPerg, opcoes));
                }

                sincronizarMemoriaInterno(form);
            }
            proximoIdMemoria = Math.max(proximoIdMemoria, maiorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void salvarFormulariosPersonalizadosNoDisco() {
        Properties props = new Properties();
        List<FormularioAnamnese> personalizados = new ArrayList<>();
        for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
            if (f.getTipoModelo() == TipoModeloAnamnese.PERSONALIZADO
                    && f.getStatusFormulario() == StatusPacienteAgendamentoDocumento.ATIVO) {
                personalizados.add(f);
            }
        }
        props.setProperty("form.count", String.valueOf(personalizados.size()));
        for (int i = 0; i < personalizados.size(); i++) {
            FormularioAnamnese f = personalizados.get(i);
            String pref = "form." + i + ".";
            props.setProperty(pref + "id", String.valueOf(f.getId() != null ? f.getId() : (proximoIdMemoria++)));
            props.setProperty(pref + "titulo", f.getTitulo() != null ? f.getTitulo() : "Personalizado");
            props.setProperty(pref + "dataAbertura", (f.getDataAbertura() != null ? f.getDataAbertura() : LocalDate.now()).toString());
            props.setProperty(pref + "tipoModelo", (f.getTipoModelo() != null ? f.getTipoModelo() : TipoModeloAnamnese.PERSONALIZADO).name());
            props.setProperty(pref + "status", (f.getStatusFormulario() != null ? f.getStatusFormulario() : StatusPacienteAgendamentoDocumento.ATIVO).name());

            List<PerguntaAnamnese> perguntas = f.getPerguntas();
            props.setProperty(pref + "perguntas.count", String.valueOf(perguntas != null ? perguntas.size() : 0));
            if (perguntas != null) {
                for (int j = 0; j < perguntas.size(); j++) {
                    PerguntaAnamnese p = perguntas.get(j);
                    String pPref = pref + "p." + j + ".";
                    props.setProperty(pPref + "enunciado", p.getEnunciado() != null ? p.getEnunciado() : "");
                    props.setProperty(pPref + "ordem", String.valueOf(p.getOrdem() != null ? p.getOrdem() : (j + 1)));
                    props.setProperty(pPref + "tipo", (p.getTipoPergunta() != null ? p.getTipoPergunta() : TipoPerguntaAnamnese.TEXTO_LIVRE).name());
                    List<String> ops = p.getOpcoes();
                    props.setProperty(pPref + "opcoes.count", String.valueOf(ops != null ? ops.size() : 0));
                    if (ops != null) {
                        for (int k = 0; k < ops.size(); k++) {
                            props.setProperty(pPref + "op." + k, ops.get(k) != null ? ops.get(k) : "");
                        }
                    }
                }
            }
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(ARQUIVO_FORMULARIOS_DISCO), StandardCharsets.UTF_8)) {
            props.store(writer, "Formularios Personalizados de Anamnese - SistemaIntegraMind");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void carregarRespostasDoDisco() {
        if (!ARQUIVO_RESPOSTAS_DISCO.exists()) {
            return;
        }
        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(ARQUIVO_RESPOSTAS_DISCO), StandardCharsets.UTF_8)) {
            props.load(reader);
            int qtd = Integer.parseInt(props.getProperty("resp.count", "0"));
            long maiorId = proximoIdRespostaMemoria;

            for (int i = 0; i < qtd; i++) {
                String pref = "resp." + i + ".";
                Long id = Long.parseLong(props.getProperty(pref + "id", String.valueOf(proximoIdRespostaMemoria++)));
                if (id >= maiorId) {
                    maiorId = id + 1;
                }
                LocalDate dataAb;
                try {
                    dataAb = LocalDate.parse(props.getProperty(pref + "dataAbertura", LocalDate.now().toString()));
                } catch (Exception ex) {
                    dataAb = LocalDate.now();
                }
                boolean ativa = Boolean.parseBoolean(props.getProperty(pref + "ativa", "true"));
                LocalDateTime dataHoraDesat = null;
                String desatStr = props.getProperty(pref + "dataHoraDesativacao", "");
                if (!desatStr.isBlank()) {
                    try {
                        dataHoraDesat = LocalDateTime.parse(desatStr);
                    } catch (Exception ignored) {
                    }
                }
                if (!ativa && dataHoraDesat == null) {
                    dataHoraDesat = LocalDateTime.now();
                }

                String chavePac = props.getProperty(pref + "chavePaciente", "PAC_GERAL");
                String conteudo = props.getProperty(pref + "conteudoRespostas", "");
                String dadosCampos = props.getProperty(pref + "dadosCampos", "");

                String formTipoStr = props.getProperty(pref + "form.tipo", TipoModeloAnamnese.INFANTIL_ADOLESCENTE.name());
                TipoModeloAnamnese formTipo;
                try {
                    formTipo = TipoModeloAnamnese.valueOf(formTipoStr);
                } catch (Exception ex) {
                    formTipo = TipoModeloAnamnese.INFANTIL_ADOLESCENTE;
                }
                Long formId = null;
                try {
                    String fIdStr = props.getProperty(pref + "form.id", "");
                    if (!fIdStr.isBlank()) {
                        formId = Long.parseLong(fIdStr);
                    }
                } catch (Exception ignored) {
                }
                String formTitulo = props.getProperty(pref + "form.titulo", "Anamnese");

                // Constrói o snapshot exato do formulário salvo com esta resposta
                FormularioAnamnese formSnapshot = new FormularioAnamnese();
                formSnapshot.setId(formId);
                formSnapshot.setTitulo(formTitulo);
                formSnapshot.setDataAbertura(dataAb);
                formSnapshot.setTipoModelo(formTipo);
                formSnapshot.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

                int pCount = Integer.parseInt(props.getProperty(pref + "form.p.count", "0"));
                for (int j = 0; j < pCount; j++) {
                    String pPref = pref + "form.p." + j + ".";
                    String enunciado = props.getProperty(pPref + "enunciado", "Pergunta");
                    int ordem = Integer.parseInt(props.getProperty(pPref + "ordem", String.valueOf(j + 1)));
                    TipoPerguntaAnamnese tPerg;
                    try {
                        tPerg = TipoPerguntaAnamnese.valueOf(props.getProperty(pPref + "tipo", TipoPerguntaAnamnese.TEXTO_LIVRE.name()));
                    } catch (Exception ex) {
                        tPerg = TipoPerguntaAnamnese.TEXTO_LIVRE;
                    }
                    int opCount = Integer.parseInt(props.getProperty(pPref + "opcoes.count", "0"));
                    List<String> ops = new ArrayList<>();
                    for (int k = 0; k < opCount; k++) {
                        ops.add(props.getProperty(pPref + "op." + k, ""));
                    }
                    formSnapshot.adicionarPergunta(new PerguntaAnamnese(enunciado, ordem, tPerg, ops));
                }

                FormularioAnamnese formAssociado = !formSnapshot.getPerguntas().isEmpty()
                        ? formSnapshot
                        : localizarFormularioEmMemoria(formId, formTipo, formTitulo);

                RespostaAnamnese r = new RespostaAnamnese();
                r.setId(id);
                r.setDataAbertura(dataAb);
                r.setAtiva(ativa);
                r.setDataHoraDesativacao(dataHoraDesat);
                r.setChavePaciente(chavePac);
                r.setConteudoRespostas(conteudo);
                r.setDadosCampos(dadosCampos);
                r.setFormulario(formAssociado);

                sincronizarRespostaMemoriaInterno(r);
            }
            proximoIdRespostaMemoria = Math.max(proximoIdRespostaMemoria, maiorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FormularioAnamnese localizarFormularioEmMemoria(Long formId, TipoModeloAnamnese tipo, String titulo) {
        if (tipo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
                if (f.getTipoModelo() == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
                    return f;
                }
            }
        }
        if (tipo == TipoModeloAnamnese.ADULTO) {
            for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
                if (f.getTipoModelo() == TipoModeloAnamnese.ADULTO) {
                    return f;
                }
            }
        }
        if (formId != null) {
            for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
                if (formId.equals(f.getId())) {
                    return f;
                }
            }
        }
        if (titulo != null && !titulo.isBlank()) {
            for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
                if (f.getTitulo() != null && f.getTitulo().equalsIgnoreCase(titulo)) {
                    return f;
                }
            }
        }
        return null;
    }

    private static synchronized void salvarRespostasNoDisco() {
        Properties props = new Properties();
        props.setProperty("resp.count", String.valueOf(MEMORIA_RESPOSTAS.size()));
        for (int i = 0; i < MEMORIA_RESPOSTAS.size(); i++) {
            RespostaAnamnese r = MEMORIA_RESPOSTAS.get(i);
            String pref = "resp." + i + ".";
            props.setProperty(pref + "id", String.valueOf(r.getId() != null ? r.getId() : (proximoIdRespostaMemoria++)));
            props.setProperty(pref + "dataAbertura", (r.getDataAbertura() != null ? r.getDataAbertura() : LocalDate.now()).toString());
            props.setProperty(pref + "ativa", String.valueOf(r.isAtiva()));
            props.setProperty(pref + "dataHoraDesativacao", r.getDataHoraDesativacao() != null ? r.getDataHoraDesativacao().toString() : "");
            String chavePac = r.getChavePaciente() != null ? r.getChavePaciente() : gerarChavePaciente(r.getPaciente());
            props.setProperty(pref + "chavePaciente", chavePac);
            props.setProperty(pref + "conteudoRespostas", r.getConteudoRespostas() != null ? r.getConteudoRespostas() : "");
            props.setProperty(pref + "dadosCampos", r.getDadosCampos() != null ? r.getDadosCampos() : "");

            FormularioAnamnese f = r.getFormulario();
            if (f != null) {
                props.setProperty(pref + "form.id", f.getId() != null ? String.valueOf(f.getId()) : "");
                props.setProperty(pref + "form.tipo", (f.getTipoModelo() != null ? f.getTipoModelo() : TipoModeloAnamnese.INFANTIL_ADOLESCENTE).name());
                props.setProperty(pref + "form.titulo", f.getTitulo() != null ? f.getTitulo() : "Anamnese");
                List<PerguntaAnamnese> perguntas = f.getPerguntas();
                props.setProperty(pref + "form.p.count", String.valueOf(perguntas != null ? perguntas.size() : 0));
                if (perguntas != null) {
                    for (int j = 0; j < perguntas.size(); j++) {
                        PerguntaAnamnese p = perguntas.get(j);
                        String pPref = pref + "form.p." + j + ".";
                        props.setProperty(pPref + "enunciado", p.getEnunciado() != null ? p.getEnunciado() : "");
                        props.setProperty(pPref + "ordem", String.valueOf(p.getOrdem() != null ? p.getOrdem() : (j + 1)));
                        props.setProperty(pPref + "tipo", (p.getTipoPergunta() != null ? p.getTipoPergunta() : TipoPerguntaAnamnese.TEXTO_LIVRE).name());
                        List<String> ops = p.getOpcoes();
                        props.setProperty(pPref + "opcoes.count", String.valueOf(ops != null ? ops.size() : 0));
                        if (ops != null) {
                            for (int k = 0; k < ops.size(); k++) {
                                props.setProperty(pPref + "op." + k, ops.get(k) != null ? ops.get(k) : "");
                            }
                        }
                    }
                }
            }
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(ARQUIVO_RESPOSTAS_DISCO), StandardCharsets.UTF_8)) {
            props.store(writer, "Respostas Salvas de Anamnese (Ativas e Desativadas) - SistemaIntegraMind");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sincronizarRespostaMemoriaInterno(RespostaAnamnese resposta) {
        if (resposta == null) {
            return;
        }
        if (resposta.getChavePaciente() == null || resposta.getChavePaciente().isBlank()) {
            resposta.setChavePaciente(gerarChavePaciente(resposta.getPaciente()));
        }
        for (int i = 0; i < MEMORIA_RESPOSTAS.size(); i++) {
            RespostaAnamnese existente = MEMORIA_RESPOSTAS.get(i);
            if (existente.getId() != null && existente.getId().equals(resposta.getId())) {
                MEMORIA_RESPOSTAS.set(i, resposta);
                return;
            }
        }
        MEMORIA_RESPOSTAS.add(resposta);
    }

    private static void addPergunta(FormularioAnamnese form, String enunciado, TipoPerguntaAnamnese tipo, String... opcoes) {
        int ordem = form.getPerguntas().size() + 1;
        List<String> listaOpcoes = (opcoes != null && opcoes.length > 0)
                ? new ArrayList<>(Arrays.asList(opcoes))
                : new ArrayList<>();
        form.adicionarPergunta(new PerguntaAnamnese(enunciado, ordem, tipo, listaOpcoes));
    }

    public static FormularioAnamnese criarTemplatePadrao(TipoModeloAnamnese tipo) {
        if (tipo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            FormularioAnamnese form = new FormularioAnamnese();
            form.setTitulo("ANAMNESE – INFANTIL E ADOLESCENTE");
            form.setDataAbertura(LocalDate.now());
            form.setTipoModelo(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
            form.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

            // 1 - Identificação
            addPergunta(form, "1 - Identificação", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Nome da criança e/ou adolescente:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Sexo:", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "masc.", "fem.");
            addPergunta(form, "Data de nascimento e Idade:", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Data de nascimento (dd/mm/aaaa)", "Idade (anos e meses)");
            addPergunta(form, "É filho:", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "natural", "adotivo");
            addPergunta(form, "Telefone:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Trabalho dos pais:", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Trab. Mãe", "Trab. Pai");
            addPergunta(form, "Endereço residencial do cliente (Rua/Av e Bairro):", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Rua/ Av", "Bairro");
            addPergunta(form, "Endereço residencial do cliente (Cidade/UF e CEP):", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Cidade/ UF", "CEP");
            addPergunta(form, "Local de trab. do pai e mãe:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Obs:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Nome do responsável que trouxe para o atendimento:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Dados do responsável:", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Grau de parentesco", "Data nasc.", "Idade", "Naturalidade", "Profissão");
            addPergunta(form, "Quem encaminhou ao atendimento:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Por que?", TipoPerguntaAnamnese.TEXTO_LIVRE);

            // 2 – Dados da Escola
            addPergunta(form, "2 – Dados da Escola", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Série e Grau:", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Série", "do grau");
            addPergunta(form, "Estuda no período:", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                    "manhã", "tarde", "noite", "integral");
            addPergunta(form, "Nome da escola:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Endereço da escola:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Telefones:", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Rede / Tipo da escola:", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "Particular", "Estadual", "Municipal", "Abrigo", "Outro", "COMPLEMENTO:Outro");

            // 3 - Disponibilidade de horários
            addPergunta(form, "3 - Disponibilidade de horários", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Assinale com um X nos dias e períodos do dia disponíveis para vir aos atendimentos:",
                    TipoPerguntaAnamnese.GRADE_DISPONIBILIDADE,
                    "COL:2ª-feira", "COL:3ª-feira", "COL:4ª-feira", "COL:5ª-feira", "COL:6ª-feira",
                    "LIN:Manhã", "LIN:Tarde");
            addPergunta(form, "OBS:", TipoPerguntaAnamnese.TEXTO_LIVRE);

            // 6 - Dados sobre a família
            addPergunta(form, "6 - Dados sobre a família", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Algum dos filhos é adotivo? Qual?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Algum filho mora com outras pessoas?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Porque?");
            addPergunta(form, "Houve algum aborto?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "natural", "provocado", "COMPLEMENTO:Quantos?");
            addPergunta(form, "Morreu algum filho?", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Quando?", "Com que idade?", "Causa", "Qual a idade do paciente na ocasião?");
            addPergunta(form, "Quem mora na casa?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "pai", "mãe", "avó", "avô", "tios", "outros", "COMPLEMENTO:outros");
            addPergunta(form, "Os pais já tiveram tratamento psicológico ou psiquiátrico?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "Pai", "Mãe", "COMPLEMENTO:Quando?");
            addPergunta(form, "A família possui alguma crença religiosa?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "COMPLEMENTO:Qual?", "sim", "não", "COMPLEMENTO:Qual a orientação dada aos filhos a respeito?");

            // COMPORTAMENTOS E HÁBITOS
            addPergunta(form, "COMPORTAMENTOS E HÁBITOS", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Roe unhas?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Desde", "COMPLEMENTO:Razões dadas");
            addPergunta(form, "Morde os lábios?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Desde", "COMPLEMENTO:Razões dadas");
            addPergunta(form, "Puxa os cabelos?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Desde", "COMPLEMENTO:Razões dadas");
            addPergunta(form, "Tem algum tique?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Desde", "COMPLEMENTO:Razões dadas");
            addPergunta(form, "Como os pais reagem a isso?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Prefere brincar:", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sozinho", "acompanhado");
            addPergunta(form, "Que tipo de brincadeira prefere?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Cuida dos brinquedos?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Tem amigos?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Normalmente, estes amigos são da mesma idade?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Faz amizades facilmente?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Lidera nas brincadeiras?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Quem escolhe os amigos?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Como se comporta em festas?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Costuma frequentar a casa dos amigos?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Recebe amigos em casa?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Que tipo de amigos tem? São de conhecimento dos pais?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "É inquieto na classe?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Briga muito com os amigos?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Na escola mantém-se isolado dos colegas?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Foi reprovado alguma vez?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Quando?", "COMPLEMENTO:Por quê?");
            addPergunta(form, "Mudou de escola?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Por quê?");
            addPergunta(form, "Como os pais reagem quando tira notas baixas?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Alguém auxilia a criança nos estudos?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Quem?");

            // Saúde
            addPergunta(form, "Saúde", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "A criança / adolescente tem algum problema físico?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Qual?");
            addPergunta(form, "Tomou ou toma algum medicamento?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Qual?", "COMPLEMENTO:Há quanto tempo?");
            addPergunta(form, "Que doenças teve na infância?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Sofreu algum acidente?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Como foi?");
            addPergunta(form, "Enxerga bem?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Ouve bem?", TipoPerguntaAnamnese.MULTIPLA_ESCOLHA, "sim", "não");
            addPergunta(form, "Fala errado?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "COMPLEMENTO:Que erros comete");
            addPergunta(form, "Já esteve em algum tipo de atendimento especializado?", TipoPerguntaAnamnese.ESCOLHA_COM_COMPLEMENTO,
                    "sim", "não", "Psicológico", "Fonoaudiológico", "Psiquiátrico", "Psicopedagógico", "Outro",
                    "COMPLEMENTO:Outro", "COMPLEMENTO:Por quanto tempo?", "COMPLEMENTO:Porque deixou?");
            addPergunta(form, "Algum outro dado importante sobre a saúde dele / dela?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Quais as doenças que já teve, com que idade e qual a gravidade?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Sofreu algum tipo de operação? Qual? Com que idade?", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Teve convulsão? Com que idade? Descreva a mesma.", TipoPerguntaAnamnese.TEXTO_LIVRE);
            addPergunta(form, "Local, Data e Responsável pela anamnese:", TipoPerguntaAnamnese.CAMPOS_LINHA,
                    "Cidade", "Dia", "Mês", "Ano (20__)", "Responsável pela anamnese");

            return form;
        } else {
            FormularioAnamnese form = new FormularioAnamnese();
            form.setTitulo("Anamnese Adulto");
            form.setDataAbertura(LocalDate.now());
            form.setTipoModelo(TipoModeloAnamnese.ADULTO);
            form.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

            addPergunta(form, "1 - Identificação e Histórico", TipoPerguntaAnamnese.SECAO_TITULO);
            addPergunta(form, "Já realizou acompanhamento psicológico ou psiquiátrico anteriormente?",
                    TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                    "Sim, atualmente em acompanhamento", "Sim, em período anterior", "Não, é o primeiro atendimento");
            addPergunta(form, "Como avalia a qualidade atual do sono, rotina e nível de disposição diária?",
                    TipoPerguntaAnamnese.ESTRELAS);
            addPergunta(form, "Queixa principal, histórico familiar e motivo da busca pelo atendimento:",
                    TipoPerguntaAnamnese.TEXTO_LIVRE);
            return form;
        }
    }

    private EntityManager obterEntityManagerSeguro() {
        try {
            return JPAUtil.getEntityManager();
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public void cadastrarFormulario(FormularioAnamnese formulario) {
        if (formulario.getStatusFormulario() == null) {
            formulario.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);
        }
        if (formulario.getDataAbertura() == null) {
            formulario.setDataAbertura(LocalDate.now());
        }
        int ordem = 1;
        for (PerguntaAnamnese p : formulario.getPerguntas()) {
            p.setFormulario(formulario);
            p.setOrdem(ordem++);
            p.getOpcoesTexto();
        }

        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                em.persist(formulario);
                em.getTransaction().commit();
                sincronizarMemoria(formulario);
                return;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

        if (formulario.getId() == null) {
            formulario.setId(proximoIdMemoria++);
        }
        sincronizarMemoria(formulario);
    }

    @Override
    public void atualizarFormulario(FormularioAnamnese formulario) {
        if (formulario == null) {
            return;
        }
        if (formulario.getId() == null) {
            cadastrarFormulario(formulario);
            return;
        }
        if (formulario.getDataAbertura() == null) {
            formulario.setDataAbertura(LocalDate.now());
        }

        int ordem = 1;
        for (PerguntaAnamnese p : formulario.getPerguntas()) {
            p.setFormulario(formulario);
            p.setOrdem(ordem++);
            p.getOpcoesTexto();
        }

        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                FormularioAnamnese gerenciado = em.find(FormularioAnamnese.class, formulario.getId());
                if (gerenciado != null) {
                    gerenciado.setTitulo(formulario.getTitulo());
                    gerenciado.setDataAbertura(formulario.getDataAbertura());
                    if (formulario.getTipoModelo() != null) {
                        gerenciado.setTipoModelo(formulario.getTipoModelo());
                    }
                    if (formulario.getStatusFormulario() != null) {
                        gerenciado.setStatusFormulario(formulario.getStatusFormulario());
                    }
                    gerenciado.getPerguntas().clear();
                    int ord = 1;
                    for (PerguntaAnamnese p : formulario.getPerguntas()) {
                        PerguntaAnamnese novaPergunta = new PerguntaAnamnese(
                                p.getEnunciado(),
                                ord++,
                                p.getTipoPergunta(),
                                p.getOpcoes()
                        );
                        gerenciado.adicionarPergunta(novaPergunta);
                    }
                    FormularioAnamnese atualizado = em.merge(gerenciado);
                    em.getTransaction().commit();
                    sincronizarMemoria(atualizado);
                    return;
                } else {
                    FormularioAnamnese atualizado = em.merge(formulario);
                    em.getTransaction().commit();
                    sincronizarMemoria(atualizado);
                    return;
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

        sincronizarMemoria(formulario);
    }

    private static void sincronizarMemoriaInterno(FormularioAnamnese formulario) {
        if (formulario == null) {
            return;
        }
        for (int i = 0; i < MEMORIA_FORMULARIOS.size(); i++) {
            FormularioAnamnese existente = MEMORIA_FORMULARIOS.get(i);
            if (existente.getId() != null && existente.getId().equals(formulario.getId())) {
                MEMORIA_FORMULARIOS.set(i, formulario);
                return;
            }
            if (existente.getTipoModelo() == TipoModeloAnamnese.PERSONALIZADO
                    && formulario.getTipoModelo() == TipoModeloAnamnese.PERSONALIZADO
                    && existente.getTitulo() != null
                    && existente.getTitulo().equalsIgnoreCase(formulario.getTitulo())) {
                MEMORIA_FORMULARIOS.set(i, formulario);
                return;
            }
        }
        MEMORIA_FORMULARIOS.add(formulario);
    }

    private void sincronizarMemoria(FormularioAnamnese formulario) {
        sincronizarMemoriaInterno(formulario);
        salvarFormulariosPersonalizadosNoDisco();
    }

    @Override
    public FormularioAnamnese buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                FormularioAnamnese encontrado = em.find(FormularioAnamnese.class, id);
                if (encontrado != null) {
                    return encontrado;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
            if (id.equals(f.getId())) {
                return f;
            }
        }
        return null;
    }

    @Override
    public FormularioAnamnese buscarModeloPadrao(TipoModeloAnamnese tipoModelo) {
        List<FormularioAnamnese> lista = listarFormulariosPorTipo(tipoModelo);
        if (!lista.isEmpty()) {
            FormularioAnamnese existente = lista.get(0);
            if (tipoModelo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE
                    && (existente.getPerguntas() == null || existente.getPerguntas().size() < 20)) {
                FormularioAnamnese completo = criarTemplatePadrao(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
                completo.setId(existente.getId());
                atualizarFormulario(completo);
                return completo;
            }
            return existente;
        }
        FormularioAnamnese padrao = criarTemplatePadrao(tipoModelo);
        cadastrarFormulario(padrao);
        return padrao;
    }

    @Override
    public List<FormularioAnamnese> listarTodosFormularios() {
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<FormularioAnamnese> cq = cb.createQuery(FormularioAnamnese.class);
                Root<FormularioAnamnese> root = cq.from(FormularioAnamnese.class);
                Predicate ativo = cb.equal(root.get("statusFormulario"), StatusPacienteAgendamentoDocumento.ATIVO);
                cq.select(root).where(ativo).distinct(true);
                List<FormularioAnamnese> doBanco = em.createQuery(cq).getResultList();
                if (doBanco != null) {
                    for (FormularioAnamnese fb : doBanco) {
                        sincronizarMemoriaInterno(fb);
                    }
                    salvarFormulariosPersonalizadosNoDisco();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        return new ArrayList<>(MEMORIA_FORMULARIOS);
    }

    @Override
    public List<FormularioAnamnese> listarFormulariosPorTipo(TipoModeloAnamnese tipoModelo) {
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<FormularioAnamnese> cq = cb.createQuery(FormularioAnamnese.class);
                Root<FormularioAnamnese> root = cq.from(FormularioAnamnese.class);

                Predicate ativo = cb.equal(root.get("statusFormulario"), StatusPacienteAgendamentoDocumento.ATIVO);
                Predicate porTipo = cb.equal(root.get("tipoModelo"), tipoModelo);
                cq.select(root).where(ativo, porTipo).distinct(true);

                List<FormularioAnamnese> doBanco = em.createQuery(cq).getResultList();
                if (doBanco != null) {
                    for (FormularioAnamnese fb : doBanco) {
                        sincronizarMemoriaInterno(fb);
                    }
                    if (tipoModelo == TipoModeloAnamnese.PERSONALIZADO && !doBanco.isEmpty()) {
                        salvarFormulariosPersonalizadosNoDisco();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

        List<FormularioAnamnese> filtrados = new ArrayList<>();
        for (FormularioAnamnese f : MEMORIA_FORMULARIOS) {
            if (f.getTipoModelo() == tipoModelo && f.getStatusFormulario() == StatusPacienteAgendamentoDocumento.ATIVO) {
                filtrados.add(f);
            }
        }
        return filtrados;
    }

    @Override
    public void salvarResposta(RespostaAnamnese resposta) {
        if (resposta == null) {
            return;
        }
        if (resposta.getDataAbertura() == null) {
            resposta.setDataAbertura(LocalDate.now());
        }
        if (resposta.getAtiva() == null) {
            resposta.setAtiva(Boolean.TRUE);
        }
        if (!resposta.isAtiva() && resposta.getDataHoraDesativacao() == null) {
            resposta.setDataHoraDesativacao(LocalDateTime.now());
        }
        if (resposta.getChavePaciente() == null || resposta.getChavePaciente().isBlank()) {
            resposta.setChavePaciente(gerarChavePaciente(resposta.getPaciente()));
        }

        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                if (resposta.getFormulario() != null) {
                    FormularioAnamnese f = resposta.getFormulario();
                    FormularioAnamnese fBanco = (f.getId() != null) ? em.find(FormularioAnamnese.class, f.getId()) : null;
                    if (fBanco == null) {
                        fBanco = em.merge(f);
                    }
                    resposta.setFormulario(fBanco);
                }
                if (resposta.getPaciente() != null && resposta.getPaciente().getId() != null) {
                    Paciente pBanco = em.find(Paciente.class, resposta.getPaciente().getId());
                    if (pBanco != null) {
                        resposta.setPaciente(pBanco);
                    }
                }
                if (resposta.getId() == null) {
                    em.persist(resposta);
                } else {
                    resposta = em.merge(resposta);
                }
                em.getTransaction().commit();
                sincronizarRespostaMemoriaInterno(resposta);
                salvarRespostasNoDisco();
                return;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

        if (resposta.getId() == null) {
            resposta.setId(proximoIdRespostaMemoria++);
        }
        sincronizarRespostaMemoriaInterno(resposta);
        salvarRespostasNoDisco();
    }

    @Override
    public void desativarRespostasPorPaciente(Paciente paciente) {
        LocalDateTime agora = LocalDateTime.now();
        for (RespostaAnamnese r : MEMORIA_RESPOSTAS) {
            if (respostaPertenceAoPaciente(r, paciente) && r.isAtiva()) {
                r.setAtiva(Boolean.FALSE);
                r.setDataHoraDesativacao(agora);
            }
        }
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<RespostaAnamnese> cq = cb.createQuery(RespostaAnamnese.class);
                Root<RespostaAnamnese> root = cq.from(RespostaAnamnese.class);
                cq.select(root);
                List<RespostaAnamnese> doBanco = em.createQuery(cq).getResultList();
                if (doBanco != null) {
                    for (RespostaAnamnese rb : doBanco) {
                        if (respostaPertenceAoPaciente(rb, paciente) && rb.isAtiva()) {
                            rb.setAtiva(Boolean.FALSE);
                            rb.setDataHoraDesativacao(agora);
                            em.merge(rb);
                            sincronizarRespostaMemoriaInterno(rb);
                        }
                    }
                }
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        salvarRespostasNoDisco();
    }

    @Override
    public List<RespostaAnamnese> listarRespostasPorPaciente(Paciente paciente) {
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<RespostaAnamnese> cq = cb.createQuery(RespostaAnamnese.class);
                Root<RespostaAnamnese> root = cq.from(RespostaAnamnese.class);
                cq.select(root);
                List<RespostaAnamnese> doBanco = em.createQuery(cq).getResultList();
                if (doBanco != null) {
                    for (RespostaAnamnese rb : doBanco) {
                        if (respostaPertenceAoPaciente(rb, paciente)) {
                            if (!rb.isAtiva() && rb.getDataHoraDesativacao() == null) {
                                rb.setDataHoraDesativacao(rb.getDataAtualizacao() != null ? rb.getDataAtualizacao() : LocalDateTime.now());
                            }
                            sincronizarRespostaMemoriaInterno(rb);
                        }
                    }
                    salvarRespostasNoDisco();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
        List<RespostaAnamnese> resultado = new ArrayList<>();
        for (RespostaAnamnese r : MEMORIA_RESPOSTAS) {
            if (respostaPertenceAoPaciente(r, paciente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}
