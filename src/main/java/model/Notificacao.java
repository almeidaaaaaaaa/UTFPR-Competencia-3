package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma notificação enviada ao proponente quando o status
 * da sugestão é alterado.
 * RF05 - Notificações por e-mail.
 */
public class Notificacao {

    private int           id;
    private String        mensagem;
    private boolean       enviada;
    private LocalDateTime dataEnvio;
    private Usuario       destinatario;
    private Sugestao      sugestaoRelacionada;

    public Notificacao() {
        this.dataEnvio = LocalDateTime.now();
        this.enviada   = false;
    }

    public Notificacao(int id, String mensagem, Usuario destinatario,
                       Sugestao sugestaoRelacionada) {
        this.id                  = id;
        this.mensagem            = mensagem;
        this.destinatario        = destinatario;
        this.sugestaoRelacionada = sugestaoRelacionada;
        this.dataEnvio           = LocalDateTime.now();
        this.enviada             = false;
    }

    /**
     * Gera a mensagem padrão de notificação com base no status atual da sugestão.
     */
    public static String gerarMensagem(Sugestao sugestao) {
        return "Olá " + sugestao.getProponente().getNome() + ",\n"
             + "Sua sugestão \"" + sugestao.getTitulo() + "\" foi atualizada para: "
             + sugestao.getStatus().getDescricao() + ".\n"
             + (sugestao.getJustificativa() != null
                ? "Justificativa: " + sugestao.getJustificativa()
                : "");
    }

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public String getMensagem()                 { return mensagem; }
    public void setMensagem(String mensagem)    { this.mensagem = mensagem; }

    public boolean isEnviada()              { return enviada; }
    public void setEnviada(boolean enviada) { this.enviada = enviada; }

    public LocalDateTime getDataEnvio()                { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio)  { this.dataEnvio = dataEnvio; }

    public Usuario getDestinatario()                   { return destinatario; }
    public void setDestinatario(Usuario destinatario)  { this.destinatario = destinatario; }

    public Sugestao getSugestaoRelacionada()            { return sugestaoRelacionada; }
    public void setSugestaoRelacionada(Sugestao s)      { this.sugestaoRelacionada = s; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Notificacao{id=" + id
                + ", destinatario=" + (destinatario != null ? destinatario.getEmail() : "N/A")
                + ", enviada=" + enviada
                + ", dataEnvio=" + (dataEnvio != null ? dataEnvio.format(fmt) : "N/A")
                + "}";
    }
}
