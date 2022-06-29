package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Medico;
import br.com.uniamerica.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public Optional<Medico> findById(Long id){return medicoRepository.findById(id);}

    public Page<Medico> listAll(Pageable pageable){return medicoRepository.findAll(pageable);}

    @Transactional
    public void insert(Medico medico){this.medicoRepository.save(medico);}

    @Transactional
    public void update(Medico medico, Long id){
        if(id == medico.getId()) {
            this.medicoRepository.save(medico);
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void updateStatus(Medico medico, Long id){
        if(id == medico.getId()){
            this.medicoRepository.updateStatus(medico.getExcluido(), medico.getId());
        }else{
            throw new RuntimeException();
        }
    }
}
