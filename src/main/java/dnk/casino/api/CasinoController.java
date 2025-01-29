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

@Controller
public class CasinoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/restablecer-contrasena")
    public String restablecerContrasena(Model model, @RequestParam(required = false) String token) {
        model.addAttribute("token", token);
        return "restablecer-contrasena";
    }

    @PostMapping(value = "/coins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping("/coins")
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

    public static class CoinUpdateRequest {
        @JsonProperty("delta")
        private int delta;

        public int getDelta() {
            return delta;
        }

        public void setDelta(int delta) {
            this.delta = delta;
        }
    }
}
