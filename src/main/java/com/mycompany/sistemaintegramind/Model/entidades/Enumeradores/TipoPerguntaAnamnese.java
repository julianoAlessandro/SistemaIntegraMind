package com.mycompany.sistemaintegramind.Model.entidades.Enumeradores;

public enum TipoPerguntaAnamnese {
    MULTIPLA_ESCOLHA("Múltipla Escolha"),
    ESTRELAS("Avaliação (Estrelas)"),
    CAIXA_SELECAO("Caixas de Seleção"),
    TEXTO_LIVRE("Resposta em Texto");

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
