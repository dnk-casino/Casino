package dnk.casino.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;

import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

/**
 * Controlador de la aplicación del casino.
 * 
 * @author Danikileitor
 */
@Controller
public class CasinoController {

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Página de inicio de la aplicación.
     * 
     * @return la página de inicio
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Página de administración de la aplicación.
     * 
     * @return la página de administración
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    /**
     * Página de la tienda de la aplicación.
     * 
     * @return la página de la tienda
     */
    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    /**
     * Página de restablecimiento de contraseña de la aplicación.
     * 
     * @param model modelo de la página
     * @param token token de restablecimiento de contraseña
     * @return la página de restablecimiento de contraseña
     */
    @GetMapping("/restablecer-contrasena")
    public String restablecerContrasena(Model model, @RequestParam(required = false) String token) {
        model.addAttribute("token", token);
        return "restablecer-contrasena";
    }

    /**
     * Obtiene la cantidad de monedas de un usuario.
     * 
     * @param token token de autenticación
     * @return la cantidad de monedas del usuario
     */
    @PostMapping(value = "/api/coins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Integer> getCoins(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                return ResponseEntity.ok(usuarioOpt.get().getCoins());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Actualiza la cantidad de monedas de un usuario.
     * 
     * @param token   token de autenticación
     * @param request solicitud de actualización de monedas
     * @return la cantidad de monedas actualizada del usuario
     */
    @PutMapping("/api/coins")
    @ResponseBody
    public ResponseEntity<Integer> updateCoins(@RequestHeader("Authorization") String token,
            @RequestBody CoinUpdateRequest request) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                int newCoins = usuarioOpt.get().getCoins() + request.getDelta();
                if (newCoins >= 0) {
                    usuarioOpt.get().setCoins(newCoins);
                    usuarioService.updateUser(usuarioOpt.get().getId(), usuarioOpt.get());
                    return ResponseEntity.ok(newCoins);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Solicitud de actualización de monedas.
     */
    public static class CoinUpdateRequest {
        /**
         * Cantidad de monedas a agregar o restar.
         */
        @JsonProperty("delta")
        private int delta;

        /**
         * Obtiene la cantidad de monedas a agregar o restar.
         * 
         * @return la cantidad de monedas
         */
        public int getDelta() {
            return delta;
        }

        /**
         * Establece la cantidad de monedas a agregar o restar.
         * 
         * @param delta la cantidad de monedas
         */
        public void setDelta(int delta) {
            this.delta = delta;
        }
    }
}
