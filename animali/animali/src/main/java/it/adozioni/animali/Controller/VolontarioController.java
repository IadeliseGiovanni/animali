package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Dto.VolontarioDto;
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

    @GetMapping("/all")
    public ResponseEntity<List<VolontarioDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
//
    // 🔹 1 - INSERT
    @PostMapping("/insert")
    public VolontarioDto insert(@RequestBody VolontarioDto dto) {
        return service.insert(dto);
    }

    // 🔹 3 - GET BY ID
    @GetMapping("/read")
    public VolontarioDto read(@RequestParam("id") Integer id) {
        return service.read(id);
    }

    // 🔹 4 - UPDATE
    @PutMapping("/update")
    public VolontarioDto update(@RequestBody VolontarioDto dto) {
        return service.update(dto);
    }

    // 🔹 5 - DELETE
    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Integer id) {
        service.delete(id);
    }

    // 🔹 6 - CERCA PER CF
    @GetMapping("/findByCf")
    public VolontarioDto findByCf(@RequestParam("cf") String cf) {
        return service.findByCfDto(cf);
    }

    // 🔹 7 - CERCA PER NOME
    @GetMapping("/findByNome")
    public List<VolontarioDto> findByNome(@RequestParam("nome") String nome) {
        return service.findByNomeDto(nome);
    }

    // 🔹 8 - CERCA PER COGNOME
    @GetMapping("/findByCognome")
    public List<VolontarioDto> findByCognome(@RequestParam("cognome") String cognome) {
        return service.findByCognomeDto(cognome);
    }

    // 🔹 9 - CERCA PER TURNO
    @GetMapping("/findByTurno")
    public List<VolontarioDto> findByTurno(@RequestParam("turno") String turno) {
        return service.findByTurnoDto(turno);
    }

    // 🔹 10 - SEARCH (LIKE)
    @GetMapping("/search")
    public List<VolontarioDto> search(@RequestParam("keyword") String keyword) {
        return service.searchByKeyword(keyword);
    }
}
