package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Convenio;
import br.com.uniamerica.api.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConvenioService {

    @Autowired
    private ConvenioRepository convenioRepository;

    public Optional<Convenio> findById(Long id){return this.convenioRepository.findById(id);}

    public Page<Convenio> listAll(Pageable pageable)
    {
        return this.convenioRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Convenio convenio){this.convenioRepository.save(convenio);};

    @Transactional
    public void update(Convenio convenio, Long id)
    {
        System.out.println(id + "   " + convenio.getId());

        if(id.equals(convenio.getId())){
            this.convenioRepository.save(convenio);
        }else{
            throw new RuntimeException();
        }
    }

    @Transactional
    public void updateStatus(Long id, Convenio convenio)
    {

        Optional<Convenio> convenioEntity = this.convenioRepository.findById(id);

        if(convenioEntity.isPresent() && convenioEntity.get().getExcluido() == null){
            this.convenioRepository.updateStatus(LocalDateTime.now(), convenio.getId());
        }else{
            throw new RuntimeException();
        }
    }
}
