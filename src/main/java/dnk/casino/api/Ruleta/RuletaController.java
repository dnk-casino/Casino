package dnk.casino.api.Ruleta;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

@RestController
@RequestMapping("/api/ruleta")
public class RuletaController {

    @Autowired
    private RuletaService ruletaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Ruleta>> getAllRuletas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletas();
        return new ResponseEntity<>(ruletas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRuletaById(@PathVariable String id) {
        Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
        if (ruletaOpt.isPresent()) {
            return new ResponseEntity<>(ruletaOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ruleta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Ruleta> crearRuleta(@RequestBody Ruleta ruleta) {
        Ruleta newRuleta = ruletaService.crearRuleta();
        return new ResponseEntity<>(newRuleta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> apostar(@RequestHeader("Authorization") String token, @PathVariable String id,
            @RequestBody Apuesta apuesta) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
                if (ruletaOpt.isPresent()) {
                    Ruleta ruleta = ruletaOpt.get();
                    if (ruleta.isRuletaAbierta()) {
                        try {
                            Ruleta result = ruletaService.apostar(ruleta, usuarioOpt.get(), apuesta);
                            return ResponseEntity.ok(result);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error al apostar: " + e.getMessage());
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ruleta cerrada");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruleta no encontrada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @GetMapping("/reglas")
    public ResponseEntity<Resource> getReglasPDF() throws Exception {
        Path path = Paths.get("src/main/resources/static/reglas-ruleta-americana.pdf");
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}