package com.mycompany.sistemaintegramind.util.Utilitarios;

import com.mycompany.sistemaintegramind.Model.entidades.Usuarios;
import com.mycompany.sistemaintegramind.Model.entidades.Permissoes;

public class SessaoUsuario {

    private static Usuarios usuarioLogado;

    public static void setUsuarioLogado(Usuarios usuario) {
        usuarioLogado = usuario;
    }

    public static Usuarios getUsuarioLogado() {
        return usuarioLogado;
    }

    //2026-01-31 Guilherme: Verifica se o usuário logado tem a permissão
    public static boolean temPermissao(String nomePermissao) {
        if (usuarioLogado == null || usuarioLogado.getCargo() == null) {
            return false;
        }

        //2026-01-31 Guilherme: Se for ADMIN master, pode liberar tudo (opcional)
        if ("ADMIN".equalsIgnoreCase(usuarioLogado.getCargo().getNome())) {
            return true;
        }

        //2026-01-31 Guilherme: Percorre as permissões associadas ao Cargo do Usuário
        if (usuarioLogado.getCargo().getPermissoes() != null) {
            for (Permissoes p : usuarioLogado.getCargo().getPermissoes()) {
                if (p.getNome().equalsIgnoreCase(nomePermissao)) {
                    return true;
                }
            }
        }
        return false;
    }
}