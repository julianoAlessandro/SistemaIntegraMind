/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaintegramind.Model.entidades;

import com.mycompany.sistemaintegramind.Model.dao.impl.AgendamentoJPA;
import com.mycompany.sistemaintegramind.util.Utilitarios.BotaoRenderizarIcones;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Micro
 */
public class GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos {

    public static DefaultTableModel formatarTabelaAgendamento(DefaultTableModel definirModeloTabelaAgendamento) {

        definirModeloTabelaAgendamento = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Paciente",
                    "Horário",
                    "Status",
                    "Tipo de Atendimento",
                    "Pagamento",
                    "Situação",
                    "Data",
                    "Valor da Consulta",
                    "Editar",
                    "Excluir"
                },
                0
        );

        return definirModeloTabelaAgendamento;
    }

    public static void configurarIconesDasColunasDaTabelaAgendamento(JTable tblAgendamentos) {
        tblAgendamentos.getColumnModel()
                .getColumn(6).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(6)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/PENDENTE.png"));

        tblAgendamentos.getColumnModel().getColumn(9).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(9)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/empate.png"));

        tblAgendamentos.getColumnModel().getColumn(10).setMaxWidth(50);
        tblAgendamentos.getColumnModel().getColumn(10)
                .setCellRenderer(new BotaoRenderizarIcones("/imagens/excluir.png"));

    }

    public static void carregarAgendamento(DefaultTableModel modelo, List<Agendamento> ListarAgendamentos) {
        for (Agendamento agendamentos : ListarAgendamentos) {
            String statuspagamento = agendamentos.getStatuspagamento().toString();
            String statusagendamento = agendamentos.getStatusagendamento().toString();
            String pagamentoformatado, statusagendamentoformatado = null;
            LocalDate dataAtualConsulta = agendamentos.getDataAgendamento();
            if (statuspagamento.equals("PENDENTE")) {
                pagamentoformatado = "<html><font color='red'><b>PENDENTE</b></font></html>";
            } else {
                pagamentoformatado = "<html><font color='green'><b>PAGO</b></font></html>";
            }
            //2026-09-30 Juliano: Validação da mudança automática do status de AGENDADO para REALIZADO,quando a data da consulta já tiver passado e o status da consulta esteja como AGENDADO
            if (dataAtualConsulta.isBefore(LocalDate.now()) & statusagendamento.equals("AGENDADO")) {
                statusagendamentoformatado = "<html><font color='green'><b>REALIZADO</b></font></html>";
            } else {
                if (statusagendamento.equals("REALIZADO")) {
                    statusagendamentoformatado = "<html><font color='green'><b>REALIZADO</b></font></html>";
                }
                if (statusagendamento.equals("AGENDADO")) {
                    statusagendamentoformatado = "<html><font color='blue'><b>AGENDADO</b></font></html>";
                }

                if (statusagendamento.equals("CANCELADO")) {
                    statusagendamentoformatado = "<html><font color='gray'><b>CANCELADO</b></font></html>";
                }
                if (statusagendamento.equals("FALTOU")) {
                    statusagendamentoformatado = "<html><font color='yellow'><b>FALTOU</b></font></html>";
                }

            }

            modelo.addRow(new Object[]{
                agendamentos.getId(),
                agendamentos.getPaciente().getNome(),
                agendamentos.getHorario(),
                statusagendamentoformatado,
                agendamentos.getTipoatendimento(),
                pagamentoformatado,
                "",
                agendamentos.getDataAgendamento().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                agendamentos.getValorDaConsulta(),
                "",
                ""

            }
            );
        }
    }

    public static List<Agendamento> atualizartabelaAgendamento(List<Agendamento> agendamentos,DefaultTableModel modeloTabelaListarTodosAgendamentos,JTable tblAgendamentos,Paciente paciente) {

        AgendamentoJPA agendamentojpa = new AgendamentoJPA();

        agendamentos = agendamentojpa.listarAgendamentosPorPacientes(paciente);

        modeloTabelaListarTodosAgendamentos.setRowCount(0);

        GerenciadorDeCarregamentoEFormatacaoDaTabelaDeAgendamentos.carregarAgendamento(
                modeloTabelaListarTodosAgendamentos,
                agendamentos
        );

        tblAgendamentos.revalidate();
        tblAgendamentos.repaint();

        return agendamentos;
    }

}
