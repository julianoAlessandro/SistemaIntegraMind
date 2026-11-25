
package com.mycompany.sistemaintegramind.SegurancaLicenca;

import java.util.List;
import javax.persistence.EntityManager;

import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;

public class LicencaRepository {

    public void salvarOuAtualizar(Licenca novaLicenca) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            List<Licenca> resultado = em.createQuery(
                    "SELECT l FROM Licenca l ORDER BY l.id",
                    Licenca.class
            ).setMaxResults(1).getResultList();

            if (resultado.isEmpty()) {

                em.persist(novaLicenca);

            } else {

                Licenca existente = resultado.get(0);

                existente.setToken(novaLicenca.getToken());
                existente.setClinicaId(novaLicenca.getClinicaId());
                existente.setPagamentoId(novaLicenca.getPagamentoId());
                existente.setDataInicio(novaLicenca.getDataInicio());
                existente.setDataVencimento(novaLicenca.getDataVencimento());
                existente.setDataAtivacao(novaLicenca.getDataAtivacao());
                existente.setStatus(novaLicenca.getStatus());

            }

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "Erro ao salvar ou atualizar licença.", e
            );

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Licenca buscarLicenca() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            List<Licenca> resultado = em.createQuery(
                    "SELECT l FROM Licenca l ORDER BY l.id DESC",
                    Licenca.class
            ).setMaxResults(1).getResultList();

            return resultado.isEmpty() ? null : resultado.get(0);

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
