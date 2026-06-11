package br.edu.utfpr.pb.pw45s.server.model;

public enum OrderStatus {
    AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
    PAGO("Pago"),
    EM_TRANSPORTE("Em transporte"),
    CONCLUIDO("Concluído");

    private final String descricao;

    OrderStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
