package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Paciente;
import br.com.uniamerica.api.entity.TipoAtendimento;
import br.com.uniamerica.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public void save(Paciente paciente){
        this.validarFormulario(paciente);
        this.insert(paciente);
    }

    public Optional<Paciente> findById(Long id){
        return this.pacienteRepository.findById(id);
    }

    public Page<Paciente> listAll(Pageable pageable){
        return this.pacienteRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Paciente paciente){
        this.pacienteRepository.save(paciente);
    }

    @Transactional
    public void update(Paciente paciente, Long id){
        if(paciente.getId() == id){
            this.pacienteRepository.save(paciente);
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void updateStatus(Long id, Paciente paciente){
        if(paciente.getId() == id){
            this.pacienteRepository.updateStatus(LocalDateTime.now(), paciente.getId());
        }
    }

    public void validarFormulario(Paciente paciente){

        if(paciente.getTipoAtendimento().equals(TipoAtendimento.convenio)){

            if(paciente.getConvenio() == null || paciente.getConvenio().getId() == null){
                throw new RuntimeException(("Convenio não informado"));
            }
            if(paciente.getNumeroCartaoConvenio() == null){
                throw new RuntimeException(("Número do cartão do convenio não informado"));
            }
            if(paciente.getDataVencimento() == null){
                throw new RuntimeException(("Data de vencimento do cartão não informada"));
            }
            if(paciente.getDataVencimento().compareTo(LocalDateTime.now()) > 0){
                throw new RuntimeException(("Data de vencimento expirada"));
            }

        }else if(paciente.getTipoAtendimento().equals(TipoAtendimento.particular)){
            paciente.setConvenio(null);
            paciente.setNumeroCartaoConvenio(null);
            paciente.setDataVencimento(null);
        }
    }
}
