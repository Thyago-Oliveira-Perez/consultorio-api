package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.Historico;
import br.com.uniamerica.api.entity.StatusAgenda;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    private Historico historico;

    public void insert(Agenda agenda, String observacao){
        this.validarFormulario(agenda, observacao);
        this.saveTransaction(agenda);
    }

    public void update(Agenda agenda, String observacao){
        this.validarFormulario(agenda, observacao);
        this.saveTransaction(agenda);
    }

    public Optional<Agenda> findById(Long id){
        return this.agendaRepository.findById(id);
    }

    public Page<Agenda> listAll(Pageable pageable){
        return this.agendaRepository.findAll(pageable);
    }

    @Transactional
    public void saveTransaction(Agenda agenda){
        this.agendaRepository.save(agenda);
    }

    public void validarFormulario(Agenda agenda, String observacao){

        if(agenda.getStatus() == null){

            agenda.setStatus(StatusAgenda.pendente);

            if(agenda.getDataDe() == null){
                throw new RuntimeException(("Data inicial não informada"));
            }
            if(agenda.getDataAte() == null){
                throw new RuntimeException(("Data final não informada"));
            }
            if(agenda.getDataAte().compareTo(LocalDateTime.now()) > 0){
                throw new RuntimeException(("Data inválida"));
            }

            Historico historico = new Historico(
                    LocalDateTime.now(),
                    agenda.getStatus(),
                    observacao, agenda.getSecretaria(),
                    agenda.getPaciente(),
                    agenda);

            historicoRepository.save(historico);

        }else{

            Historico historico = new Historico(
                    LocalDateTime.now(),
                    agenda.getStatus(),
                    observacao, agenda.getSecretaria(),
                    agenda.getPaciente(),
                    agenda);

            historicoRepository.save(historico);
        }
    }

}
