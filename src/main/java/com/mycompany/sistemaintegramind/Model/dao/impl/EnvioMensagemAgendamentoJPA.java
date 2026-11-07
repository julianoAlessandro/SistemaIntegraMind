/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.dao.impl;

import com.mycompany.sistemaintegramind.Model.dao.EnvioMensagemAgendamentoDAO;
import com.mycompany.sistemaintegramind.Model.entidades.EnvioMensagemAgendamento;
import com.mycompany.sistemaintegramind.util.Utilitarios.JPAUtil;
import javax.persistence.EntityManager;

/**
 *
 * @author Micro
 */
public class EnvioMensagemAgendamentoJPA implements EnvioMensagemAgendamentoDAO {

    @Override
    public void registrarEnvioMensagemAutomatica(EnvioMensagemAgendamento enviomensagem) {
         EntityManager em = JPAUtil.getEntityManager(); 
        try {
          
            em.getTransaction().begin();
            em.persist(enviomensagem);
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
    
}
