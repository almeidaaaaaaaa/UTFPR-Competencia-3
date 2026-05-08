package model;

/**
 * Gestor: pode aprovar/recusar sugestões e gerenciar projetos (RF03).
 */
public class Gestor extends Usuario {

    public Gestor() {
        super();
        setTipo(TipoUsuario.GESTOR);
    }

    public Gestor(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.GESTOR);
    }

    @Override
    public String getPermissoes() {
        return "Gerenciar projetos e aprovar/recusar sugestões.";
    }
}
