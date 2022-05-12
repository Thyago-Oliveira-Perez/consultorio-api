package br.com.uniamerica.api.entity;

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
