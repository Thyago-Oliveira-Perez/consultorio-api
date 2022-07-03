package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Modifying
    @Query("UPDATE Medico medico " +
            "SET medico.excluido = :dataExcluido " +
            "WHERE medico.id = :idMedico")

    public void updateStatus(@Param("dataExcluido")LocalDateTime dataExcluido,
                             @Param("idMedico") Long id);

    @Query("FROM Medico medico WHERE medico.nome like :name")
    public Page findAllByName(@Param("name") String name, Pageable pageable);
}
