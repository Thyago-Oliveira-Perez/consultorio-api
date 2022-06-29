package br.com.uniamerica.api.entity;

public enum StatusAgenda {

    pendente("pendente"),
        aprovado("aprovado"),
            rejeitado("rejeitado"),
                cancelado("cancelado"),
                    compareceu("compareceu"),
                        nao_compareceu("não compareceu");

    public final String valor;

    private StatusAgenda(String valor){
        this.valor = valor;
    }

}
