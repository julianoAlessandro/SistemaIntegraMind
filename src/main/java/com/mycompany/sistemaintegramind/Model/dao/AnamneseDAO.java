package com.mycompany.sistemaintegramind.Model.dao;

import com.mycompany.sistemaintegramind.Model.entidades.Enumeradores.TipoModeloAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.FormularioAnamnese;
import com.mycompany.sistemaintegramind.Model.entidades.Paciente;
import com.mycompany.sistemaintegramind.Model.entidades.RespostaAnamnese;
import java.util.List;

public interface AnamneseDAO {

    void cadastrarFormulario(FormularioAnamnese formulario);

    void atualizarFormulario(FormularioAnamnese formulario);

    FormularioAnamnese buscarPorId(Long id);

    FormularioAnamnese buscarModeloPadrao(TipoModeloAnamnese tipoModelo);

    List<FormularioAnamnese> listarTodosFormularios();

    List<FormularioAnamnese> listarFormulariosPorTipo(TipoModeloAnamnese tipoModelo);

    void salvarResposta(RespostaAnamnese resposta);

    List<RespostaAnamnese> listarRespostasPorPaciente(Paciente paciente);
}
