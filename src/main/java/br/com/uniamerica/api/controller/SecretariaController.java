package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Secretaria;
import br.com.uniamerica.api.service.SecretariaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@Controller
@RequestMapping("/secretarias")
public class SecretariaController {

    @Autowired
    public SecretariaService secretariaService;

    @GetMapping("/{idSecretaria}")
    public ResponseEntity<Secretaria> getById(@PathVariable("idSecretaria")Long idSecretaria)
    {
        return ResponseEntity.ok().body(this.secretariaService.findById(idSecretaria).get());
    }

    @GetMapping
    public ResponseEntity<Page<Secretaria>> findAll(Pageable pageable)
    {
        return ResponseEntity.ok().body(this.secretariaService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Secretaria secretaria)
    {
        try{
            this.secretariaService.insert(secretaria);
            return ResponseEntity.ok().body(("Secretaria Cadastrada com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idSecretaria}")
    public ResponseEntity<?> update(@PathVariable("idSecretaria")Long idSecretaria,
                                    @RequestBody Secretaria secretaria)
    {
        try{
            this.secretariaService.update(secretaria, idSecretaria);
            return ResponseEntity.ok().body(("Secretaria Atualizada com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{idSecretaria}")
    public ResponseEntity<?> updateStatus(@PathVariable("idSecretaria")Long idSecretaria,
                                          @RequestBody Secretaria secretaria)
    {
        try{
            this.secretariaService.updateStatus(secretaria, idSecretaria);
            return ResponseEntity.ok().body(("Secretaria Atualizada com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
