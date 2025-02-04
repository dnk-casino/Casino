package dnk.casino.api.Blackjack;

import org.springframework.stereotype.Component;

/**
 * Representa la inteligencia artificial (IA) del juego de Blackjack.
 * 
 * @author Danikileitor
 */
@Component
public class IA {
    /**
     * Mano de la IA.
     */
    private Mano mano;

    /**
     * Constructor que inicializa la IA con una mano vacía.
     */
    public IA() {
        this.mano = new Mano();
    }

    /**
     * Agrega una carta a la mano de la IA.
     * 
     * @param carta la carta a agregar
     */
    public void agregarCarta(Carta carta) {
        mano.agregarCarta(carta);
    }

    /**
     * Obtiene el valor total de la mano de la IA.
     * 
     * @return el valor total de la mano de la IA
     */
    public int getValorTotal() {
        return mano.getValorTotal();
    }

    /**
     * Determina si la IA debe pedir otra carta.
     * 
     * @return true si la IA debe pedir otra carta, false en caso contrario
     */
    public boolean debePedirCarta() {
        // Algoritmo de juego para la IA
        if (getValorTotal() < 17) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtiene la mano de la IA.
     * 
     * @return la mano de la IA
     */
    public Mano getMano() {
        return mano;
    }

    /**
     * Establece la mano de la IA.
     * 
     * @param mano la mano de la IA
     */
    public void setMano(Mano mano) {
        this.mano = mano;
    }
}
