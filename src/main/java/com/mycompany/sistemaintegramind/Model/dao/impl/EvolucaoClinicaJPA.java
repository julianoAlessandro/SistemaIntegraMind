/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.EvolucaoClinicaDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.EvolucaoClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
                    + "JOIN e.agendamento a "
                    + "WHERE a.paciente.id = :pacienteId "
                    + "ORDER BY a.dataAgendamento DESC, a.horario DESC";

            return em.createQuery(jpql, EvolucaoClinica.class)
                    .setParameter("pacienteId", pacienteId)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
