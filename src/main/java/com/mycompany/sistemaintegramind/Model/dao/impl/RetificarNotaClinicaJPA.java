/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.RetificarNotaClinicaDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Agendamento;
import com.mycompany.sistemaintegramind.Model.entidades.InativarNotaClinica;
import com.mycompany.sistemaintegramind.Model.entidades.RetificarNotaClinica;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import javax.persistence.EntityManager;

public class RetificarNotaClinicaJPA implements RetificarNotaClinicaDAO {

    @Override
    public void cadastrarRetificacaoNota(RetificarNotaClinica retificarnota) {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(retificarnota);
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

    public RetificarNotaClinica buscarRetificacaoPorNotaOriginal(Long idNota) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql = "SELECT r "
                    + "FROM RetificarNotaClinica r "
                    + "WHERE r.notaOriginal.id = :idNota";

            return em.createQuery(jpql, RetificarNotaClinica.class)
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
