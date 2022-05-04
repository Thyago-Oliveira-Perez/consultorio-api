package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.Medico;
import br.com.uniamerica.api.repository.AgendaRepository;
import br.com.uniamerica.api.repository.MedicoRepository;
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
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    public MedicoRepository medicoRepository;

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Medico>> findAllMedicos(){
        return new ResponseEntity<>(medicoRepository.findAll(), HttpStatus.OK) ;
    }

    /**
     *
     * @return
     */
    @GetMapping("listTable")
    public ResponseEntity<List<Medico>> listTable(){
        return new ResponseEntity<>(medicoRepository.listTable(), HttpStatus.OK) ;
    }

    /**
     *
     * @param medico
     * @return
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Medico medico){
        medicoRepository.save(medico);
        return new ResponseEntity<>("Registro Cadastrado", HttpStatus.OK);
    }

}
