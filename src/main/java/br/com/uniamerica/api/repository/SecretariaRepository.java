package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Secretaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "SET secretaria.excluido = :dataExcluido, secretaria.ativo = false " +
            "WHERE secretaria.id = :idSecretaria")

    public void updateStatus(@Param("dataExcluido")LocalDateTime dataExcluido,
                             @Param("idSecretaria") Long id);

    @Query("FROM Secretaria secretaria WHERE secretaria.nome like :name")
    public Page findAllByName(@Param("name") String name, Pageable pageable);

    @Query("FROM Secretaria secretaria WHERE secretaria.ativo = :ativo")
    public Page findAllHabilitados(@Param("ativo") Boolean ativo, Pageable pageable);
}
