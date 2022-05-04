package br.com.uniamerica.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
@MappedSuperclass
@NoArgsConstructor
public abstract class Pessoa extends AbstractEntity {

    @Getter @Setter
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Getter @Setter
    @Column(name = "telefone", nullable = false, length = 16)
    private String telefone;

    @Getter @Setter
    @Column(name = "celular", nullable = false, length = 100)
    private String celular;

    @Getter @Setter
    @Column(name = "nacionalidade", nullable = false, length = 50)
    private String nacionalidade;

    @Getter @Setter
    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Getter @Setter
    @Column(name = "rg", nullable = false, length = 12)
    private String rg;

    @Getter @Setter
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Getter @Setter
    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Getter @Setter
    @Column(name = "senha", nullable = false, length = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false, length = 20)
    private Sexo sexo;

    /**
     * @see AbstractEntity#AbstractEntity(Long) 
     *
     * @param id
     * @param nome
     */
    public Pessoa(Long id, String nome){
        super(id);
        this.nome = nome;
    }

}
