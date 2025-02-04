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

/**
 * Controlador de la API de la tragaperras.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/api/tragaperras")
public class TragaperrasController {

    /**
     * Obtiene un reel aleatorio para una skin.
     * 
     * @param skin la skin
     * @return el reel aleatorio
     */
    private String getRandomReel(Skin skin) {
        Optional<Skin> skinOpt = skinRepository.findById(skin.getId());
        if (skinOpt.isPresent()) {
            String[] reels = skinOpt.get().getReels();
            return reels[(int) (Math.random() * reels.length)];
        } else {
            throw new IllegalArgumentException("La skin no existe");
        }
    }

    /**
     * Servicio de la tragaperras.
     */
    @Autowired
    private TragaperrasService tragaperrasService;

    /**
     * Repositorio de skins.
     */
    @Autowired
    private SkinRepository skinRepository;

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todas las skins disponibles.
     * 
     * @return las skins disponibles
     */
    @PostMapping(value = "/skins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSkins() {
        return ResponseEntity.ok(skinRepository.findAll());
    }

    /**
     * Obtiene una skin por su ID.
     * 
     * @param id el ID de la skin
     * @return la skin
     */
    @PostMapping(value = "/skins/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSkin(@PathVariable String id) {
        return ResponseEntity.ok(skinRepository.findById(id));
    }

    /**
     * Obtiene las skins vendibles o no vendibles según el parámetro.
     * 
     * @param vendible true para obtener skins vendibles, false para obtener skins
     *                 no
     *                 vendibles
     * @return las skins
     */
    @PostMapping(value = "/skins/vendibles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtenerSkinsVendibles(@RequestBody boolean vendible) {
        return ResponseEntity.ok(skinRepository.findAllByVendible(vendible));
    }

    /**
     * Obtiene las skins desbloqueadas por un usuario.
     * 
     * @param token el token de autenticación
     * @return las skins desbloqueadas
     */
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

    /**
     * Obtiene los IDs de las skins desbloqueadas por un usuario.
     * 
     * @param token el token de autenticación
     * @return los IDs de las skins desbloqueadas
     */
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

    /**
     * Juega a la tragaperras.
     * 
     * @param token   el token de autenticación
     * @param request la solicitud de juego
     * @return el resultado del juego
     */
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
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tienes suficientes monedas");
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

    /**
     * Solicitud de juego.
     */
    public static class PlayRequest {
        /**
         * Nombre de la skin a jugar.
         */
        @JsonProperty("skin")
        private String skin;
        /**
         * Costo de la jugada.
         */
        @JsonProperty("cost")
        private int cost;

        /**
         * Obtiene el costo de la jugada.
         * 
         * @return el costo de la jugada
         */
        public int getCost() {
            return cost;
        }

        /**
         * Establece el costo de la jugada.
         * 
         * @param cost el costo de la jugada
         */
        public void setCost(int cost) {
            this.cost = cost;
        }

        /**
         * Obtiene el nombre de la skin a jugar.
         * 
         * @return el nombre de la skin
         */
        public String getSkin() {
            return skin;
        }

        /**
         * Establece el nombre de la skin a jugar.
         * 
         * @param skin el nombre de la skin
         */
        public void setSkin(String skin) {
            this.skin = skin;
        }
    }

    /**
     * Obtiene el número de victorias de un usuario.
     * 
     * @param token el token de autenticación
     * @return el número de victorias
     */
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

    /**
     * Obtiene el ranking de usuarios.
     * 
     * @return el ranking de usuarios
     */
    @PostMapping(value = "/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getRanking() {
        // Obtén los 5 jugadores con más victorias
        List<Usuario> usuarios = usuarioService.getTop5Winners();
        // Crea un objeto que contenga el ranking
        return ResponseEntity.ok(new Ranking(usuarios, usuarioService));
    }

    /**
     * Resultado de la tragaperras.
     */
    @JsonAutoDetect
    public static class SlotMachineResult {
        /**
         * ID del resultado.
         */
        @Id
        private String id;
        /**
         * Nombre del usuario.
         */
        private String username;
        /**
         * Número de intento.
         */
        private int attemptNumber;
        /**
         * Costo de la jugada.
         */
        private int cost;
        /**
         * Reel 1.
         */
        private String reel1;
        /**
         * Reel 2.
         */
        private String reel2;
        /**
         * Reel 3.
         */
        private String reel3;
        /**
         * Mensaje del resultado.
         */
        private String message;
        /**
         * Indica si el resultado es una victoria.
         */
        private boolean win;
        /**
         * Fecha de ejecución.
         */
        private Date executionDate;

        /**
         * Constructor vacío.
         */
        public SlotMachineResult() {
        }

        /**
         * Constructor que inicializa el resultado con los parámetros.
         * 
         * @param username      nombre del usuario
         * @param attemptNumber número de intento
         * @param cost          costo de la jugada
         * @param reel1         reel 1
         * @param reel2         reel 2
         * @param reel3         reel 3
         * @param message       mensaje del resultado
         * @param win           indica si el resultado es una victoria
         * @param executionDate fecha de ejecución
         */
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

        /**
         * Obtiene el nombre del usuario.
         * 
         * @return el nombre del usuario
         */
        public String getUsername() {
            return username;
        }

        /**
         * Establece el nombre del usuario.
         * 
         * @param username el nombre del usuario
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Obtiene el costo de la jugada.
         * 
         * @return el costo de la jugada
         */
        public int getCost() {
            return cost;
        }

        /**
         * Establece el costo de la jugada.
         * 
         * @param cost el costo de la jugada
         */
        public void setCost(int cost) {
            this.cost = cost;
        }

        /**
         * Obtiene el reel 1.
         * 
         * @return el reel 1
         */
        public String getReel1() {
            return reel1;
        }

        /**
         * Establece el reel 1.
         * 
         * @param reel1 el reel 1
         */
        public void setReel1(String reel1) {
            this.reel1 = reel1;
        }

        /**
         * Obtiene el reel 2.
         * 
         * @return el reel 2
         */
        public String getReel2() {
            return reel2;
        }

        /**
         * Establece el reel 2.
         * 
         * @param reel2 el reel 2
         */
        public void setReel2(String reel2) {
            this.reel2 = reel2;
        }

        /**
         * Obtiene el reel 3.
         * 
         * @return el reel 3
         */
        public String getReel3() {
            return reel3;
        }

        /**
         * Establece el reel 3.
         * 
         * @param reel3 el reel 3
         */
        public void setReel3(String reel3) {
            this.reel3 = reel3;
        }

        /**
         * Obtiene el mensaje del resultado.
         * 
         * @return el mensaje del resultado
         */
        public String getMessage() {
            return message;
        }

        /**
         * Establece el mensaje del resultado.
         * 
         * @param message el mensaje del resultado
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * Indica si el resultado es una victoria.
         * 
         * @return true si el resultado es una victoria, false en caso contrario
         */
        public boolean isWin() {
            return win;
        }

        /**
         * Establece si el resultado es una victoria.
         * 
         * @param win true si el resultado es una victoria, false en caso contrario
         */
        public void setWin(boolean win) {
            this.win = win;
        }

        /**
         * Obtiene el ID del resultado.
         * 
         * @return el ID del resultado
         */
        public String getId() {
            return id;
        }

        /**
         * Establece el ID del resultado.
         * 
         * @param id el ID del resultado
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Obtiene el número de intento.
         * 
         * @return el número de intento
         */
        public int getAttemptNumber() {
            return attemptNumber;
        }

        /**
         * Establece el número de intento.
         * 
         * @param attemptNumber el número de intento
         */
        public void setAttemptNumber(int attemptNumber) {
            this.attemptNumber = attemptNumber;
        }

        /**
         * Obtiene la fecha de ejecución.
         * 
         * @return la fecha de ejecución
         */
        public Date getExecutionDate() {
            return executionDate;
        }

        /**
         * Establece la fecha de ejecución.
         * 
         * @param executionDate la fecha de ejecución
         */
        public void setExecutionDate(Date executionDate) {
            this.executionDate = executionDate;
        }
    }
}
