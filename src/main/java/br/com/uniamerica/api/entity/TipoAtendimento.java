package br.com.uniamerica.api.entity;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
public enum TipoAtendimento {

    particular("Particular"),
        convenio("Convênio");

    public final String valor;

    /**
     *
     * @param valor
     */
    private TipoAtendimento(String valor){
        this.valor = valor;
    }
}
