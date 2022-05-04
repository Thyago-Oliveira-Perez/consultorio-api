package br.com.uniamerica.api.entity;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
public enum StatusAgenda {

    pendente("Pendente"),
        aprovado("Aprovado"),
            rejeitado("Rejeitado"),
                cancelado("Cancelado"),
                    compareceu("Compareceu"),
                        nao_compareceu("Não Compareceu");

    public final String valor;

    /**
     *
     * @param valor
     */
    private StatusAgenda(String valor){
        this.valor = valor;
    }

}
