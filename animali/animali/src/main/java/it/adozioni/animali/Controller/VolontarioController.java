package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Service.AdottanteService; // Obbligatorio per l'Abstract
import it.adozioni.animali.Service.VolontarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Volontario")
@CrossOrigin(origins = "http://localhost:4200")
public class VolontarioController extends AbstractController<VolontarioDto> {

    @Autowired
    private VolontarioService service;

    @Override
    @SuppressWarnings("unchecked")
    protected AdottanteService getService() {
        // Restituiamo il nostro service castato a AdottanteService per "mentire" all'abstract
        // Se AdottanteService è una classe specifica, questo è l'unico modo per compilare.
        return (AdottanteService) (Object) service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<VolontarioDto>> getAll() {
        // Usiamo il metodo reale creato nel service dei volontari
        return ResponseEntity.ok(service.findAllVolontari());
    }

    @PostMapping("/insert")
    public VolontarioDto insert(@RequestBody VolontarioDto dto) {
        return service.insert(dto);
    }

    @GetMapping("/read")
    public VolontarioDto read(@RequestParam("id") Integer id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public VolontarioDto update(@RequestBody VolontarioDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Integer id) {
        service.delete(id);
    }

    @GetMapping("/findByCf")
    public VolontarioDto findByCf(@RequestParam("cf") String cf) {
        return service.findByCfDto(cf);
    }

    @GetMapping("/findByNome")
    public List<VolontarioDto> findByNome(@RequestParam("nome") String nome) {
        return service.findByNomeDto(nome);
    }

    @GetMapping("/findByCognome")
    public List<VolontarioDto> findByCognome(@RequestParam("cognome") String cognome) {
        return service.findByCognomeDto(cognome);
    }

    @GetMapping("/findByTurno")
    public List<VolontarioDto> findByTurno(@RequestParam("turno") String turno) {
        return service.findByTurnoDto(turno);
    }

    @GetMapping("/search")
    public List<VolontarioDto> search(@RequestParam("keyword") String keyword) {
        return service.searchByKeyword(keyword);
    }
}