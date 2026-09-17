/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.AgendamentoDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AgendamentoJPA implements AgendamentoDAO {

    @Override
    public void cadastrarAgendamento(Agendamento agendamento) {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(agendamento);
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

    public List<Agendamento> listarAgendamentos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Agendamento> listAgendamento = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Agendamento> cq = cb.createQuery(Agendamento.class); // 2025-11-20 Juliano montou a query aqui vai
            // pegar os operadores do SQL(SELECT,INSERT
            // etc)
            Root<Agendamento> RootAgendamento = cq.from(Agendamento.class);

            //2026-02-26 Juliano: mostrando na listagem todos os pacientes que são ativos
            Predicate somenteClienteAtivo = cb.equal(RootAgendamento.get("statuspacienteagendamento"), StatusPacienteAgendamento.ATIVO);
            cq.select(RootAgendamento).where(somenteClienteAtivo);

            listAgendamento = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close(); // 2025-11-20 Juliano fecha o EntityManager, para não travar o sistema e não
                // consumir memoria precisa fechar
            }
        }
        return listAgendamento;

    }

    @Override
    public void atualizarAgendamento(Agendamento agendamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(agendamento);
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

    public Agendamento buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(Agendamento.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
