package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.*;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.lang.model.element.PackageElement;
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

    public void insert(Agenda agenda, Secretaria secretaria){
        this.validationInsert(agenda, secretaria);
        this.agendaRepository.save(agenda);
    }

    public void update(Agenda agenda){
        this.validationUpdate(agenda);
        this.saveTransaction(agenda);
    }

    public void updateStatusToRejeitado(Agenda agenda, Secretaria secretaria)
    {
        this.updateStatusPendenteToRejeitado(agenda, secretaria);
        this.saveTransaction(agenda);
    }

    public void updateStatusToAprovado(Agenda agenda, Secretaria secretaria)
    {
        this.updateStatusPendenteToAprovado(agenda, secretaria);
        this.saveTransaction(agenda);
    }

    public void updateStatusToCancelado(Agenda agenda, Secretaria secretaria, Paciente paciente)
    {
        this.updateStatusPendenteOrAprovadoToCancelado(agenda, secretaria, paciente);
        this.saveTransaction(agenda);
    }

    public void updateStatusToCompareceu(Agenda agenda, Secretaria secretaria)
    {
        this.updateStatusAprovadoToCompareceu(agenda, secretaria);
    }

    public void updateStatusToNaoCompareceu(Agenda agenda, Secretaria secretaria)
    {
        this.updateStatusAprovadoToNaoCompareceu(agenda, secretaria);
        this.saveTransaction(agenda);
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
            Assert.isTrue(dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");

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
            Assert.isTrue(dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");

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
            validacoesPadroes(agenda);
        }else
        {
            Assert.isTrue(horarioValido(agenda.getDataDe()), "Assets = false");
            Assert.isTrue(horarioValido(agenda.getDataAte()), "Assets = false");
            Assert.isTrue(horariosMedicosEPacientes(agenda), "Assets = false");
        }
    }

    public void validationInsert(Agenda agenda, Secretaria secretaria)
    {
        if(secretaria != null)
        {
            validacoesPadroes(agenda);
            agenda.setStatus(StatusAgenda.aprovado);
        }else
        {
            validacoesPadroes(agenda);
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
        Assert.isTrue(!agenda.getEncaixe(), "Assets = false");
        Assert.isTrue(dataValida(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");
        Assert.isTrue(horarioValido(agenda.getDataDe()), "Assets = false");
        Assert.isTrue(horarioValido(agenda.getDataAte()), "Assets = false");
        Assert.isTrue(diaValido(agenda.getDataDe()), "Assets = false");
        Assert.isTrue(diaValido(agenda.getDataAte()), "Assets = false");
        Assert.isTrue(horariosMedicosEPacientes(agenda), "Assets = false");
    }

}
