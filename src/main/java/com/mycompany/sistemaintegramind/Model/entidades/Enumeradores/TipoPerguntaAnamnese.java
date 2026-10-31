package com.mycompany.sistemaintegramind.Model.entidades.Enumeradores;

public enum TipoPerguntaAnamnese {
    SECAO_TITULO("Título de Seção"),
    TEXTO_LIVRE("Resposta em Texto"),
    CAMPOS_LINHA("Múltiplos Campos na Linha"),
    MULTIPLA_ESCOLHA("Múltipla Escolha"),
    CAIXA_SELECAO("Caixas de Seleção"),
    ESCOLHA_COM_COMPLEMENTO("Escolha + Complemento (Qual/Desde/Por quê)"),
    GRADE_DISPONIBILIDADE("Tabela / Grade (Dias x Períodos)"),
    ESTRELAS("Avaliação (Estrelas)");

    private final String descricao;

    TipoPerguntaAnamnese(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
