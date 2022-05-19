package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Secretaria;
import br.com.uniamerica.api.repository.SecretariaRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SecretariaService {

    @Autowired
    private SecretariaRepository secretariaRepository;

    public Optional<Secretaria> findById(Long id){
        return secretariaRepository.findById(id);
    }

    public Page<Secretaria> listAll(Pageable pageable){
        return secretariaRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Secretaria secretaria){
        this.secretariaRepository.save(secretaria);
    }

    @Transactional
    public void update(Secretaria secretaria, Long id){
        if(id == secretaria.getId()){
            this.secretariaRepository.save(secretaria);
        }else{
            throw  new RuntimeException();
        }
    }

    @Transactional
    public void updateStatus(Secretaria secretaria, Long id){
        if (id == secretaria.getId()) {
            this.secretariaRepository.updateStatus(secretaria.getExcluido(), secretaria.getId());
        }else {
            throw new RuntimeException();
        }
    }
}
