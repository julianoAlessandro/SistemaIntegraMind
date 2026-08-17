package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.sistemaintegramind.Model.dao.PermissoesDAO;

/**
 * @author Guilherme
 */
public class PermissoesJPA implements PermissoesDAO {

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    @Override
    public void cadastrarPermissao(Permissoes permissao) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(permissao);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new RuntimeException(
                    "Erro ao cadastrar a permissão: " + e.getMessage()
            );

        } finally {
            em.close();
        }
    }

    @Override
    public List listarPermissoes() {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Permissoes> query = em.createQuery(
                    "SELECT p FROM Permissoes p",
                    Permissoes.class
            );

            return query.getResultList();

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();

        } finally {
            em.close();
        }
    }

    @Override
    public void deletarPermissao(Permissoes permissao) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            // Faz o merge para garantir que o objeto esteja
            // no estado gerenciado antes de remover
            Permissoes permissaoGerenciada = em.merge(permissao);

            em.remove(permissaoGerenciada);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new RuntimeException(
                    "Erro ao deletar a permissão: " + e.getMessage()
            );

        } finally {
            em.close();
        }
    }

    @Override
    public void atualizarPermissao(Permissoes permissao) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(permissao);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new RuntimeException(
                    "Erro ao atualizar a permissão: " + e.getMessage()
            );

        } finally {
            em.close();
        }
    }

    @Override
    public Permissoes buscarPorNome(String nome) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Permissoes> query = em.createQuery(
                    "SELECT p FROM Permissoes p WHERE p.nome = :nome",
                    Permissoes.class
            );

            query.setParameter("nome", nome);

            // Retorna o resultado ou null caso não encontre
            return query.getResultStream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {
            em.close();
        }
    }

    @Override
    public Permissoes buscarPorId(Long id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(Permissoes.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {
            em.close();
        }
    }
}