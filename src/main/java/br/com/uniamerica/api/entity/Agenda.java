package br.com.uniamerica.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendas", schema = "public")
public class Agenda extends AbstractEntity {

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAgenda status;

    @Getter @Setter
    @Column(name="observacoes")
    private String observacao;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Paciente paciente;

    @Getter @Setter
    @JoinColumn(name = "id_secretaria")
    @ManyToOne(fetch = FetchType.LAZY)
    private Secretaria secretaria;

    @Getter @Setter
    @JoinColumn(name = "id_medico", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Medico medico;

}
