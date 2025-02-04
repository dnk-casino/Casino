package dnk.casino.api.Blackjack;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

/**
 * Controlador de la API de Blackjack.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/api/blackjack")
public class BlackjackController {

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Servicio de juegos de Blackjack.
     */
    @Autowired
    private JuegoService juegoService;

    /**
     * Obtiene el número de victorias de un usuario en el juego de Blackjack.
     * 
     * @param token el token de autenticación
     * @return el número de victorias del usuario
     */
    @PostMapping(value = "/wins", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getWins(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                try {
                    int wins = usuarioService.getBjwins(usuarioOpt.get().getId());
                    return ResponseEntity.ok(wins);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al obtener las victorias: " + e.getMessage());
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    /**
     * Obtiene el ranking de usuarios en el juego de Blackjack.
     * 
     * @return el ranking de usuarios
     */
    @PostMapping(value = "/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getRanking() {
        // Obtén los 5 jugadores con más victorias
        List<Usuario> usuarios = usuarioService.getTop5BJWinners();
        // Crea un objeto que contenga el ranking
        return ResponseEntity.ok(new Ranking(usuarios, usuarioService));
    }

    /**
     * Crea un nuevo juego de Blackjack para un usuario.
     * 
     * @param apuesta la apuesta del juego
     * @param token   el token de autenticación
     * @return el juego creado
     */
    @PostMapping(value = "/crear-juego/{apuesta}")
    public ResponseEntity<?> crearJuego(@PathVariable int apuesta, @RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                if (usuario.getCoins() >= apuesta) {
                    usuarioService.pagar(usuario.getId(), apuesta);
                    Juego juego = juegoService.crearJuego(usuario.getId(), apuesta);
                    if (!juego.isActivo()) {
                        switch (juego.determinarGanador()) {
                            case 0 -> {
                                usuarioService.cobrar(usuario.getId(), juego.getApuesta());
                            }
                            case 1 -> {
                                usuarioService.cobrar(usuario.getId(), (juego.getApuesta() * 2));
                                usuarioService.bjvictoria(usuario.getId());
                            }
                            default -> {
                            }
                        }
                    }
                    return ResponseEntity.ok(juego);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tienes suficientes monedas");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tienes que iniciar sesión");
        }
    }

    /**
     * Obtiene un juego de Blackjack por su ID.
     * 
     * @param id    el ID del juego
     * @param token el token de autenticación
     * @return el juego
     */
    @PostMapping("/juego/{id}")
    public ResponseEntity<?> getJuego(@PathVariable String id, @RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Optional<Juego> juegoOpt = juegoService.findById(id);
                if (juegoOpt.isPresent()) {
                    return ResponseEntity.ok(juegoOpt.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida no encontrada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no econtrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tienes que iniciar sesión");
        }
    }

    /**
     * Obtiene el juego de Blackjack activo de un usuario.
     * 
     * @param token el token de autenticación
     * @return el juego activo
     */
    @PostMapping("/juegoActivo")
    public ResponseEntity<?> getJuegoActivo(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Optional<Juego> juegoOpt = juegoService.findLastActiveJuegoByJugador(usuarioOpt.get().getId());
                if (juegoOpt.isPresent()) {
                    return ResponseEntity.ok(juegoOpt.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida no encontrada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no econtrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tienes que iniciar sesión");
        }
    }

    /**
     * Pide una carta en un juego de Blackjack.
     * 
     * @param id    el ID del juego
     * @param token el token de autenticación
     * @return el juego actualizado
     */
    @PostMapping("/pedir-carta/{id}")
    public ResponseEntity<?> pedirCarta(@PathVariable String id, @RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                Optional<Juego> juegoOpt = juegoService.findById(id);
                if (juegoOpt.isPresent()) {
                    if (juegoOpt.get().isActivo()) {
                        Juego juego = juegoService.pedirCarta(id);
                        if (!juego.isActivo()) {
                            switch (juego.determinarGanador()) {
                                case 0 -> {
                                    usuarioService.cobrar(usuario.getId(), juego.getApuesta());
                                }
                                case 1 -> {
                                    usuarioService.cobrar(usuario.getId(), (juego.getApuesta() * 2));
                                    usuarioService.bjvictoria(usuario.getId());
                                }
                                default -> {
                                }
                            }
                        }
                        return ResponseEntity.ok(juego);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La partida ya ha terminado");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida no encontrada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no econtrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tienes que iniciar sesión");
        }
    }

    /**
     * Planta el jugador en un juego de Blackjack.
     * 
     * @param id    el ID del juego
     * @param token el token de autenticación
     * @return el resultado del juego
     */
    @PostMapping(value = "/plantarse/{id}")
    public ResponseEntity<?> plantarse(@PathVariable String id, @RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                Optional<Juego> juegoOpt = juegoService.findById(id);
                if (juegoOpt.isPresent()) {
                    if (juegoOpt.get().isActivo()) {
                        Object[] result = juegoService.plantarse(id);
                        switch ((int) result[1]) {
                            case 0 -> {
                                usuarioService.cobrar(usuario.getId(), juegoOpt.get().getApuesta());
                            }
                            case 1 -> {
                                usuarioService.cobrar(usuario.getId(), (juegoOpt.get().getApuesta() * 2));
                                usuarioService.bjvictoria(usuario.getId());
                            }
                            default -> {
                            }
                        }
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La partida ya ha terminado");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partida no encontrada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no econtrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tienes que iniciar sesión");
        }
    }
}
