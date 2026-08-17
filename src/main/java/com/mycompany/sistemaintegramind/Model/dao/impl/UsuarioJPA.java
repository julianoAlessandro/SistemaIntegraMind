package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.UsuarioDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Usuarios;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Guilherme
 */
public class UsuarioJPA implements UsuarioDAO {

    @Override
    public void cadastrarUsuario(Usuarios usuario) {
        EntityManager em = null;

        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();

            //2026-07-18 Guilherme: HASH DA SENHA
            String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            usuario.setSenha(senhaHash);

            em.persist(usuario);

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
    public List<Usuarios> listarUsuarios() {
        List<Usuarios> listUsuarios = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();

        try {
            //2026-07-18 Guilherme: Alterado para carregar o Cargo e suas Permissões juntos de forma performática
            String jpql = "SELECT DISTINCT u FROM Usuarios u LEFT JOIN FETCH u.cargo c LEFT JOIN FETCH c.permissoes";
            listUsuarios = em.createQuery(jpql, Usuarios.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return listUsuarios;
    }

    @Override
    public Usuarios login(String usuario, String senha) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            //2026-07-18 Guilherme: JPQL atualizada trazendo o Cargo e as Permissões necessárias para a segurança modular
            String jpql = "SELECT u FROM Usuarios u "
                    + "JOIN FETCH u.cargo c "
                    + "LEFT JOIN FETCH c.permissoes "
                    + "WHERE u.usuario = :usuario";

            Usuarios user = em.createQuery(jpql, Usuarios.class)
                    .setParameter("usuario", usuario)
                    .getSingleResult();

            //2026-07-18 Guilherme: VALIDA SENHA COM HASH
            if (BCrypt.checkpw(senha, user.getSenha())) {
                System.out.println("Usuário e senha corretos! Login realizado com sucesso.");
                return user;
            }

        } catch (NoResultException e) {
            return null; //2026-07-18 Guilherme: Usuário não encontrado
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return null; //2026-07-18 Guilherme: Senha incorreta ou erro interno
    }

    @Override
    public void deletarUsuario(Usuarios usuario) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.merge(usuario));
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
    public void atualizarUsuario(Usuarios usuario) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            //2026-07-18 Guilherme: ⚠️ IMPORTANTE: só re-hash se a senha foi alterada
            if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
                String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
                usuario.setSenha(senhaHash);
            }

            em.merge(usuario);
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

    public EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}
