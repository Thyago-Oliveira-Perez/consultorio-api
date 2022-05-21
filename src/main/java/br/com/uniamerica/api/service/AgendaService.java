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

    public void save(Agenda agenda){
        this.validarFormInsert(agenda);
        this.agendaRepository.save(agenda);
    }

    public void update(Agenda agenda){
        this.validationUpdate(agenda);
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

    public void validationUpdate(Agenda agenda){

        if(!agenda.getEncaixe())
        {
            Assert.isTrue(dataValida(agenda.getDataDe(), agenda.getDataAte()), "Assets = false");
            Assert.isTrue(horarioValido(agenda.getDataDe()), "Assets = false");
            Assert.isTrue(horarioValido(agenda.getDataAte()), "Assets = false");
            Assert.isTrue(diaValido(agenda.getDataDe()), "Assets = false");
            Assert.isTrue(diaValido(agenda.getDataAte()), "Assets = false");
            Assert.isTrue(horariosMedicosEPacientes(agenda), "Assets = false");
        }else
        {
            Assert.isTrue(horarioValido(agenda.getDataDe()), "Assets = false");
            Assert.isTrue(horarioValido(agenda.getDataAte()), "Assets = false");
            Assert.isTrue(horariosMedicosEPacientes(agenda), "Assets = false");
        }
    }

    public void validarFormInsert(Agenda agenda){}

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

    public boolean horariosMedicosEPacientes(Agenda agenda)
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

}
