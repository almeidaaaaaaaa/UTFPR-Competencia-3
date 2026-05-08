package model;

public class Gestor extends Usuario {

    public Gestor() {
        setTipo(TipoUsuario.GESTOR);

    }

    public Gestor(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.GESTOR);
    }

}
