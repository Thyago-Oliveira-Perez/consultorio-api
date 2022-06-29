package br.com.uniamerica.api.entity;

public enum Sexo {

    masculino("masculino"),
        feminino("feminino"),
            outro("outro");

    public final String valor;

    private Sexo(String valor){
        this.valor = valor;
    }
}
