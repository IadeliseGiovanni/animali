package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Model.VisitaMedica;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.VisitaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/VisitaMedica")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitaMedicaController extends AbstractController<VisitaMedicaDto> {

    @Autowired
    private VisitaMedicaService service;

    @GetMapping("/all")
    public ResponseEntity<List<VisitaMedicaDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/findByData")
    public List<VisitaMedicaDto> findByData(@RequestParam("data") LocalDateTime data) {
        return service.findByData(data);
    }
//
    @GetMapping("/findByEsito")
    public List<VisitaMedicaDto> findByEsito(@RequestParam("esito") String esito) {
        return service.findByEsito(esito);
    }

    @GetMapping("/findByVeterinario")
    public List<VisitaMedicaDto> findByVeterinario(@RequestParam("veterinario") String veterinario) {
        return service.findByVeterinario(veterinario);
    }

    @GetMapping("/findByDataAndVeterinario")
    public List<VisitaMedicaDto> findByDataAndVeterinario(@RequestParam("data") LocalDateTime data, @RequestParam("veterinario") String veterinario) {
        return service.findByDataAndVeterinario(data, veterinario);
    }

    @GetMapping("/findByDataAndEsito")
    public List<VisitaMedicaDto> findByDataAndEsito(@RequestParam("data") LocalDateTime data, @RequestParam("esito") String esito) {
        return service.findByDataAndEsito(data, esito);
    }

    @PostMapping("/insert")
    public VisitaMedicaDto insert(@RequestBody VisitaMedicaDto dto) {
        return service.insert(dto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Integer id) {
        service.delete(id);
    }

    @Override
    protected AdottanteService getService() {
        return null;
    }
}
