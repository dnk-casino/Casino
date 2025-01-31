package dnk.casino.api.Ruleta;

import java.util.ArrayList;
import java.util.List;

public class Apostador {
    private String id;
    private String nombre;
    private String ruleta;
    private List<Apuesta> apuestas;

    // Constructor
    public Apostador(String id, String nombre, String ruleta, List<Apuesta> apuestas) {
        this.id = id;
        this.nombre = nombre;
        this.ruleta = ruleta;
        this.apuestas = apuestas;
    }

    public Apostador(String id, String nombre, String ruleta) {
        this(id, nombre, ruleta, new ArrayList<>());
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuleta() {
        return ruleta;
    }

    public void setRuleta(String ruleta) {
        this.ruleta = ruleta;
    }

    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    public void setApuestas(List<Apuesta> apuestas) {
        this.apuestas = apuestas;
    }

    public void apostar(Apuesta apuesta) {
        this.apuestas.add(apuesta);
    }
}
