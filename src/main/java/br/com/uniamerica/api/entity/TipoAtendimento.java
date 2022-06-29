package br.com.uniamerica.api.entity;

public enum TipoAtendimento {

    particular("particular"),
        convenio("convênio");

    public final String valor;

    private TipoAtendimento(String valor){
        this.valor = valor;
    }
}
