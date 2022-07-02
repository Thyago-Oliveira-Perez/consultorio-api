package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Especialidade;
import br.com.uniamerica.api.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/{idEspecialidade}")
    public ResponseEntity<Especialidade> findById(@PathVariable("idEspecialidade")Long idEspecialidade)
    {
        return ResponseEntity.ok().body(this.especialidadeService.findById(idEspecialidade).get());
    }

    @GetMapping
    public ResponseEntity<Page<Especialidade>> findAll(Pageable pageable)
    {
        return ResponseEntity.ok().body(this.especialidadeService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Especialidade especialidade)
    {
        try{
            this.especialidadeService.insert(especialidade);
            return ResponseEntity.ok().body("Especialidade Cadastrada com Sucesso!");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{idEspecialidade}")
    public ResponseEntity<?> update(@PathVariable("idEspecialidade") Long idEspecialidade,
                                    @RequestBody Especialidade especialidade)
    {
        try{
            this.especialidadeService.update(idEspecialidade, especialidade);
            return ResponseEntity.ok().body("Especialidade Atualizada com Sucesso!");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{idEspecialidade}")
    public ResponseEntity<?> updateStatus(@PathVariable("idEspecialidade") Long idEspecialidade)
    {
        try{
            this.especialidadeService.updateStatus(idEspecialidade);
            return ResponseEntity.ok().body("Especialidade Desativada com Sucesso!");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body("Especialidade não existe no banco.");
        }
    }
}
