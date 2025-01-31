package dnk.casino.api.Ruleta;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "ruleta")
public class Ruleta {
    @Id
    private String id;
    private int numeroGanador;
    private List<Apostador> apostadores;
    private boolean ruletaAbierta;

    // Constructor
    public Ruleta() {
        this.apostadores = new ArrayList<>();
        this.ruletaAbierta = true;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumeroGanador() {
        return numeroGanador;
    }

    public void setNumeroGanador(int numeroGanador) {
        this.numeroGanador = numeroGanador;
    }

    public List<Apostador> getApostadores() {
        return apostadores;
    }

    public void setApostadores(List<Apostador> apostadores) {
        this.apostadores = apostadores;
    }

    public boolean isRuletaAbierta() {
        return ruletaAbierta;
    }

    public void setRuletaAbierta(boolean ruletaAbierta) {
        this.ruletaAbierta = ruletaAbierta;
    }
}