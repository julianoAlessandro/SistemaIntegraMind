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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AnamneseJPA implements AnamneseDAO {

    private static final List<FormularioAnamnese> MEMORIA_FORMULARIOS = new ArrayList<>();
    private static final List<RespostaAnamnese> MEMORIA_RESPOSTAS = new ArrayList<>();
    private static long proximoIdMemoria = 100L;

    static {
        inicializarModelosPadraoMemoria();
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

    private void sincronizarMemoria(FormularioAnamnese formulario) {
        if (formulario == null) {
            return;
        }
        for (int i = 0; i < MEMORIA_FORMULARIOS.size(); i++) {
            FormularioAnamnese existente = MEMORIA_FORMULARIOS.get(i);
            if (existente.getId() != null && existente.getId().equals(formulario.getId())) {
                MEMORIA_FORMULARIOS.set(i, formulario);
                return;
            }
        }
        MEMORIA_FORMULARIOS.add(formulario);
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
            // Atualiza automaticamente caso o banco ainda tenha a versão antiga de 3 perguntas do modelo Infantil/Adolescente
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
                List<FormularioAnamnese> resultado = em.createQuery(cq).getResultList();
                if (resultado != null && !resultado.isEmpty()) {
                    return resultado;
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

                List<FormularioAnamnese> resultado = em.createQuery(cq).getResultList();
                if (resultado != null && !resultado.isEmpty()) {
                    return resultado;
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
        if (resposta.getDataAbertura() == null) {
            resposta.setDataAbertura(LocalDate.now());
        }
        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                if (resposta.getId() == null) {
                    em.persist(resposta);
                } else {
                    em.merge(resposta);
                }
                em.getTransaction().commit();
                MEMORIA_RESPOSTAS.add(resposta);
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
        MEMORIA_RESPOSTAS.add(resposta);
    }

    @Override
    public List<RespostaAnamnese> listarRespostasPorPaciente(Paciente paciente) {
        EntityManager em = obterEntityManagerSeguro();
        if (em != null && paciente != null && paciente.getId() != null) {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<RespostaAnamnese> cq = cb.createQuery(RespostaAnamnese.class);
                Root<RespostaAnamnese> root = cq.from(RespostaAnamnese.class);
                Predicate predPaciente = cb.equal(root.get("paciente"), paciente);
                cq.select(root).where(predPaciente);
                return em.createQuery(cq).getResultList();
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
            if (paciente == null || (r.getPaciente() != null && r.getPaciente().equals(paciente))) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}
