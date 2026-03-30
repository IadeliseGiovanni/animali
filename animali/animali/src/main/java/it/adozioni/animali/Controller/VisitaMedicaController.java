package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Service.VisitaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("VisitaMedica")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitaMedicaController extends AbstractController<VisitaMedicaDto> {

    @Autowired
    private VisitaMedicaService service;

    @GetMapping("/findByNome")
    public List<VisitaMedicaDto> findByNome(String nome) {
        return service.findByNome(nome);
    }
}
