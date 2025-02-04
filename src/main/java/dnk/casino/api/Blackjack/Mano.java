package dnk.casino.api.Blackjack;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dnk.casino.api.Blackjack.Carta.Valor;

/**
 * Representa una mano de cartas en el juego de Blackjack.
 * 
 * @author Danikileitor
 */
@Component
public class Mano {
    /**
     * Lista de cartas en la mano.
     */
    private List<Carta> cartas;

    /**
     * Constructor que inicializa la mano vacía.
     */
    public Mano() {
        this.cartas = new ArrayList<>();
    }

    /**
     * Agrega una carta a la mano.
     * 
     * @param carta la carta a agregar
     */
    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    /**
     * Obtiene el valor total de la mano.
     * 
     * @return el valor total de la mano
     */
    public int getValorTotal() {
        int valorTotal = 0;
        int numAses = 0;
        for (Carta carta : cartas) {
            if (carta.getValor().equals(Valor.AS)) {
                numAses++;
                valorTotal += 11;
            } else {
                valorTotal += carta.getValorNumerico();
            }
        }
        while (valorTotal > 21 && numAses > 0) {
            valorTotal -= 10;
            numAses--;
        }
        return valorTotal;
    }

    /**
     * Obtiene la lista de cartas en la mano.
     * 
     * @return la lista de cartas
     */
    public List<Carta> getCartas() {
        return cartas;
    }

    /**
     * Establece la lista de cartas en la mano.
     * 
     * @param cartas la lista de cartas
     */
    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}
