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
            "AND (agenda.medico = :medico OR agenda.paciente = :paciente) " +
            "AND agenda.id <> :agenda")
    public List<Agenda> conflitoMedicoPaciente(
            @Param("agenda") Long idAgenda,
            @Param("datade") LocalDateTime dataDe,
            @Param("dataAte") LocalDateTime dataAte,
            @Param("medico") Long idMedico,
            @Param("paciente") Long idPaciente
    );

    @Modifying
    @Query("UPDATE Agenda agenda " +
            "SET agenda.status = :agendaStatus " +
            "WHERE agenda.id = :idAgenda")
    public void updateStatus(@Param("agendaStatus") StatusAgenda agendaStatus,
                             @Param("idAgenda") Long idAgenda);

}
