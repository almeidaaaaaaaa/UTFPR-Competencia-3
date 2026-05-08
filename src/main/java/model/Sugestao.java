package model;

import java.time.LocalDateTime;

public class Sugestao {

    private int id;
    private String titulo;
    private String descricao;
    private StatusSugestao status;
    private LocalDateTime dataEnvio;
    private Usuario proponente;
    private ProjetoExtensao projetoVinculado;
    private String justificativa;

     public Sugestao(){}
    
    public Sugestao(int id, String titulo, String descricao,
            Usuario proponente, ProjetoExtensao projetoVinculado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.proponente = proponente;
        this.projetoVinculado = projetoVinculado;
        this.status = StatusSugestao.PENDENTE;
        this.dataEnvio = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusSugestao getStatus() {
        return status;
    }

    public void setStatus(StatusSugestao status) {
        this.status = status;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Usuario getProponente() {
        return proponente;
    }

    public void setProponente(Usuario proponente) {
        this.proponente = proponente;
    }

    public ProjetoExtensao getProjetoVinculado() {
        return projetoVinculado;
    }

    public void setProjetoVinculado(ProjetoExtensao projeto) {
        this.projetoVinculado = projeto;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
