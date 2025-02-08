package dnk.casino.api.Ruleta;

/**
 * Representa una apuesta en la ruleta.
 * 
 * @author Danikileitor
 */
public class Apuesta {
    /**
     * Tipo de apuesta.
     */
    private Tipo tipo;
    /**
     * Número 1 de la apuesta.
     */
    private int n1;
    /**
     * Número 2 de la apuesta.
     */
    private int n2;
    /**
     * Número 3 de la apuesta.
     */
    private int n3;
    /**
     * Número 4 de la apuesta.
     */
    private int n4;
    /**
     * Cantidad apostada.
     */
    private int cantidad;
    /**
     * Indica si la apuesta es impar.
     */
    private boolean impar;
    /**
     * Color de la apuesta.
     */
    private Color color;
    /**
     * Otras opciones de apuesta.
     */
    private Otros otros;

    public Apuesta() {
    }

    /**
     * Constructor que inicializa la apuesta PLENO con un número y una cantidad.
     * 
     * @param n1       el número de la apuesta
     * @param cantidad la cantidad apostada
     */
    public Apuesta(int n1, int cantidad) {
        this.tipo = Tipo.PLENO;
        this.n1 = n1;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta CABALLO con dos números y una cantidad.
     * 
     * @param n1       el primer número de la apuesta
     * @param n2       el segundo número de la apuesta
     * @param cantidad la cantidad apostada
     */
    public Apuesta(int n1, int n2, int cantidad) {
        this.tipo = Tipo.CABALLO;
        this.n1 = n1;
        this.n2 = n2;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta TRANSVERSAL con tres números y una
     * cantidad.
     * 
     * @param n1       el primer número de la apuesta
     * @param n2       el segundo número de la apuesta
     * @param n3       el tercer número de la apuesta
     * @param cantidad la cantidad apostada
     */
    public Apuesta(int n1, int n2, int n3, int cantidad) {
        this.tipo = Tipo.TRANSVERSAL;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta CUADRO con cuatro números y una
     * cantidad.
     * 
     * @param n1       el primer número de la apuesta
     * @param n2       el segundo número de la apuesta
     * @param n3       el tercer número de la apuesta
     * @param n4       el cuarto número de la apuesta
     * @param cantidad la cantidad apostada
     */
    public Apuesta(int n1, int n2, int n3, int n4, int cantidad) {
        this.tipo = Tipo.CUADRO;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n4 = n4;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta PAR con una opción de par o impar y una
     * cantidad.
     * 
     * @param impar    la opción de par (false) o impar (true)
     * @param cantidad la cantidad apostada
     */
    public Apuesta(boolean impar, int cantidad) {
        this.tipo = Tipo.PAR;
        this.impar = impar;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta COLOR con un color y una cantidad.
     * 
     * @param color    el color de la apuesta
     * @param cantidad la cantidad apostada
     */
    public Apuesta(Color color, int cantidad) {
        this.tipo = Tipo.COLOR;
        this.color = color;
        this.cantidad = cantidad;
    }

    /**
     * Constructor que inicializa la apuesta con una opción de otros y una cantidad.
     * 
     * @param otros    la opción de otros
     * @param cantidad la cantidad apostada
     */
    public Apuesta(Otros otros, int cantidad) {
        this.tipo = Tipo.OTROS;
        this.otros = otros;
        this.cantidad = cantidad;
    }

    /**
     * Enumeración de tipos de apuestas.
     */
    public enum Tipo {
        /**
         * Apuesta a un número específico.
         */
        PLENO,
        /**
         * Apuesta a dos números.
         */
        CABALLO,
        /**
         * Apuesta a tres números.
         */
        TRANSVERSAL,
        /**
         * Apuesta a cuatro números.
         */
        CUADRO,
        /**
         * Apuesta a un color.
         */
        COLOR,
        /**
         * Apuesta a par o impar.
         */
        PAR,
        /**
         * Apuesta a otras opciones.
         */
        OTROS;
    }

    /**
     * Enumeración de colores.
     */
    public enum Color {
        /**
         * Rojo.
         */
        ROJO,
        /**
         * Negro.
         */
        NEGRO,
        /**
         * Verde.
         */
        VERDE;
    }

    /**
     * Enumeración de opciones de otros.
     */
    public enum Otros {
        /**
         * Alto.
         */
        ALTO,
        /**
         * Bajo.
         */
        BAJO,
        /**
         * Docena 1.
         */
        DOCENA1,
        /**
         * Docena 2.
         */
        DOCENA2,
        /**
         * Docena 3.
         */
        DOCENA3,
        /**
         * Doble docena 1.
         */
        DOBLEDOCENA1,
        /**
         * Doble docena 2.
         */
        DOBLEDOCENA2,
        /**
         * Columna 1.
         */
        COLUMNA1,
        /**
         * Columna 2.
         */
        COLUMNA2,
        /**
         * Columna 3.
         */
        COLUMNA3,
        /**
         * Doble columna 1.
         */
        DOBLECOLUMNA1,
        /**
         * Doble columna 2.
         */
        DOBLECOLUMNA2,
        /**
         * Seisena 1.
         */
        SEISENA1,
        /**
         * Seisena 2.
         */
        SEISENA2,
        /**
         * Seisena 3.
         */
        SEISENA3,
        /**
         * Seisena 4.
         */
        SEISENA4,
        /**
         * Seisena 5.
         */
        SEISENA5,
        /**
         * Seisena 6.
         */
        SEISENA6;
    }

    /**
     * Obtiene el tipo de apuesta.
     * 
     * @return el tipo de apuesta
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de apuesta.
     * 
     * @param tipo el tipo de apuesta
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el número 1 de la apuesta.
     * 
     * @return el número 1 de la apuesta
     */
    public int getN1() {
        return n1;
    }

    /**
     * Establece el número 1 de la apuesta.
     * 
     * @param n1 el número 1 de la apuesta
     */
    public void setN1(int n1) {
        this.n1 = n1;
    }

    /**
     * Obtiene el número 2 de la apuesta.
     * 
     * @return el número 2 de la apuesta
     */
    public int getN2() {
        return n2;
    }

    /**
     * Establece el número 2 de la apuesta.
     * 
     * @param n2 el número 2 de la apuesta
     */
    public void setN2(int n2) {
        this.n2 = n2;
    }

    /**
     * Obtiene el número 3 de la apuesta.
     * 
     * @return el número 3 de la apuesta
     */
    public int getN3() {
        return n3;
    }

    /**
     * Establece el número 3 de la apuesta.
     * 
     * @param n3 el número 3 de la apuesta
     */
    public void setN3(int n3) {
        this.n3 = n3;
    }

    /**
     * Obtiene el número 4 de la apuesta.
     * 
     * @return el número 4 de la apuesta
     */
    public int getN4() {
        return n4;
    }

    /**
     * Establece el número 4 de la apuesta.
     * 
     * @param n4 el número 4 de la apuesta
     */
    public void setN4(int n4) {
        this.n4 = n4;
    }

    /**
     * Obtiene la cantidad apostada.
     * 
     * @return la cantidad apostada
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad apostada.
     * 
     * @param cantidad la cantidad apostada
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Indica si la apuesta es impar.
     * 
     * @return true si la apuesta es impar, false en caso contrario
     */
    public boolean isImpar() {
        return impar;
    }

    /**
     * Establece si la apuesta es impar.
     * 
     * @param impar true si la apuesta es impar, false en caso contrario
     */
    public void setImpar(boolean impar) {
        this.impar = impar;
    }

    /**
     * Obtiene el color de la apuesta.
     * 
     * @return el color de la apuesta
     */
    public Color getColor() {
        return color;
    }

    /**
     * Establece el color de la apuesta.
     * 
     * @param color el color de la apuesta
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Obtiene la opción de otros de la apuesta.
     * 
     * @return la opción de otros de la apuesta
     */
    public Otros getOtros() {
        return otros;
    }

    /**
     * Establece la opción de otros de la apuesta.
     * 
     * @param otros la opción de otros de la apuesta
     */
    public void setOtros(Otros otros) {
        this.otros = otros;
    }
}
