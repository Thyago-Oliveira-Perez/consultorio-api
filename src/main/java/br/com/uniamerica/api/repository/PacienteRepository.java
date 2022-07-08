package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Modifying
    @Query("UPDATE Paciente paciente " +
            "SET paciente.ativo = :newStatus " +
            "WHERE paciente.id = :idPaciente")
    public void updateStatus(@Param("newStatus")boolean newStatus,
                             @Param("idPaciente")Long idPaciente);

    @Query("FROM Paciente paciente WHERE paciente.nome like :name")
    public Page findAllByName(@Param("name") String name, Pageable pageable);
}
