package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.AudioEvolucaoClinicaDAO;
import com.mycompany.sistemaintegramind.Model.entidades.AudioEvolucaoClinica;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import javax.persistence.EntityManager;

public class AudioEvolucaoClinicaJPA implements AudioEvolucaoClinicaDAO {

    @Override
    public void cadastrarAudioEvolucaoClinica(AudioEvolucaoClinica audioevolucaoclinica) {
        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(audioevolucaoclinica);
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
    public AudioEvolucaoClinica buscarAudioPorEvolucao(Long idEvolucao) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "SELECT a FROM AudioEvolucaoClinica a "
                    + "WHERE a.evolucaoClinica.id = :idEvolucao",
                    AudioEvolucaoClinica.class
            )
                    .setParameter("idEvolucao", idEvolucao)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (em != null) {
                em.close();
            }
        }
    }

}
