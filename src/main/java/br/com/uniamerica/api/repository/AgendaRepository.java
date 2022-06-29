package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.StatusAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long>
{

    @Query("FROM Agenda agenda " +
            "WHERE (:datade BETWEEN agenda.dataDe AND agenda.dataAte " +
            "OR :dataAte BETWEEN agenda.dataDe AND agenda.dataAte) " +
            "AND (agenda.medico.id = :id_medico OR agenda.paciente.id = :id_paciente) " +
            "AND agenda.id <> :id_agenda")
    public List<Agenda> conflitoMedicoPaciente(
            @Param("id_agenda") Long idAgenda,
            @Param("datade") LocalDateTime dataDe,
            @Param("dataAte") LocalDateTime dataAte,
            @Param("id_medico") Long idMedico,
            @Param("id_paciente") Long idPaciente
    );

    @Modifying
    @Query("UPDATE Agenda agenda " +
            "SET agenda.status = :agendaStatus " +
            "WHERE agenda.id = :idAgenda")
    public void updateStatus(@Param("agendaStatus") StatusAgenda agendaStatus,
                             @Param("idAgenda") Long idAgenda);

}
