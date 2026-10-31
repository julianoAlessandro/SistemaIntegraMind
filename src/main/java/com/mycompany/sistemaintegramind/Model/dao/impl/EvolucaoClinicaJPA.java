/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.EvolucaoClinicaDAO;
import com.mycompany.sistemaintegramind.Model.dao.FichaProntuarioPacienteFiltro;
import com.mycompany.sistemaintegramind.Model.dao.PacienteFiltro;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Micro
 */
public class EvolucaoClinicaJPA implements EvolucaoClinicaDAO {

    @Override
    public void cadastrarEvolucaoClinica(EvolucaoClinica evolucaoClinica) {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(evolucaoClinica);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<EvolucaoClinica> listarEvolucoesClinicas() {
        EntityManager em = JPAUtil.getEntityManager();
        List<EvolucaoClinica> listEvolucoesClinica = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<EvolucaoClinica> cq = cb.createQuery(EvolucaoClinica.class);

            Root<EvolucaoClinica> RootEvolucaoClinica = cq.from(EvolucaoClinica.class);

            cq.select(RootEvolucaoClinica);

            listEvolucoesClinica = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return listEvolucoesClinica;

    }

    public List<EvolucaoClinica> listarEvolucoesPorPaciente(Long pacienteId) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql = "SELECT e "
                    + "FROM EvolucaoClinica e "
                    + "LEFT JOIN FETCH e.inativacao "
                    + "WHERE e.paciente.id = :pacienteId "
                    + "ORDER BY e.dataEvolucao DESC, e.horario DESC";

            return em.createQuery(jpql, EvolucaoClinica.class)
                    .setParameter("pacienteId", pacienteId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void AtualizarStatusNotaClinica(EvolucaoClinica evolucao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(evolucao);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    @Override
    public EvolucaoClinica buscarNotaClinicaPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(EvolucaoClinica.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<EvolucaoClinica> filtrarEvolucaoClinicaPacientes(FichaProntuarioPacienteFiltro fichaprontuariofilro) {

        List<EvolucaoClinica> filtrarEvolucoes = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();

        try {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<EvolucaoClinica> cq = cb.createQuery(EvolucaoClinica.class);

            Root<EvolucaoClinica> RootEvolucaoClinica = cq.from(EvolucaoClinica.class);

            //2026-10-31 Juliano: Relacionamento com a inativação da nota
            Join<EvolucaoClinica, InativarNotaClinica> joinInativacao
                    = RootEvolucaoClinica.join("inativacao", JoinType.LEFT);

            //2026-10-31 Juliano: Relacionamento com as retificações da nota original
            Join<EvolucaoClinica, RetificarNotaClinica> joinRetificacaoOriginal
                    = RootEvolucaoClinica.join("retificacoesComoNotaOriginal", JoinType.LEFT);

            //2026-10-31 Juliano: Relacionamento com as retificações da nota retificadora
            Join<EvolucaoClinica, RetificarNotaClinica> joinRetificacaoRetificadora
                    = RootEvolucaoClinica.join("retificacoesComoNotaRetificadora", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            //2026-10-31 Juliano: Filtrando obrigatoriamente pelo paciente
            if (fichaprontuariofilro.getPacienteId() == null) {
                return filtrarEvolucoes;
            }

            predicates.add(cb.equal(
                    RootEvolucaoClinica.get("paciente").get("id"),
                    fichaprontuariofilro.getPacienteId()
            ));

            //2026-10-31 Juliano: Pesquisando nos três campos simultaneamente
            if (fichaprontuariofilro.getEvolucaoSessao() != null
                    && !fichaprontuariofilro.getEvolucaoSessao().trim().isEmpty()) {

                String pesquisa = "%"
                        + fichaprontuariofilro.getEvolucaoSessao().trim().toLowerCase()
                        + "%";

                Predicate pesquisarEvolucao = cb.like(
                        cb.lower(RootEvolucaoClinica.get("evolucaoSessao")),
                        pesquisa
                );

                Predicate pesquisarInativacao = cb.like(
                        cb.lower(joinInativacao.get("motivoInativarNota")),
                        pesquisa
                );

                Predicate pesquisarRetificacaoOriginal = cb.like(
                        cb.lower(joinRetificacaoOriginal.get("motivoRetificacao")),
                        pesquisa
                );

                Predicate pesquisarRetificacaoRetificadora = cb.like(
                        cb.lower(joinRetificacaoRetificadora.get("motivoRetificacao")),
                        pesquisa
                );

                predicates.add(cb.or(
                        pesquisarEvolucao,
                        pesquisarInativacao,
                        pesquisarRetificacaoOriginal,
                        pesquisarRetificacaoRetificadora
                ));
            }

            cq.select(RootEvolucaoClinica)
                    .distinct(true)
                    .where(cb.and(predicates.toArray(new Predicate[0])));

            filtrarEvolucoes = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return filtrarEvolucoes;
    }

}
