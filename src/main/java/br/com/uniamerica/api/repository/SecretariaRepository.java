package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {

    @Modifying
    @Query("UPDATE Secretaria secretaria " +
            "SET secretaria.excluido = : dataExcluido " +
            "WHERE secretaria.id = : idSecretaria")

    public void updateStatus(@Param("dataExcluido")LocalDateTime dataExcluido,
                             @Param("idSecretaria") Long id);
}
