/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.InativarNotaClinicaDAO;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;

public class InativarNotaClinicaJPA implements InativarNotaClinicaDAO {

    @Override
    public void cadastrarJustificativaInativarNota(InativarNotaClinica inativarNota) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // Verifica se a nota já possui uma justificativa de inativação
            Long idNota = inativarNota.getNotaclinica().getId();

            Long quantidade = em.createQuery(
                    "SELECT COUNT(i) FROM InativarNotaClinica i "
                    + "WHERE i.notaclinica.id = :idNota",
                    Long.class
            )
                    .setParameter("idNota", idNota)
                    .getSingleResult();

            if (quantidade > 0) {
                em.getTransaction().rollback();

                throw new IllegalStateException(
                        "Esta nota clínica já possui um registro de inativação."
                );
            }

            // Salva a justificativa
            em.persist(inativarNota);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "Não foi possível salvar a justificativa da inativação.",
                    e
            );

        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public InativarNotaClinica buscarInativacaoPorNotaClinica(Long idNota) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql = "SELECT i "
                    + "FROM InativarNotaClinica i "
                    + "WHERE i.notaclinica.id = :idNota";

            return em.createQuery(jpql, InativarNotaClinica.class)
                    .setParameter("idNota", idNota)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}
