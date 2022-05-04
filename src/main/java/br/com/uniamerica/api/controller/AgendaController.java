package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.repository.AgendaRepository;
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
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    public AgendaRepository agendaRepository;

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Agenda>> listAllAgendas(){
        return new ResponseEntity<>(agendaRepository.findAll(), HttpStatus.OK) ;
    }

    /**
     *
     * @param agenda
     * @return
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody List<Agenda> agenda){
        agendaRepository.saveAll(agenda);
        return new ResponseEntity<>("Registro Cadastrado", HttpStatus.OK);
    }
}
