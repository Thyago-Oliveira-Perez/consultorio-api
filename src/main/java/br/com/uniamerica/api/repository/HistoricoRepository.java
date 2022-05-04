package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 05/04/2022
 * @version 1.0.0
 */
@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> { }
