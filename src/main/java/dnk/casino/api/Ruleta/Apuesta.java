package dnk.casino.api.Ruleta;

public class Apuesta {
    private Tipo tipo;
    private int n1;
    private int n2;
    private int n3;
    private int n4;
    private int cantidad;
    private boolean impar;
    private Color color;
    private Otros otros;

    public Apuesta(int n1, int cantidad) {
        this.tipo = Tipo.PLENO;
        this.n1 = n1;
        this.cantidad = cantidad;
    }

    public Apuesta(int n1, int n2, int cantidad) {
        this.tipo = Tipo.CABALLO;
        this.n1 = n1;
        this.n2 = n2;
        this.cantidad = cantidad;
    }

    public Apuesta(int n1, int n2, int n3, int cantidad) {
        this.tipo = Tipo.TRANSVERSAL;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.cantidad = cantidad;
    }

    public Apuesta(int n1, int n2, int n3, int n4, int cantidad) {
        this.tipo = Tipo.CUADRO;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n4 = n4;
        this.cantidad = cantidad;
    }

    public Apuesta(boolean impar, int cantidad) {
        this.tipo = Tipo.PAR;
        this.impar = impar;
        this.cantidad = cantidad;
    }

    public Apuesta(Color color, int cantidad) {
        this.tipo = Tipo.COLOR;
        this.color = color;
        this.cantidad = cantidad;
    }

    public Apuesta(Otros otros, int cantidad) {
        this.tipo = Tipo.OTROS;
        this.otros = otros;
        this.cantidad = cantidad;
    }

    public enum Tipo {
        PLENO, CABALLO, TRANSVERSAL, CUADRO, COLOR, PAR, OTROS;
    }

    public enum Color {
        ROJO, NEGRO, VERDE;
    }

    public enum Otros {
        ALTO, BAJO, DOCENA1, DOCENA2, DOCENA3, DOBLEDOCENA1, DOBLEDOCENA2, COLUMNA1, COLUMNA2, COLUMNA3, DOBLECOLUMNA1,
        DOBLECOLUMNA2, SEISENA1, SEISENA2, SEISENA3, SEISENA4, SEISENA5, SEISENA6;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }

    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }

    public int getN3() {
        return n3;
    }

    public void setN3(int n3) {
        this.n3 = n3;
    }

    public int getN4() {
        return n4;
    }

    public void setN4(int n4) {
        this.n4 = n4;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isImpar() {
        return impar;
    }

    public void setImpar(boolean impar) {
        this.impar = impar;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Otros getOtros() {
        return otros;
    }

    public void setOtros(Otros otros) {
        this.otros = otros;
    }
}
