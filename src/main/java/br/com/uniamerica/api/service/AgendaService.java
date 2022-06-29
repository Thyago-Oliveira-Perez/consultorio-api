package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.*;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    private Historico historico;

    public void insert(Agenda agenda){
        this.validationInsert(agenda);
        this.saveTransaction(agenda);
    }

    public void update(Agenda agenda){
        this.validationUpdate(agenda);
        this.saveTransaction(agenda);
    }

    @Transactional
    public void updateStatus(Agenda agenda, StatusAgenda status, Secretaria secretaria) {

        switch (status) {
            case rejeitado:
                this.updateStatusPendenteToRejeitado(agenda, secretaria);
                break;
            case aprovado:
                this.updateStatusPendenteToAprovado(agenda, secretaria);
                break;

            case cancelado:
                this.updateStatusPendenteOrAprovadoToCancelado(agenda, secretaria, agenda.getPaciente());
                break;
            case compareceu:
                this.updateStatusAprovadoToCompareceu(agenda, secretaria);
                break;

            case nao_compareceu:
                this.updateStatusAprovadoToNaoCompareceu(agenda, secretaria);
                break;
        }
    }

    public void updateStatusPendenteToRejeitado(Agenda agenda, Secretaria secretaria)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agendaRepository.findById(agenda.getId()).get().getStatus().equals(StatusAgenda.pendente), "Assert = false");
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.rejeitado), "Assert = false");

            agendaRepository.updateStatus(agenda.getStatus(), agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                                                agenda.getObservacao(), secretaria,
                                                agenda.getPaciente(), agenda);
            historicoRepository.save(historico);
        }
    }

    public void updateStatusPendenteToAprovado(Agenda agenda, Secretaria secretaria)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agendaRepository.findById(agenda.getId()).get().getStatus().equals(StatusAgenda.pendente), "Assert = false");
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.aprovado), "Assert = false");

            agendaRepository.updateStatus(agenda.getStatus(), agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);
        }
    }

    public void updateStatusPendenteOrAprovadoToCancelado(Agenda agenda, Secretaria secretaria, Paciente paciente)
    {
        if(secretaria != null || paciente != null)
        {
            if(agendaRepository.getById(agenda.getId()).getStatus().equals(StatusAgenda.pendente)
                || agendaRepository.getById(agenda.getId()).getStatus().equals(StatusAgenda.aprovado))
            {
                agendaRepository.updateStatus(agenda.getStatus(), agenda.getId());

                historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                        agenda.getObservacao(), secretaria,
                        agenda.getPaciente(), agenda);

                historicoRepository.save(historico);
            }
        }
    }

    public void updateStatusAprovadoToCompareceu(Agenda agenda, Secretaria secretaria)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agendaRepository.getById(agenda.getId()).getStatus().equals(StatusAgenda.aprovado), "Assets = false");
            Assert.isTrue(this.dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");

            agendaRepository.updateStatus(agenda.getStatus(), agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);

        }
    }

    public void updateStatusAprovadoToNaoCompareceu(Agenda agenda, Secretaria secretaria)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agendaRepository.getById(agenda.getId()).getStatus().equals(StatusAgenda.aprovado), "Assets = false");
            Assert.isTrue(this.dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");

            agendaRepository.updateStatus(agenda.getStatus(), agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);
        }
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

    public void validationUpdate(Agenda agenda)
    {
        if(!agenda.getEncaixe())
        {
            this.validacoesPadroes(agenda);
        }else
        {
            Assert.isTrue(horarioValido(agenda.getDataDe()), "Horario de inválido");
            Assert.isTrue(horarioValido(agenda.getDataAte()), "Horario ate inválido");
            //Assert.isTrue(horariosMedicosEPacientes(agenda), "Assets = false");
        }
    }

    public void validationInsert(Agenda agenda)
    {
        if(agenda.getSecretaria() != null)
        {
            this.validacoesPadroes(agenda);
            agenda.setStatus(StatusAgenda.aprovado);
        }else
        {
            this.validacoesPadroes(agenda);
            agenda.setStatus(StatusAgenda.pendente);
        }
    }

    private boolean dataValida(LocalDateTime dataDe, LocalDateTime dataAte)
    {
        if(dataDe.isAfter(LocalDateTime.now())
                &&
           dataAte.isAfter(LocalDateTime.now()))
        {
            if(dataDe.isBefore(dataAte))
            {
                return true;
            }
        }
        return false;
    }

    private boolean dataPassada(LocalDateTime dataDe, LocalDateTime dataAte)
    {
        if(dataDe.isBefore(LocalDateTime.now())
           && dataAte.isBefore(LocalDateTime.now()))
        {
            return true;
        }
        return false;
    }

    private boolean horarioValido(LocalDateTime data)
    {
        if(data.getHour() > 8 && data.getHour() < 12
           ||
           data.getHour() > 14 && data.getHour() < 18)
        {
            return true;
        }
        return false;
    }

    private boolean diaValido(LocalDateTime data)
    {
        return !data.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                &&
                !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                ? false : true;
    }

    private boolean horariosMedicosEPacientes(Agenda agenda)
    {
        if(agendaRepository.conflitoMedicoPaciente(
                agenda.getId(),
                agenda.getDataDe(),
                agenda.getDataAte(),
                agenda.getMedico().getId(),
                agenda.getPaciente().getId()
        ).size() > 0)
        {
            return true;
        }
            return false;
    }

    private void validacoesPadroes(Agenda agenda)
    {
        Assert.isTrue(!agenda.getEncaixe(), "Agenda não é encaixe!");
        Assert.isTrue(this.dataValida(agenda.getDataDe(), agenda.getDataAte()), "Data inválida!");
        Assert.isTrue(this.horarioValido(agenda.getDataDe()), "Horário de inválido");
        Assert.isTrue(this.horarioValido(agenda.getDataAte()), "Horário ate inválido");
        Assert.isTrue(this.diaValido(agenda.getDataDe()), "Data de inválida");
        Assert.isTrue(this.diaValido(agenda.getDataAte()), "Data até inválida");
        Assert.isTrue(this.horariosMedicosEPacientes(agenda), "Conflito de horario medico e paciente");
    }

}
