package dnk.casino.api.Tragaperras;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import dnk.casino.Skins.Skin;
import dnk.casino.Skins.SkinRepository;
import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

@RestController
@RequestMapping("/api/tragaperras")
public class TragaperrasController {

    private String getRandomReel(Skin skin) {
        Optional<Skin> skinOpt = skinRepository.findById(skin.getId());
        if (skinOpt.isPresent()) {
            String[] reels = skinOpt.get().getReels();
            return reels[(int) (Math.random() * reels.length)];
        } else {
            throw new IllegalArgumentException("La skin no existe");
        }
    }

    @Autowired
    private TragaperrasService tragaperrasService;

    @Autowired
    private SkinRepository skinRepository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/skins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSkins() {
        return ResponseEntity.ok(skinRepository.findAll());
    }

    @PostMapping(value = "/skins/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSkin(@PathVariable String id) {
        return ResponseEntity.ok(skinRepository.findById(id));
    }

    @PostMapping(value = "/skins/vendibles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtenerSkinsVendibles(@RequestBody boolean vendible) {
        return ResponseEntity.ok(skinRepository.findAllByVendible(vendible));
    }

    @PostMapping(value = "/skins/desbloqueadas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> obtenerSkinsDesbloqueadas(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                Set<Skin> skins = usuarioOpt.get().getSkins().stream()
                        .map(skinId -> {
                            return skinRepository.findById(skinId).get();
                        })
                        .collect(Collectors.toSet());
                return ResponseEntity.ok(skins);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PostMapping(value = "/skins/desbloqueadas/id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> obtenerSkinsDesbloqueadasId(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                return ResponseEntity.ok(usuarioOpt.get().getSkins());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    @PostMapping(value = "/play", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> play(@RequestHeader("Authorization") String token, @RequestBody PlayRequest request) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            String username = usernameOpt.get();
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(username);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                int cost = request.getCost();
                if (usuario.getCoins() >= cost) {
                    Optional<Skin> skinOpt = skinRepository.findByName(request.getSkin());
                    if (skinOpt.isPresent()) {
                        Skin skin = skinOpt.get();
                        String reel1 = getRandomReel(skin);
                        String reel2 = getRandomReel(skin);
                        String reel3 = getRandomReel(skin);
                        boolean win = reel1.equals(reel2) && reel2.equals(reel3);
                        String message = win ? "¡Ganaste!" : "¡Sigue intentando!";
                        int attemptNumber = tragaperrasService.getNextAttemptNumber(username);

                        int newCoins = usuario.getCoins() - cost;
                        if (newCoins < 0) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tienes sufiences monedas");
                        }
                        usuario.setCoins(newCoins);
                        usuarioService.updateUser(usuario.getId(), usuario);

                        SlotMachineResult result = new SlotMachineResult(username, attemptNumber, cost, reel1, reel2,
                                reel3, message, win, new Date());
                        tragaperrasService.saveResult(username, result);

                        if (result.isWin()) {
                            switch (result.getReel1()) {
                                case String o when o.equals(skin.getReels()[0]) -> {
                                    newCoins += (cost * 50);
                                }

                                case String o when o.equals(skin.getReels()[1]) -> {
                                    newCoins += (cost * 30);
                                }

                                case String o when o.equals(skin.getReels()[2]) -> {
                                    newCoins += (cost * 20);
                                }

                                case String o when o.equals(skin.getReels()[3]) -> {
                                    newCoins += (cost * 10);
                                }

                                case String o when o.equals(skin.getReels()[4]) -> {
                                    newCoins += (cost * 5);
                                }

                                default -> {
                                    // En caso de crear una skin con más de 5 emojis
                                    newCoins += (cost * 3);
                                }
                            }
                            usuario.setCoins(newCoins);
                            usuarioService.victoria(usuario.getId());
                            usuarioService.updateUser(usuario.getId(), usuario);
                        }

                        List<Object> envio = new ArrayList<>();
                        envio.add(result);
                        envio.add(skin.getReels());

                        return ResponseEntity.ok(envio);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La skin no existe");
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

    public static class PlayRequest {
        @JsonProperty("skin")
        private String skin;
        @JsonProperty("cost")
        private int cost;

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public String getSkin() {
            return skin;
        }

        public void setSkin(String skin) {
            this.skin = skin;
        }
    }

    @PostMapping(value = "/wins", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getWins(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                try {
                    int wins = usuarioService.getWins(usuarioOpt.get().getId());
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

    @PostMapping(value = "/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getRanking() {
        // Obtén los 5 jugadores con más victorias
        List<Usuario> usuarios = usuarioService.getTop5Winners();
        // Crea un objeto que contenga el ranking
        return ResponseEntity.ok(new Ranking(usuarios, usuarioService));
    }

    @JsonAutoDetect
    public static class SlotMachineResult {
        @Id
        private String id;
        private String username;
        private int attemptNumber;
        private int cost;
        private String reel1;
        private String reel2;
        private String reel3;
        private String message;
        private boolean win;
        private Date executionDate;

        public SlotMachineResult() {
        }

        public SlotMachineResult(String username, int attemptNumber, int cost, String reel1, String reel2, String reel3,
                String message, boolean win, Date executionDate) {
            this.username = username;
            this.attemptNumber = attemptNumber;
            this.reel1 = reel1;
            this.reel2 = reel2;
            this.reel3 = reel3;
            this.message = message;
            this.win = win;
            this.executionDate = executionDate;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public String getReel1() {
            return reel1;
        }

        public void setReel1(String reel1) {
            this.reel1 = reel1;
        }

        public String getReel2() {
            return reel2;
        }

        public void setReel2(String reel2) {
            this.reel2 = reel2;
        }

        public String getReel3() {
            return reel3;
        }

        public void setReel3(String reel3) {
            this.reel3 = reel3;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getAttemptNumber() {
            return attemptNumber;
        }

        public void setAttemptNumber(int attemptNumber) {
            this.attemptNumber = attemptNumber;
        }

        public Date getExecutionDate() {
            return executionDate;
        }

        public void setExecutionDate(Date executionDate) {
            this.executionDate = executionDate;
        }
    }
}