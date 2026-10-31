package com.mycompany.sistemaintegramind.Model.entidades.Enumeradores;

public enum TipoModeloAnamnese {
    INFANTIL_ADOLESCENTE("Infantil / Adolescente"),
    ADULTO("Adulto"),
    PERSONALIZADO("Personalizado");

    private final String descricao;

    TipoModeloAnamnese(String descricao) {
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
