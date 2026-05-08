package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma sugestão enviada por um usuário vinculada a um projeto.
 * RF02 - Submissão de Ideia
 * RF03 - Painel de Gestão (controle de status)
 * RF04 - Histórico de Sugestões
 */
public class Sugestao {

    private int             id;
    private String          titulo;
    private String          descricao;
    private StatusSugestao  status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime   dataEnvio;

    private Usuario         proponente;
    private ProjetoExtensao projetoVinculado;
    private String          justificativa;

    public Sugestao() {
        this.status    = StatusSugestao.PENDENTE;
        this.dataEnvio = LocalDateTime.now();
    }

    public Sugestao(int id, String titulo, String descricao,
                    Usuario proponente, ProjetoExtensao projetoVinculado) {
        this.id               = id;
        this.titulo           = titulo;
        this.descricao        = descricao;
        this.proponente       = proponente;
        this.projetoVinculado = projetoVinculado;
        this.status           = StatusSugestao.PENDENTE;
        this.dataEnvio        = LocalDateTime.now();
    }

    /**
     * Altera o status da sugestão e registra a justificativa do gestor.
     */
    public void alterarStatus(StatusSugestao novoStatus, String justificativa) {
        this.status        = novoStatus;
        this.justificativa = justificativa;
    }

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public int getId()             { return id; }
    public void setId(int id)      { this.id = id; }

    public String getTitulo()                  { return titulo; }
    public void setTitulo(String titulo)       { this.titulo = titulo; }

    public String getDescricao()               { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusSugestao getStatus()                  { return status; }
    public void setStatus(StatusSugestao status)       { this.status = status; }

    public LocalDateTime getDataEnvio()                    { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio)      { this.dataEnvio = dataEnvio; }

    public Usuario getProponente()                     { return proponente; }
    public void setProponente(Usuario proponente)      { this.proponente = proponente; }

    public ProjetoExtensao getProjetoVinculado()                       { return projetoVinculado; }
    public void setProjetoVinculado(ProjetoExtensao projetoVinculado)  { this.projetoVinculado = projetoVinculado; }

    public String getJustificativa()                   { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Sugestao{id=" + id
                + ", titulo='" + titulo + "'"
                + ", status=" + status
                + ", dataEnvio=" + (dataEnvio != null ? dataEnvio.format(fmt) : "N/A")
                + ", proponente=" + (proponente != null ? proponente.getNome() : "N/A")
                + ", projeto=" + (projetoVinculado != null ? projetoVinculado.getNome() : "N/A")
                + "}";
    }
}
