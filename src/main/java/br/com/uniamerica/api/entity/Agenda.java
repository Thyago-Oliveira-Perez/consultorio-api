package br.com.uniamerica.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 22/03/2022
 * @version 1.0.0
 */
@Entity
@Table(name = "agendas", schema = "public")
public class Agenda extends AbstractEntity {

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAgenda status;

    @Getter @Setter
    @Column(name = "data_de", nullable = false)
    private LocalDateTime dataDe;

    @Getter @Setter
    @Column(name = "data_ate", nullable = false)
    private LocalDateTime dataAte;

    @Getter @Setter
    @Column(name = "encaixe", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private Boolean encaixe;

    @Getter @Setter
    @JoinColumn(name = "id_paciente", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Paciente paciente;

    @Getter @Setter
    @JoinColumn(name = "id_medico", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Medico medico;

}
