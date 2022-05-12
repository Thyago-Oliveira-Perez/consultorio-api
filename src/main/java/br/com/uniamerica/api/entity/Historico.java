package br.com.uniamerica.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "historicos", schema = "public")
public class Historico extends AbstractEntity {

    @Getter @Setter
    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAgenda statusAgenda;

    @Getter @Setter
    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Getter @Setter
    @JoinColumn(name = "id_secretatia")
    @ManyToOne(fetch = FetchType.LAZY)
    private Secretaria secretaria;

    @Getter @Setter
    @JoinColumn(name = "id_paciente")
    @ManyToOne(fetch = FetchType.LAZY)
    private Paciente paciente;

    @Getter @Setter
    @JoinColumn(name = "id_agenda", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Agenda agenda;

}
