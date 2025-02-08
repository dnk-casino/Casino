package dnk.casino.api.Ruleta;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un apostador en la ruleta.
 * 
 * @author Danikileitor
 */
public class Apostador {
    /**
     * ID del apostador.
     */
    private String id;
    /**
     * Nombre del apostador.
     */
    private String nombre;
    /**
     * Lista de apuestas del apostador.
     */
    private List<Apuesta> apuestas;

    /**
     * Constructor que inicializa el apostador con un ID, un nombre y una lista de
     * apuestas.
     * 
     * @param id       el ID del apostador
     * @param nombre   el nombre del apostador
     * @param apuestas la lista de apuestas
     */
    public Apostador(String id, String nombre, List<Apuesta> apuestas) {
        this.id = id;
        this.nombre = nombre;
        this.apuestas = apuestas;
    }

    /**
     * Constructor que inicializa el apostador con un ID y un nombre.
     * 
     * @param id     el ID del apostador
     * @param nombre el nombre del apostador
     */
    public Apostador(String id, String nombre) {
        this(id, nombre, new ArrayList<>());
    }

    public Apostador() {
    }

    /**
     * Obtiene el ID del apostador.
     * 
     * @return el ID del apostador
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del apostador.
     * 
     * @param id el ID del apostador
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del apostador.
     * 
     * @return el nombre del apostador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del apostador.
     * 
     * @param nombre el nombre del apostador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de apuestas del apostador.
     * 
     * @return la lista de apuestas
     */
    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    /**
     * Establece la lista de apuestas del apostador.
     * 
     * @param apuestas la lista de apuestas
     */
    public void setApuestas(List<Apuesta> apuestas) {
        this.apuestas = apuestas;
    }

    /**
     * Agrega una apuesta a la lista de apuestas del apostador.
     * 
     * @param apuesta la apuesta a agregar
     */
    public void apostar(Apuesta apuesta) {
        this.apuestas.add(apuesta);
    }
}
