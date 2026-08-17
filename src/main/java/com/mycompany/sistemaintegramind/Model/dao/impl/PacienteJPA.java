/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.ClienteFiltro;
import com.mycompany.sistemaintegramind.Model.entidades.Pacientes;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.mycompany.sistemaintegramind.Model.dao.PacienteDAO;
import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.StatusPaciente;

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
    public void CadastrarCliente(Pacientes cliente) {
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
    public List<Pacientes> listarClientes() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Pacientes> listClientes = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pacientes> cq = cb.createQuery(Pacientes.class); // 2025-11-20 Juliano montou a query aqui vai
            // pegar os operadores do SQL(SELECT,INSERT
            // etc)
            Root<Pacientes> RootCliente = cq.from(Pacientes.class);

            //2026-02-26 Juliano: mostrando na listagem todos os clientes que são ativos
            Predicate somenteClienteAtivo = cb.equal(RootCliente.get("statuspaciente;"), StatusPaciente.ATIVO);
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
    public List<Pacientes> filtrarClientes(ClienteFiltro clientefiltro) {
        List<Pacientes> filtrarClientes = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pacientes> cq = cb.createQuery(Pacientes.class);
            Root<Pacientes> RootClientes = cq.from(Pacientes.class);

            List<Predicate> predicates = new ArrayList<>(); // 2025-11-23 Juliano indica qual condicao/ filtro  tera
            
            //2026-08-04 Juliano: já definindo que está lista filtrada tera somente os clientes ATIVOS
            predicates.add(
                    cb.equal(
                            RootClientes.get("statuspaciente"),
                            StatusPaciente.ATIVO
                    )
            );

            if (clientefiltro.getId() != null) {
                predicates.add(cb.equal(RootClientes.get("id"), clientefiltro.getId()));
            }
            if (clientefiltro.getSexo() != null) {
                predicates.add(cb.equal(RootClientes.get("sexo"), clientefiltro.getSexo()));

            }
            if (clientefiltro.getNome() != null && !clientefiltro.getNome().isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(RootClientes.get("nome")), "%" + clientefiltro.getNome().toLowerCase() + "%"));

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
    public void deletarCliente(Pacientes cliente) {
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
    public void atualizarCliente(Pacientes cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cliente);
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
        EntityManager em = JPAUtil.getEntityManager();
        return em;
    }

    public String buscarEnderecoPorCep(String cep) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
