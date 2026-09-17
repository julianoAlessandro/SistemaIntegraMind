/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.PacienteFiltro;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.mycompany.sistemaintegramind.Model.dao.PacienteDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPacienteAgendamento;

/**
 *
 * @author Juliano
 */
public class PacienteJPA implements PacienteDAO {

    // 2025-11-22
    // Juliano
    // inicia
    // todas
    // as
    // configurações
    // do
    // persistente.xml,senha,porta,driver
    // etc,
    // atributo
    // static
    // para
    // gerar
    // apenas
    // uma
    // instancia
    // sem
    // precisar
    // ficar
    // configurando
    // todo
    // esse
    // arquivo
    // novamente
    @Override
    public void CadastrarCliente(Paciente cliente) {
        EntityManager em = JPAUtil.getEntityManager(); // 2026-08-08 Guilherme: Utilitario para reduzir o tempo de entrada do login

        try {
            // 2025-11-20 juliano depois de criar a conexao com o banco de dados você
            // chama um metodo para permitir realizar operações no banco de dados
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
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
    }

    @Override
    public List<Paciente> listarPacientes() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Paciente> listClientes = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Paciente> cq = cb.createQuery(Paciente.class); // 2025-11-20 Juliano montou a query aqui vai
            // pegar os operadores do SQL(SELECT,INSERT
            // etc)
            Root<Paciente> RootCliente = cq.from(Paciente.class);

            //2026-02-26 Juliano: mostrando na listagem todos os clientes que são ativos
            Predicate somenteClienteAtivo = cb.equal(RootCliente.get("statuspaciente"), StatusPacienteAgendamento.ATIVO);
            cq.select(RootCliente).where(somenteClienteAtivo);

            listClientes = em.createQuery(cq).getResultList();

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
        return listClientes;

    }

    @Override
    public List<Paciente> filtrarPacientes(PacienteFiltro pacientefiltro) {
        List<Paciente> filtrarClientes = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Paciente> cq = cb.createQuery(Paciente.class);
            Root<Paciente> RootClientes = cq.from(Paciente.class);

            List<Predicate> predicates = new ArrayList<>(); // 2025-11-23 Juliano indica qual condicao/ filtro  tera

            //2026-08-04 Juliano: já definindo que está lista filtrada tera somente os clientes ATIVOS
            predicates.add(cb.equal(RootClientes.get("statuspaciente"),
                            StatusPacienteAgendamento.ATIVO
                    )
            );

            if (pacientefiltro.getId() != null) {
                predicates.add(cb.equal(RootClientes.get("id"), pacientefiltro.getId()));
            }

            if (pacientefiltro.getNome() != null && !pacientefiltro.getNome().isEmpty()) {
                predicates.add(cb.like(cb.lower(RootClientes.get("nome")), "%" + pacientefiltro.getNome().toLowerCase() + "%"));

            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            filtrarClientes = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return filtrarClientes;
    }

    @Override
    public void deletarCliente(Paciente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(cliente));
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
    public void atualizarPaciente(Paciente paciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(paciente);
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

    public Paciente buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(Paciente.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Paciente buscarPorNome(String nome) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "SELECT p FROM Paciente p WHERE p.nome = :nome",
                    Paciente.class)
                    .setParameter("nome", nome)
                    .getSingleResult();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public EntityManager getEntityManager() {
        EntityManager em = JPAUtil.getEntityManager();
        return em;
    }

    public String buscarEnderecoPorCep(String cep) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
