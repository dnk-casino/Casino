package dnk.casino.api.Ruleta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ruleta")
public class RuletaController {

    @Autowired
    private RuletaService ruletaService;

    @GetMapping
    public ResponseEntity<List<Ruleta>> getAllRuletas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletas();
        return new ResponseEntity<>(ruletas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ruleta> getRuletaById(@PathVariable String id) {
        Ruleta ruleta = ruletaService.obtenerRuleta(id);
        return new ResponseEntity<>(ruleta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ruleta> createRuleta(@RequestBody Ruleta ruleta) {
        Ruleta newRuleta = ruletaService.crearRuleta();
        return new ResponseEntity<>(newRuleta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ruleta> updateRuleta(@RequestHeader("Authorization") String token, @PathVariable String id,
            @RequestBody Apuesta apuesta) {
        Ruleta updatedRuleta = ruletaService.obtenerRuleta(id);

        return new ResponseEntity<>(updatedRuleta, HttpStatus.OK);
    }
}