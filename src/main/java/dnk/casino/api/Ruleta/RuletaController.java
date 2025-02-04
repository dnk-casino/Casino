package dnk.casino.api.Ruleta;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

/**
 * Controlador de la API de la ruleta.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/api/ruleta")
public class RuletaController {

    /**
     * Servicio de ruletas.
     */
    @Autowired
    private RuletaService ruletaService;

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene el PDF con las reglas de la ruleta.
     * 
     * @return el PDF con las reglas de la ruleta
     */
    @GetMapping("/reglas")
    public ResponseEntity<?> getReglasPDF() throws Exception {
        try {
            Resource resource = new ClassPathResource("static/reglas-ruleta-americana.pdf");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichero no encontrado");
        }
    }

    /**
     * Obtiene todas las ruletas.
     * 
     * @return la lista de ruletas
     */
    @GetMapping
    public ResponseEntity<List<Ruleta>> getAllRuletas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletas();
        return new ResponseEntity<>(ruletas, HttpStatus.OK);
    }

    /**
     * Obtiene todas las ruletas abiertas.
     * 
     * @return la lista de ruletas abiertas
     */
    @GetMapping("/abiertas")
    public ResponseEntity<List<Ruleta>> getAllRuletasAbiertas() {
        List<Ruleta> ruletas = ruletaService.getAllRuletasAbiertas(true);
        return new ResponseEntity<>(ruletas, HttpStatus.OK);
    }

    /**
     * Obtiene una ruleta por su ID.
     * 
     * @param id el ID de la ruleta a buscar
     * @return la ruleta encontrada, o un mensaje de error si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRuletaById(@PathVariable String id) {
        Optional<Ruleta> ruletaOpt = ruletaService.findById(id);
        if (ruletaOpt.isPresent()) {
            return new ResponseEntity<>(ruletaOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ruleta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva ruleta.
     * 
     * @return la ruleta creada, o un mensaje de error si no se puede crear
     */
    @PostMapping
    public ResponseEntity<?> crearRuleta() {
        if (ruletaService.getAllRuletasAbiertas(true).size() < 5) {
            return new ResponseEntity<>(ruletaService.crearRuleta(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Ya existen demasiadas ruletas abiertas", HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    /**
     * Realiza una apuesta en una ruleta.
     * 
     * @param token   el token de autenticación
     * @param id      el ID de la ruleta en la que se realiza la apuesta
     * @param apuesta la apuesta a realizar
     * @return la ruleta actualizada, o un mensaje de error si no se puede apostar
     */
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

    /**
     * Cierra una ruleta.
     * 
     * @param id el ID de la ruleta a cerrar
     * @return la ruleta cerrada, o un mensaje de error si no se puede cerrar
     */
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

    /**
     * Gira una ruleta.
     * 
     * @param id el ID de la ruleta a girar
     * @return la ruleta girada, o un mensaje de error si no se puede girar
     */
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
