package br.com.uniamerica.api.entity;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
public enum Sexo {

    masculino("Masculino"),
        feminino("Feminino"),
            outro("Outro");

    public final String valor;

    /**
     *
     * @param valor
     */
    private Sexo(String valor){
        this.valor = valor;
    }
}
