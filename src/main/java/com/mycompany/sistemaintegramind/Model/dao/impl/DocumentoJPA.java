/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.DocumentoDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Documento;
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
            //Predicate somenteClienteAtivo = cb.equal(RootCliente.get("statuspaciente"), StatusPacienteAgendamento.ATIVO);
            Predicate DocumentosPaciente = cb.equal(RootDocumentos.get("paciente"), paciente);
            cq.select(RootDocumentos).where(DocumentosPaciente);

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

}
