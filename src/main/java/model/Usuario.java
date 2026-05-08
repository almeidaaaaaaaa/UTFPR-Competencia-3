package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Classe base abstrata para todos os tipos de usuário do sistema.
 * RF01 - Cadastro/Login: Autenticação de usuários.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UsuarioComum.class,    name = "COMUM"),
    @JsonSubTypes.Type(value = Gestor.class,          name = "GESTOR"),
    @JsonSubTypes.Type(value = Administrador.class,   name = "ADMINISTRADOR")
})
public abstract class Usuario {

    private int         id;
    private String      nome;
    private String      email;
    private String      senha;
    private TipoUsuario tipo;

    public Usuario() {}

    public Usuario(int id, String nome, String email, String senha, TipoUsuario tipo) {
        this.id    = id;
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
        this.tipo  = tipo;
    }

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public String getNome()          { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getSenha()             { return senha; }
    public void setSenha(String senha)   { this.senha = senha; }

    public TipoUsuario getTipo()                   { return tipo; }
    public void setTipo(TipoUsuario tipo)           { this.tipo = tipo; }

    /**
     * Retorna uma descrição das permissões deste tipo de usuário.
     */
    public abstract String getPermissoes();

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', email='" + email
                + "', tipo=" + tipo + "}";
    }
}
