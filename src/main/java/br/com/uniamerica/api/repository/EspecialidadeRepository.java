package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    @Modifying
    @Query("UPDATE Especialidade especialidade " +
            "SET especialidade.ativo = :newStatus " +
            "WHERE " +
            "especialidade.id = :idEspecialidade")

    public void updateStatus(@Param("newStatus") boolean newStatus,
                             @Param("idEspecialidade") Long idEspecialidade);

    @Query("FROM Especialidade especialidade WHERE especialidade.nome like :name")
    public Page findAllByName(@Param("name") String name, Pageable pageable);
}
