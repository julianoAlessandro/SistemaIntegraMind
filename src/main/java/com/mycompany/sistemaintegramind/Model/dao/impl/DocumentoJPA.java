/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.DocumentoDAO;
import com.mycompany.sistemaintegramind.Model.dao.FichaDocumentosFiltro;
import com.mycompany.sistemaintegramind.Model.entidades.Documento;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamentoDocumento;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DocumentoJPA implements DocumentoDAO {

    @Override
    public void CadastrarDocumento(Documento documento) {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(documento);
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
    public List<Documento> listarDocumentosPorPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Documento> listDocumentos = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Documento> cq = cb.createQuery(Documento.class);

            Root<Documento> RootDocumentos = cq.from(Documento.class);

            //2026-02-26 Juliano: mostrando na listagem todos os clientes que são ativos
            Predicate somenteClienteAtivo = cb.equal(RootDocumentos.get("statusdocumento"), StatusPacienteAgendamentoDocumento.ATIVO);
            Predicate DocumentosPaciente = cb.equal(RootDocumentos.get("paciente"), paciente);
            cq.select(RootDocumentos).where(DocumentosPaciente, somenteClienteAtivo);

            listDocumentos = em.createQuery(cq).getResultList();

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
        return listDocumentos;

    }

    @Override
    public Documento buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(Documento.class, id);

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
    public void atualizarPaciente(Documento documento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(documento);
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
    public List<Documento> filtrarDocumentosPacientes(FichaDocumentosFiltro fichadocumentofiltro) {

        List<Documento> filtrarDocumentos = new ArrayList<>();

        EntityManager em = JPAUtil.getEntityManager();

        try {

            if (fichadocumentofiltro.getPacienteId() == null) {
                return filtrarDocumentos;
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Documento> cq = cb.createQuery(Documento.class);

            Root<Documento> RootEvolucaoClinica = cq.from(Documento.class);

            List<Predicate> predicates = new ArrayList<>();

            //2026-10-31 Juliano: Filtrando pelo ID do paciente
            predicates.add(
                    cb.equal(
                            RootEvolucaoClinica.get("paciente").get("id"),
                            fichadocumentofiltro.getPacienteId()
                    )
            );

            //2026-10-31 Juliano: Exibindo somente documentos ativos
            predicates.add(
                    cb.equal(
                            RootEvolucaoClinica.get("statusdocumento"),
                            StatusPacienteAgendamentoDocumento.ATIVO
                    )
            );

            //2026-10-31 Juliano: Filtrando pelo nome do documento
            if (fichadocumentofiltro.getNomeArquivo() != null
                    && !fichadocumentofiltro.getNomeArquivo().trim().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(RootEvolucaoClinica.get("nomeArquivo")),
                                "%" + fichadocumentofiltro.getNomeArquivo().trim().toLowerCase() + "%"
                        )
                );

            }

            cq.select(RootEvolucaoClinica)
                    .where(cb.and(predicates.toArray(new Predicate[0])));

            filtrarDocumentos = em.createQuery(cq).getResultList();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (em != null) {
                em.close();
            }

        }

        return filtrarDocumentos;
    }
}
