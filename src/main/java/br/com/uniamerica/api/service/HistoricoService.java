package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Historico;
import br.com.uniamerica.api.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    public Optional<Historico> findById(Long id){
        return this.historicoRepository.findById(id);
    }

    public Page<Historico> listAll(Pageable pageable){
        return this.historicoRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Historico historico){
        this.historicoRepository.save(historico);
    }

    public void validacaoInsert(Historico historico){

        if(historico.getStatusAgenda() == null){
            throw new RuntimeException(("Status de agenda inválido"));
        }
        if(historico.getSecretaria() == null){
            throw new RuntimeException(("Secretaria não informada"));
        }
        if(historico.getPaciente() == null){
            throw new RuntimeException(("Paciente não informado"));
        }

    }

    public void saveInsertHistorico(Historico historico){
        this.validacaoInsert(historico);
        this.insert(historico);
    }

}
