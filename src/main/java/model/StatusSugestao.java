package model;

/**
 * Representa os possíveis estados de uma sugestão no sistema.
 */
public enum StatusSugestao {
    PENDENTE("Pendente"),
    EM_ANALISE("Em Análise"),
    APROVADA("Aprovada"),
    RECUSADA("Recusada");

    private final String descricao;

    StatusSugestao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
