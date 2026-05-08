package model;

/**
 * Usuário comum: pode enviar sugestões e ver o histórico das próprias (RF02, RF04).
 */
public class UsuarioComum extends Usuario {

    public UsuarioComum() {
        super();
        setTipo(TipoUsuario.COMUM);
    }

    public UsuarioComum(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.COMUM);
    }

    @Override
    public String getPermissoes() {
        return "Enviar sugestões e visualizar histórico próprio.";
    }
}
