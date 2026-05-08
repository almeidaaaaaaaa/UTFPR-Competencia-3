package model;

/**
 * Representa um projeto de extensão da UTFPR (ex.: Meninas Digitais).
 * Cada sugestão fica vinculada a um projeto específico (RF02).
 */
public class ProjetoExtensao {

    private int    id;
    private String nome;
    private String descricao;
    private Gestor coordenador;

    public ProjetoExtensao() {}

    public ProjetoExtensao(int id, String nome, String descricao, Gestor coordenador) {
        this.id          = id;
        this.nome        = nome;
        this.descricao   = descricao;
        this.coordenador = coordenador;
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public String getDescricao()               { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Gestor getCoordenador()                       { return coordenador; }
    public void setCoordenador(Gestor coordenador)       { this.coordenador = coordenador; }

    @Override
    public String toString() {
        return "ProjetoExtensao{id=" + id + ", nome='" + nome
                + "', coordenador=" + (coordenador != null ? coordenador.getNome() : "N/A") + "}";
    }
}
