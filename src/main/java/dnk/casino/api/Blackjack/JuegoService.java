package dnk.casino.api.Blackjack;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de juegos de Blackjack.
 * 
 * @author Danikileitor
 */
@Service
public class JuegoService {

    /**
     * Repositorio de juegos de Blackjack.
     */
    @Autowired
    private JuegoRepository juegoRepository;

    /**
     * Busca un juego por su ID.
     * 
     * @param id el ID del juego a buscar
     * @return el juego encontrado, o un Optional vacío si no se encuentra
     */
    public Optional<Juego> findById(String id) {
        return juegoRepository.findById(id);
    }

    /**
     * Busca el último juego activo de un jugador.
     * 
     * @param idJugador el ID del jugador
     * @return el juego encontrado, o un Optional vacío si no se encuentra
     */
    public Optional<Juego> findLastActiveJuegoByJugador(String idJugador) {
        return juegoRepository.findLastActiveJuegoByJugador(idJugador);
    }

    /**
     * Crea un nuevo juego de Blackjack para un jugador.
     * 
     * @param idJugador el ID del jugador
     * @param apuesta   la apuesta del juego
     * @return el juego creado, o null si no se puede crear
     */
    public Juego crearJuego(String idJugador, int apuesta) {
        Optional<Juego> juegoOpt = juegoRepository.findLastActiveJuegoByJugador(idJugador);
        if (juegoOpt.isPresent()) {
            if (!juegoOpt.get().isActivo()) {
                return iniciarJuego(idJugador, apuesta);
            } else {
                return null;
            }
        } else {
            return iniciarJuego(idJugador, apuesta);
        }
    }

    /**
     * Inicia un nuevo juego de Blackjack para un jugador.
     * 
     * @param idJugador el ID del jugador
     * @param apuesta   la apuesta del juego
     * @return el juego iniciado
     */
    public Juego iniciarJuego(String idJugador, int apuesta) {
        Juego juego = new Juego(idJugador, apuesta);
        juego.iniciarJuego();
        return juegoRepository.save(juego);
    }

    /**
     * Pide una carta para un juego de Blackjack.
     * 
     * @param id el ID del juego
     * @return el juego actualizado
     */
    public Juego pedirCarta(String id) {
        Juego juego = findById(id).orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        juego.pedirCarta();
        return juegoRepository.save(juego);
    }

    /**
     * Planta el jugador en un juego de Blackjack.
     * 
     * @param id el ID del juego
     * @return un arreglo que contiene el juego actualizado y el resultado del juego
     */
    public Object[] plantarse(String id) {
        Object[] result = new Object[2];
        result[0] = findById(id).orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        result[1] = ((Juego) result[0]).plantarse();
        juegoRepository.save((Juego) result[0]);
        return result;
    }
}
