package model;

/**
 * Administrador: acesso total ao sistema.
 */
public class Administrador extends Usuario {

    public Administrador() {
        super();
        setTipo(TipoUsuario.ADMINISTRADOR);
    }

    public Administrador(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.ADMINISTRADOR);
    }

    @Override
    public String getPermissoes() {
        return "Acesso total: gerenciar usuários, projetos e sugestões.";
    }
}
