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

    @GetMapping("/reglas")
    public ResponseEntity<?> getReglasPDF() throws Exception {
        try {
            Path path = Paths.get("src/main/resources/static/reglas-ruleta-americana.pdf");
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichero no encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<List<Ruleta>> getAllRuletas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletas();
        return new ResponseEntity<>(ruletas, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Ruleta>> getAllRuletasAbiertas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletasAbiertas(true);
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
    public ResponseEntity<Ruleta> crearRuleta() {
        return new ResponseEntity<>(ruletaService.crearRuleta(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> apostar(@RequestHeader("Authorization") String token, @PathVariable String id,
            @RequestBody Apuesta apuesta) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                if (usuario.getCoins() >= apuesta.getCantidad()) {
                    Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
                    if (ruletaOpt.isPresent()) {
                        Ruleta ruleta = ruletaOpt.get();
                        if (ruleta.isRuletaAbierta()) {
                            try {
                                Ruleta result = ruletaService.apostar(ruleta, usuario, apuesta);
                                usuarioService.pagar(usuario.getId(), apuesta.getCantidad());
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tienes suficientes monedas");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> cerrarRuleta(@PathVariable String id) {
        Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
        if (ruletaOpt.isPresent()) {
            try {
                return new ResponseEntity<>(ruletaService.cerrarRuleta(ruletaOpt.get()), HttpStatus.OK);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al cerrar: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruleta no encontrada");
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> girarRuleta(@PathVariable String id) {
        Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
        if (ruletaOpt.isPresent()) {
            try {
                Ruleta ruleta = ruletaService.girarRuleta(ruletaOpt.get());
                for (Object[] apuesta : ruleta.determinarGanadores()) {
                    Apostador apostador = (Apostador) apuesta[0];
                    Integer premio = (int) apuesta[1];
                    usuarioService.cobrar(apostador.getId(), premio);
                }
                return new ResponseEntity<>(ruleta, HttpStatus.OK);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al girar: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruleta no encontrada");
        }
    }
}