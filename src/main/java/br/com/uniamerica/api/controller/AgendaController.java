package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping("/{idAgenda}")
    public ResponseEntity<Agenda> findById(@PathVariable("idAgenda") Long idAgenda)
    {
        return ResponseEntity.ok().body(this.agendaService.findById(idAgenda).get());
    }

    @GetMapping
    public ResponseEntity<Page<Agenda>> findAll(Pageable pageable)
    {
        return ResponseEntity.ok().body(this.agendaService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Agenda agenda)
    {
        try
        {
            this.agendaService.save(agenda);
            return ResponseEntity.ok().body("Agenda Cadastrada com Sucesso!");
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idAgenda}")
    public ResponseEntity<?> update(@PathVariable("idAgenda") Long idAgenda,
                                    @RequestBody Agenda agenda)
    {
        try
        {
            this.agendaService.update(agenda);
            return ResponseEntity.ok().body("Agenda Atualizada com Sucesso!");
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{idAgenda}")
    public ResponseEntity<?> updateStatus(@PathVariable("idAgenda") Long idAgenda,
                                          @RequestBody Agenda agenda)
    {
        try
        {
            this.agendaService.update(agenda);
            return ResponseEntity.ok().body("Status da Agenda Atualizada com Sucesso!");
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
