package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Paciente;
import br.com.uniamerica.api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/{idPaciente}")
    public ResponseEntity<Paciente> findById(@PathVariable("idPaciente")Long idPaciente)
    {
        return ResponseEntity.ok().body(this.pacienteService.findById(idPaciente).get());
    }

    @GetMapping
    public ResponseEntity<Page<Paciente>> findAll(Pageable pageable)
    {
        return ResponseEntity.ok().body(this.pacienteService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Paciente paciente)
    {
        try{
            this.pacienteService.save(paciente);
            return ResponseEntity.ok().body(("Paciente Cadastrada com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{idPaciente}")
    public ResponseEntity<?> update(@PathVariable("idPaciente") Long idPaciente,
                                    @RequestBody Paciente paciente)
    {
        try{
            this.pacienteService.update(paciente, idPaciente);
            return ResponseEntity.ok().body(("Paciente Atualizado com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/status/{idPaciente}")
    public ResponseEntity<?> updateStatus(@PathVariable("idPaciente") Long idPaciente,
                                          @RequestBody Paciente paciente)
    {
        try{
            this.pacienteService.updateStatus(idPaciente, paciente);
            return ResponseEntity.ok().body(("Paciente Desativado com Sucesso!"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Paciente não existe no banco!");
        }
    }
}
