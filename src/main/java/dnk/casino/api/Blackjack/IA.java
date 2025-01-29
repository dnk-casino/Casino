package dnk.casino.api.Blackjack;

import org.springframework.stereotype.Component;

@Component
public class IA {
    private Mano mano;

    public IA() {
        this.mano = new Mano();
    }

    public void agregarCarta(Carta carta) {
        mano.agregarCarta(carta);
    }

    public int getValorTotal() {
        return mano.getValorTotal();
    }

    public boolean debePedirCarta() {
        // Algoritmo de juego para la IA
        if (getValorTotal() < 17) {
            return true;
        } else {
            return false;
        }
    }

    public Mano getMano() {
        return mano;
    }

    public void setMano(Mano mano) {
        this.mano = mano;
    }
}