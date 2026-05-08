package model;

public class UsuarioComum extends Usuario {

    public UsuarioComum(int id, String nome, String email, String senha) {
        super(id, nome, email, senha, TipoUsuario.COMUM);
    }
    
     public UsuarioComum(){
        setTipo(TipoUsuario.COMUM);
    }
    
}
