package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Convenio;
import br.com.uniamerica.api.service.ConvenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/convenios")
public class ConvenioController {

    @Autowired
    private ConvenioService convenioService;

    @GetMapping("/{idConvenio}")
    public ResponseEntity<Convenio> findById(
            @PathVariable("idConvenio")Long idConvenoio)
    {
        return ResponseEntity.ok().body(this.convenioService.findById(idConvenoio).get());
    }

    @GetMapping
    public ResponseEntity<Page<Convenio>> findAll(Pageable pageable)
    {
        return ResponseEntity.ok().body(this.convenioService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Convenio convenio)
    {
        try {
            this.convenioService.insert(convenio);
            return ResponseEntity.ok().body("Convenio salvo com Sucesso!");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{idConvenio}")
    public ResponseEntity<?> update(@PathVariable("idConvenio") Long idConvenio,
                                    @RequestBody Convenio convenio)
    {
        try{
            this.convenioService.update(convenio, idConvenio);
            return ResponseEntity.ok().body("Convenio Atualizado com Sucesso!");
        }catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{idConvenio}")
    public ResponseEntity<?> updateStatus(@PathVariable("idConvenio") Long idConvenio,
                                          @RequestBody Convenio convenio)
    {
        try{
            this.convenioService.updateStatus(idConvenio, convenio);
            return ResponseEntity.ok().body("Convenio Desabilitado com Sucesso!");
        }catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body("Convenio já está desativado.");
        }
    }
}
