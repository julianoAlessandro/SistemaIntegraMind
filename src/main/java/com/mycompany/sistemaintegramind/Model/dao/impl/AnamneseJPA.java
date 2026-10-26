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

    // Fallback em memória caso o banco MariaDB não esteja acessível no momento do teste
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

    public static FormularioAnamnese criarTemplatePadrao(TipoModeloAnamnese tipo) {
        if (tipo == TipoModeloAnamnese.INFANTIL_ADOLESCENTE) {
            FormularioAnamnese form = new FormularioAnamnese();
            form.setTitulo("Anamnese Infantil / Adolescente");
            form.setTipoModelo(TipoModeloAnamnese.INFANTIL_ADOLESCENTE);
            form.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

            form.adicionarPergunta(new PerguntaAnamnese(
                    "Como ocorreu o desenvolvimento motor e da fala na primeira infância?",
                    1,
                    TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                    Arrays.asList("Dentro do esperado para a faixa etária", "Apresentou leve atraso no desenvolvimento", "Necessitou de acompanhamento especializado")
            ));
            form.adicionarPergunta(new PerguntaAnamnese(
                    "Como está a adaptação escolar, socialização e relacionamento com colegas?",
                    2,
                    TipoPerguntaAnamnese.ESTRELAS,
                    Collections.emptyList()
            ));
            form.adicionarPergunta(new PerguntaAnamnese(
                    "Principais queixas ou comportamentos observados pela família/escola:",
                    3,
                    TipoPerguntaAnamnese.TEXTO_LIVRE,
                    Collections.emptyList()
            ));
            return form;
        } else {
            FormularioAnamnese form = new FormularioAnamnese();
            form.setTitulo("Anamnese Adulto");
            form.setTipoModelo(TipoModeloAnamnese.ADULTO);
            form.setStatusFormulario(StatusPacienteAgendamentoDocumento.ATIVO);

            form.adicionarPergunta(new PerguntaAnamnese(
                    "Já realizou acompanhamento psicológico ou psiquiátrico anteriormente?",
                    1,
                    TipoPerguntaAnamnese.MULTIPLA_ESCOLHA,
                    Arrays.asList("Sim, atualmente em acompanhamento", "Sim, em período anterior", "Não, é o primeiro atendimento")
            ));
            form.adicionarPergunta(new PerguntaAnamnese(
                    "Como avalia a qualidade atual do sono, rotina e nível de disposição diária?",
                    2,
                    TipoPerguntaAnamnese.ESTRELAS,
                    Collections.emptyList()
            ));
            form.adicionarPergunta(new PerguntaAnamnese(
                    "Queixa principal, histórico familiar e motivo da busca pelo atendimento:",
                    3,
                    TipoPerguntaAnamnese.TEXTO_LIVRE,
                    Collections.emptyList()
            ));
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
        int ordem = 1;
        for (PerguntaAnamnese p : formulario.getPerguntas()) {
            p.setFormulario(formulario);
            p.setOrdem(ordem++);
        }

        EntityManager em = obterEntityManagerSeguro();
        if (em != null) {
            try {
                em.getTransaction().begin();
                FormularioAnamnese atualizado = em.merge(formulario);
                em.getTransaction().commit();
                sincronizarMemoria(atualizado);
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
            return lista.get(0);
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
