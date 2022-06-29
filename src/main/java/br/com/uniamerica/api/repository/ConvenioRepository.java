package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long> {

    @Modifying
    @Query("UPDATE Convenio convenio " +
            "SET convenio.excluido = :dataExcluido " +
            "WHERE convenio.id = :idConvenio")
    public void updateStatus(@Param("dataExcluido") LocalDateTime dataExcluido,
                             @Param("idConvenio") Long id);
}
