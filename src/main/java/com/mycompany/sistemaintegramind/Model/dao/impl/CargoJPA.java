package com.mycompany.sistemaintegramind.Model.dao.impl;


import com.mycompany.sistemaintegramind.Model.dao.CargoDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Cargo;
import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guilherme
 */
public class CargoJPA implements CargoDAO {

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    @Override
    public void cadastrarCargo(Cargo cargo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cargo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar o cargo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cargo> listarCargos() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cargo> query = em.createQuery("SELECT c FROM Cargo c", Cargo.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    @Override
    public void deletarCargo(Cargo cargo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            //2026-07-18 Guilherme: Faz o merge para garantir que o objeto esteja no estado gerenciado antes de remover
            Cargo cargoGerenciado = em.merge(cargo);
            em.remove(cargoGerenciado);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar o cargo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public void atualizarCargo(Cargo cargo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cargo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar o cargo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Cargo buscarPorNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cargo> query = em.createQuery("SELECT c FROM Cargo c WHERE c.nome = :nome", Cargo.class);
            query.setParameter("nome", nome);
            //2026-07-18 Guilherme: Retorna o resultado ou null de forma segura caso não encontre nenhum registro
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Permissoes> listarPermissoesPorCargo(Cargo cargo) {
        if (cargo == null || cargo.getId() == null) {
            return new ArrayList<>();
        }

        EntityManager em = getEntityManager();
        try {
            //2026-07-18 Guilherme: Busca o cargo do banco para garantir o carregamento do relacionamento ManyToMany
            Cargo cargoGerenciado = em.find(Cargo.class, cargo.getId());

            if (cargoGerenciado != null && cargoGerenciado.getPermissoes() != null) {
                //2026-07-18 Guilherme: Como está mapeado com FetchType.EAGER, a lista já foi inicializada na busca
                return new ArrayList<>(cargoGerenciado.getPermissoes());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}
