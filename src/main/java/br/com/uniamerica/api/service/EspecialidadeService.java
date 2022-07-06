package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Especialidade;
import br.com.uniamerica.api.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public Optional<Especialidade> findById(Long id){
        return this.especialidadeRepository.findById(id);
    }

    public Page<Especialidade> findByName(Pageable pageable, String name){
        return this.especialidadeRepository.findAllByName(name, pageable);
    }
    public Page<Especialidade> listAll(Pageable pageable){
        return this.especialidadeRepository.findAll(pageable);
    }

    @Transactional
    public void update(Long id, Especialidade especialidade){
        System.out.println(id + "   " +especialidade.getId());
        if (id.equals(especialidade.getId())) {
            this.especialidadeRepository.save(especialidade);
        }
        else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void insert(Especialidade especialidade){
        this.especialidadeRepository.save(especialidade);
    }

    @Transactional
    public void updateStatus(Long id){

        Optional<Especialidade> especialidadeEntity = this.especialidadeRepository.findById(id);

        if (especialidadeEntity.isPresent() && especialidadeEntity.get().getExcluido() == null) {
            this.especialidadeRepository.updateStatus(LocalDateTime.now(), id);
        }
        else {
            throw new RuntimeException();
        }
    }
}
