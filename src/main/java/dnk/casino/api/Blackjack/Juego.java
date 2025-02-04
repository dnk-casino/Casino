package dnk.casino.api.Blackjack;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import dnk.casino.api.Blackjack.Carta.Palo;
import dnk.casino.api.Blackjack.Carta.Valor;

/**
 * Representa un juego de Blackjack.
 * 
 * @author Danikileitor
 */
@Document(collection = "blackjack")
public class Juego {
    /**
     * ID del juego.
     */
    @Id
    @JsonProperty
    private String id;
    /**
     * Apuesta del juego.
     */
    @JsonProperty
    private int apuesta;
    /**
     * IA del juego.
     */
    @JsonProperty
    private IA ia;
    /**
     * ID del jugador.
     */
    @JsonProperty
    private String idJugador;
    /**
     * Mano del jugador.
     */
    @JsonProperty
    private Mano manoJugador;
    /**
     * Indica si el juego está activo.
     */
    @JsonProperty
    private boolean activo;

    /**
     * Constructor que inicializa el juego con un ID de jugador y una apuesta.
     * 
     * @param idJugador el ID del jugador
     * @param apuesta   la apuesta del juego
     */
    public Juego(String idJugador, int apuesta) {
        this.apuesta = apuesta;
        this.ia = new IA();
        this.idJugador = idJugador;
        this.manoJugador = new Mano();
        this.activo = true;
    }

    /**
     * Obtiene la apuesta del juego.
     * 
     * @return la apuesta del juego
     */
    public int getApuesta() {
        return apuesta;
    }

    /**
     * Establece la apuesta del juego.
     * 
     * @param apuesta la apuesta del juego
     */
    public void setApuesta(int apuesta) {
        this.apuesta = apuesta;
    }

    /**
     * Obtiene la IA del juego.
     * 
     * @return la IA del juego
     */
    public IA getIa() {
        return ia;
    }

    /**
     * Establece la IA del juego.
     * 
     * @param ia la IA del juego
     */
    public void setIa(IA ia) {
        this.ia = ia;
    }

    /**
     * Obtiene el ID del jugador.
     * 
     * @return el ID del jugador
     */
    public String getIdJugador() {
        return idJugador;
    }

    /**
     * Establece el ID del jugador.
     * 
     * @param idJugador el ID del jugador
     */
    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    /**
     * Indica si el juego está activo.
     * 
     * @return true si el juego está activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece si el juego está activo.
     * 
     * @param activo true si el juego está activo, false en caso contrario
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene la mano del jugador.
     * 
     * @return la mano del jugador
     */
    public Mano getManoJugador() {
        return manoJugador;
    }

    /**
     * Establece la mano del jugador.
     * 
     * @param mano la mano del jugador
     */
    public void setManoJugador(Mano mano) {
        this.manoJugador = mano;
    }

    /**
     * Obtiene la mano de la IA.
     * 
     * @return la mano de la IA
     */
    public Mano getManoIA() {
        return ia.getMano();
    }

    /**
     * Obtiene el valor total de la mano del jugador.
     * 
     * @return el valor total de la mano del jugador
     */
    public int getValorJugador() {
        return manoJugador.getValorTotal();
    }

    /**
     * Obtiene el valor total de la mano de la IA.
     * 
     * @return el valor total de la mano de la IA
     */
    public int getValorIA() {
        return ia.getValorTotal();
    }

    /**
     * Agrega una carta a la mano del jugador.
     * 
     * @param carta la carta a agregar
     */
    public void agregarCartaJugador(Carta carta) {
        manoJugador.agregarCarta(carta);
    }

    /**
     * Inicia el juego.
     */
    public void iniciarJuego() {
        pedirCarta();
        pedirCarta();
        pedirCartaIA();
        pedirCartaIA();
        if (getValorJugador() == 21 || getValorIA() == 21) {
            determinarGanador();
        }
    }

    /**
     * Obtiene un palo aleatorio.
     * 
     * @return un palo aleatorio
     */
    public Palo randomPalo() {
        return Palo.values()[(int) (Math.random() * Palo.values().length)];
    }

    /**
     * Obtiene un valor aleatorio.
     * 
     * @return un valor aleatorio
     */
    public Valor randomValor() {
        return Valor.values()[(int) (Math.random() * Valor.values().length)];
    }

    /**
     * Pide una carta para el jugador.
     */
    public void pedirCarta() {
        Carta carta = new Carta(randomPalo(), randomValor());
        agregarCartaJugador(carta);
        if (getValorJugador() >= 21) {
            plantarse();
        }
    }

    /**
     * Pide una carta para la IA.
     */
    public void pedirCartaIA() {
        Carta carta = new Carta(randomPalo(), randomValor());
        ia.agregarCarta(carta);
    }

    /**
     * Planta el jugador.
     * 
     * @return el resultado del juego
     */
    public int plantarse() {
        if (getValorJugador() <= 21) {
            while (getValorIA() < 17) {
                pedirCartaIA();
            }
        }
        return determinarGanador();
    }

    /**
     * Determina el ganador del juego.
     * 
     * @return el resultado del juego (1 si el jugador gana, 2 si la IA gana, 0 si
     *         es un empate)
     */
    public int determinarGanador() {
        setActivo(false);
        if (getValorJugador() > 21) {
            return 2; // El jugador se pasó, gana la IA
        } else if (getValorIA() > 21) {
            return 1; // La IA se pasó, gana el jugador
        } else if (getValorIA() == 21 && getManoIA().getCartas().size() == 2) {
            return 2; // La IA tiene un blackjack, gana
        } else if (getValorJugador() == 21 && getManoJugador().getCartas().size() == 2) {
            if (getValorIA() == 21 && getManoIA().getCartas().size() == 2) {
                return 0; // Ambos tienen un blackjack, empate
            } else {
                return 1; // El jugador tiene un blackjack, gana
            }
        } else {
            if (getValorJugador() > getValorIA()) {
                return 1; // El jugador tiene un valor mayor, gana
            } else if (getValorJugador() < getValorIA()) {
                return 2; // La IA tiene un valor mayor, gana
            } else {
                return 0; // Empate
            }
        }
    }
}
