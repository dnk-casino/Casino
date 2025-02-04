package dnk.casino.api.Blackjack;

/**
 * Representa una carta en el juego de Blackjack.
 * 
 * @author Danikileitor
 */
public class Carta {
    /**
     * Palo de la carta.
     */
    private final Palo palo;
    /**
     * Valor de la carta.
     */
    private final Valor valor;

    /**
     * Constructor que inicializa la carta con un palo y un valor.
     * 
     * @param palo  el palo de la carta
     * @param valor el valor de la carta
     */
    public Carta(Palo palo, Valor valor) {
        this.palo = palo;
        this.valor = valor;
    }

    /**
     * Obtiene el palo de la carta.
     * 
     * @return el palo de la carta
     */
    public Palo getPalo() {
        return palo;
    }

    /**
     * Obtiene el valor de la carta.
     * 
     * @return el valor de la carta
     */
    public Valor getValor() {
        return valor;
    }

    /**
     * Obtiene el icono del palo de la carta.
     * 
     * @return el icono del palo de la carta
     */
    public String getPaloIcono() {
        return palo.getIcono();
    }

    /**
     * Obtiene el valor numérico de la carta.
     * 
     * @return el valor numérico de la carta
     */
    public int getValorNumerico() {
        return valor.getValor();
    }

    /**
     * Enumeración de palos de cartas.
     */
    public enum Palo {
        /**
         * Corazones.
         */
        CORAZONES("Corazones", "♥"),
        /**
         * Diamantes.
         */
        DIAMANTES("Diamantes", "♦"),
        /**
         * Picas.
         */
        PICAS("Picas", "♠"),
        /**
         * Tréboles.
         */
        TREBOLES("Tréboles", "♣");

        /**
         * Nombre del palo.
         */
        private final String nombre;
        /**
         * Icono del palo.
         */
        private final String icono;

        /**
         * Constructor que inicializa el palo con un nombre y un icono.
         * 
         * @param nombre el nombre del palo
         * @param icono  el icono del palo
         */
        Palo(String nombre, String icono) {
            this.nombre = nombre;
            this.icono = icono;
        }

        /**
         * Obtiene el nombre del palo.
         * 
         * @return el nombre del palo
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Obtiene el icono del palo.
         * 
         * @return el icono del palo
         */
        public String getIcono() {
            return icono;
        }

        @Override
        public String toString() {
            return icono;
        }
    }

    /**
     * Enumeración de valores de cartas.
     */
    public enum Valor {
        /**
         * As.
         */
        AS("As", 11),
        /**
         * Dos.
         */
        DOS("2", 2),
        /**
         * Tres.
         */
        TRES("3", 3),
        /**
         * Cuatro.
         */
        CUATRO("4", 4),
        /**
         * Cinco.
         */
        CINCO("5", 5),
        /**
         * Seis.
         */
        SEIS("6", 6),
        /**
         * Siete.
         */
        SIETE("7", 7),
        /**
         * Ocho.
         */
        OCHO("8", 8),
        /**
         * Nueve.
         */
        NUEVE("9", 9),
        /**
         * Diez.
         */
        DIEZ("10", 10),
        /**
         * Jota.
         */
        JOTA("J", 10),
        /**
         * Reina.
         */
        REINA("Q", 10),
        /**
         * Rey.
         */
        REY("K", 10);

        /**
         * Nombre del valor.
         */
        private final String nombre;
        /**
         * Valor numérico.
         */
        private final int valor;

        /**
         * Constructor que inicializa el valor con un nombre y un valor numérico.
         * 
         * @param nombre el nombre del valor
         * @param valor  el valor numérico
         */
        Valor(String nombre, int valor) {
            this.nombre = nombre;
            this.valor = valor;
        }

        /**
         * Obtiene el nombre del valor.
         * 
         * @return el nombre del valor
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Obtiene el valor numérico.
         * 
         * @return el valor numérico
         */
        public int getValor() {
            return valor;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
