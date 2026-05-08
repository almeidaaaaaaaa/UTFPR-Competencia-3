package model;

/**
 * Define os perfis de acesso dos usuários no sistema.
 */
public enum TipoUsuario {
    COMUM("Comum"),
    GESTOR("Gestor de Projeto"),
    ADMINISTRADOR("Administrador");

    private final String descricao;

    TipoUsuario(String descricao) {
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
