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
import java.util.Locale;
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
    public void updateStatus(Long idAgenda, StatusAgenda status, Secretaria secretaria) {

        Optional<Agenda> agendaEntity = this.agendaRepository.findById(idAgenda);

        if(agendaEntity.isPresent() && agendaEntity.get().getExcluido() == null){
            switch (status) {
                case rejeitado:
                    this.updateStatusPendenteToRejeitado(agendaEntity.get(), secretaria, status);
                    break;
                case aprovado:
                    this.updateStatusPendenteToAprovado(agendaEntity.get(), secretaria, status);
                    break;

                case cancelado:
                    this.updateStatusPendenteOrAprovadoToCancelado(agendaEntity.get(), secretaria, agendaEntity.get().getPaciente(), status);
                    break;
                case compareceu:
                    this.updateStatusAprovadoToCompareceu(agendaEntity.get(), secretaria, status);
                    break;

                case nao_compareceu:
                    this.updateStatusAprovadoToNaoCompareceu(agendaEntity.get(), secretaria, status);
                    break;
            }
        }
    }

    @Transactional
    public void updateStatusPendenteToRejeitado(Agenda agenda, Secretaria secretaria, StatusAgenda status)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.pendente), "Error: PendenteToRejeitado invalid params");
            Assert.isTrue(status.equals(StatusAgenda.rejeitado), "Error: PendenteToRejeitado invalid params");

            this.agendaRepository.updateStatus(status, agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                                                agenda.getObservacao(), secretaria,
                                                agenda.getPaciente(), agenda);
            historicoRepository.save(historico);
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void updateStatusPendenteToAprovado(Agenda agenda, Secretaria secretaria, StatusAgenda status)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.pendente), "Error: PendenteToAprovado invalid params");
            Assert.isTrue(status.equals(StatusAgenda.aprovado), "Error: PendenteToAprovado invalid params");

            agendaRepository.updateStatus(status, agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);
        }
    }

    @Transactional
    public void updateStatusPendenteOrAprovadoToCancelado(Agenda agenda, Secretaria secretaria, Paciente paciente, StatusAgenda statusAgenda)
    {
        if(secretaria != null || paciente != null)
        {
            if(agenda.getStatus().equals(StatusAgenda.pendente)
                || agenda.getStatus().equals(StatusAgenda.aprovado))
            {
                agendaRepository.updateStatus(statusAgenda, agenda.getId());

                historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                        agenda.getObservacao(), secretaria,
                        agenda.getPaciente(), agenda);

                historicoRepository.save(historico);
            }
        }
    }

    @Transactional
    public void updateStatusAprovadoToCompareceu(Agenda agenda, Secretaria secretaria, StatusAgenda statusAgenda)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.aprovado), "Error: AprovadoToCompareceu");
            Assert.isTrue(this.dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Error: AprovadoToCompareceu - Data Inválida");

            agendaRepository.updateStatus(statusAgenda, agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);

        }
    }

    @Transactional
    public void updateStatusAprovadoToNaoCompareceu(Agenda agenda, Secretaria secretaria, StatusAgenda statusAgenda)
    {
        if(secretaria != null)
        {
            Assert.isTrue(agenda.getStatus().equals(StatusAgenda.aprovado), "Error: AprovadoToNaoCompareceu");
            Assert.isTrue(this.dataPassada(agenda.getDataDe(), agenda.getDataAte()), "Error: AprovadoToNaoCompareceu - Data Inválida");

            agendaRepository.updateStatus(statusAgenda, agenda.getId());

            historico = new Historico(LocalDateTime.now(), agenda.getStatus(),
                    agenda.getObservacao(), secretaria,
                    agenda.getPaciente(), agenda);

            historicoRepository.save(historico);
        }
    }

    public Optional<Agenda> findById(Long id){
        return this.agendaRepository.findById(id);
    }


    public Page<Especialidade> findByNamePaciente(Pageable pageable, String name){
        String newName = name.toLowerCase(Locale.ROOT);
        System.out.println(newName);
        return this.agendaRepository.findAllByNamePaciente(newName, pageable);
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
        }
        else
        {
            Assert.isTrue(horarioValido(agenda.getDataDe()), "Horario de inválido");
            Assert.isTrue(horarioValido(agenda.getDataAte()), "Horario ate inválido");
            Assert.isTrue(horariosMedicosEPacientes(agenda), "Conflito de horario medico e paciente");
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
        Assert.isTrue(this.horarioValido(agenda.getDataDe()), "Horário De inválido");
        Assert.isTrue(this.horarioValido(agenda.getDataAte()), "Horário Ate inválido");
        Assert.isTrue(this.diaValido(agenda.getDataDe()), "Data de inválida");
        Assert.isTrue(this.diaValido(agenda.getDataAte()), "Data até inválida");
        //Assert.isTrue(this.horariosMedicosEPacientes(agenda), "Conflito de horario medico e paciente");
    }

}
