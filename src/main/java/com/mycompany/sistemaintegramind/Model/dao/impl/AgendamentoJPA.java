/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.AgendaFiltro;
import com.mycompany.sistemaintegramind.Model.dao.AgendamentoDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPagamento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import com.mycompany.sistemaintegramind.util.Utilitarios.StatusAgendamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public List<Agendamento> listarAgendamentosDoDia() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Agendamento> listAgendamento = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Agendamento> cq = cb.createQuery(Agendamento.class); // 2025-11-20 Juliano montou a query aqui vai
            // pegar os operadores do SQL(SELECT,INSERT
            // etc)
            Root<Agendamento> RootAgendamento = cq.from(Agendamento.class);

            //2026-02-26 Juliano: mostrando na listagem todos os pacientes que são ativos e que terão consulta na data de hoje
            Predicate somenteClienteAtivo = cb.equal(RootAgendamento.get("statuspacienteagendamento"), StatusPacienteAgendamento.ATIVO);
            Predicate AgendamentosDeHoje = cb.equal(RootAgendamento.get("dataAgendamento"), LocalDate.now());
            cq.select(RootAgendamento).where(somenteClienteAtivo, AgendamentosDeHoje);

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

    public boolean horarioOcupado(LocalDate data, LocalTime horario) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            Long quantidade = em.createQuery(
                    "SELECT COUNT(a) FROM Agendamento a "
                    + "WHERE a.dataAgendamento = :data "
                    + "AND a.horario = :horario",
                    Long.class)
                    .setParameter("data", data)
                    .setParameter("horario", horario)
                    .getSingleResult();

            return quantidade > 0;

        } finally {
            em.close();
        }
    }

    public List<Agendamento> filtrarPacientes(AgendaFiltro agendafiltrar) {
        List<Agendamento> filtrarAgendamentos = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Agendamento> cq = cb.createQuery(Agendamento.class);
            Root<Agendamento> RootAgendamento = cq.from(Agendamento.class);

            List<Predicate> predicates = new ArrayList<>(); // 2025-11-23 Juliano indica qual condicao/ filtro  tera

            //2026-08-04 Juliano: já definindo que está lista filtrada tera somente os clientes ATIVOS
            predicates.add(cb.equal(RootAgendamento.get("statuspacienteagendamento"),
                    StatusPacienteAgendamento.ATIVO
            )
            );

            if (agendafiltrar.getDataAgendamento() != null) {
                predicates.add(cb.equal(RootAgendamento.get("dataAgendamento"), agendafiltrar.getDataAgendamento()));
            }

            if (agendafiltrar.getPaciente() != null) {
                predicates.add(cb.equal(RootAgendamento.get("paciente"), agendafiltrar.getPaciente()));
            }
            if (agendafiltrar.getStatuspagamento() != null) {
                predicates.add(cb.equal(RootAgendamento.get("statuspagamento"), agendafiltrar.getStatuspagamento()));
            }
            if (agendafiltrar.getTipoatendimento() != null) {
                predicates.add(cb.equal(RootAgendamento.get("tipoatendimento"), agendafiltrar.getTipoatendimento()));
            }
            if (agendafiltrar.getFrequenciaAtendimento() != null) {
                predicates.add(cb.equal(RootAgendamento.get("frequenciaatendimento"), agendafiltrar.getFrequenciaAtendimento()));
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            filtrarAgendamentos = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return filtrarAgendamentos;
    }

    public BigDecimal faturamento() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            BigDecimal faturamento = em.createQuery(
                    "SELECT COALESCE(SUM(a.valorDaConsulta), 0) "
                    + "FROM Agendamento a "
                    + "WHERE a.statuspacienteagendamento = :statusPaciente "
                    + "AND a.statusagendamento IN (:statusAgendamento1, :statusAgendamento2) "
                    + "AND a.statuspagamento = :statusPagamento ",
                    BigDecimal.class)
                    .setParameter("statusPaciente", StatusPacienteAgendamento.ATIVO)
                    .setParameter("statusAgendamento1", StatusAgendamento.AGENDADO)
                    .setParameter("statusAgendamento2", StatusAgendamento.REALIZADO)
                    .setParameter("statusPagamento", StatusPagamento.PAGO)
                    .getSingleResult();

            return faturamento;

        } finally {
            em.close();
        }
    }

    public int totalPagamentosPendentes() {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            Long totalPacientesAtivos = em.createQuery(
                    "SELECT COUNT(a) FROM Agendamento a "
                    + "WHERE a.statuspacienteagendamento = :statuspaciente "
                    + "AND a.statuspagamento = :statuspagamento",
                    Long.class)
                    .setParameter("statuspaciente", StatusPacienteAgendamento.ATIVO)
                    .setParameter("statuspagamento", StatusPagamento.PENDENTE)
                    .getSingleResult();

            return totalPacientesAtivos.intValue();

        } finally {
            em.close();
        }
    }

    public int totalAgendamentosDoDia() {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            Long totalPacientesAtivos = em.createQuery(
                    "SELECT COUNT(a) FROM Agendamento a "
                    + "WHERE a.statuspacienteagendamento = :statuspaciente "
                    + "AND a.dataAgendamento = :dataagendamento ",
                    Long.class)
                    .setParameter("statuspaciente", StatusPacienteAgendamento.ATIVO)
                    .setParameter("dataagendamento",LocalDate.now())
                    .getSingleResult();

            return totalPacientesAtivos.intValue();

        } finally {
            em.close();
        }
    }
}
