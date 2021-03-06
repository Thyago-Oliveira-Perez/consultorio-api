package br.com.uniamerica.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "especialidades", schema = "public")
public class Especialidade extends AbstractEntity {

    @Getter @Setter
    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

}
