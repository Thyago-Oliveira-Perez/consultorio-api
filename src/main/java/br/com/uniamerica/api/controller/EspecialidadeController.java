package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.Especialidade;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 07/04/2022
 * @version 1.0.0
 */
@Controller
@RequestMapping("/api/especialidades")
public class EspecialidadeController {

    @Autowired
    public EspecialidadeRepository especialidadeRepository;

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Especialidade>> listAllEspecialidades(){
        return new ResponseEntity<>(especialidadeRepository.findAll(), HttpStatus.OK) ;
    }

    /**
     *
     * @param especialidade
     * @return
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Especialidade especialidade){
        especialidadeRepository.save(especialidade);
        return new ResponseEntity<>("Registro Cadastrado", HttpStatus.OK);
    }
}
