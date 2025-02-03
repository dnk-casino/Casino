package dnk.casino.api.Ruleta;

import java.util.ArrayList;
import java.util.List;

public class Apostador {
    private String id;
    private String nombre;
    private List<Apuesta> apuestas;

    // Constructor
    public Apostador(String id, String nombre, List<Apuesta> apuestas) {
        this.id = id;
        this.nombre = nombre;
        this.apuestas = apuestas;
    }

    public Apostador(String id, String nombre) {
        this(id, nombre, new ArrayList<>());
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
