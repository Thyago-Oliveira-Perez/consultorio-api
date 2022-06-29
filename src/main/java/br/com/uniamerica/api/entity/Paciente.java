package br.com.uniamerica.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "pacientes", schema = "public")
public class Paciente extends Pessoa{

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atendimento", nullable = false, length = 20)
    private TipoAtendimento tipoAtendimento;

    @Getter @Setter
    @Column(name = "numero_cartao_convenio", length = 20, unique = true)
    private String numeroCartaoConvenio;

    @Getter @Setter
    @Column(name = "data_vencimento")
    private LocalDateTime dataVencimento;

    @Getter @Setter
    @JoinColumn(name = "id_convenio")
    @ManyToOne(fetch = FetchType.EAGER)
    private Convenio convenio;

}
