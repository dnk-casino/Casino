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

    public void addApostador(Apostador apostador) {
        this.apostadores.add(apostador);
    }

    public void eliminarApostador(Apostador apostador) {
        this.apostadores.remove(apostador);
    }

    public String getColorGanador() {
        if (this.numeroGanador == 0) {
            return "verde";
        } else {
            return this.numeroGanador % 2 == 0 ? "negro" : "rojo";
        }
    }

    public int girar() {
        return this.numeroGanador = (int) (Math.random() * 37);
    }

    public List<Object[]> determinarGanadores() {
        List<Object[]> ganadores = new ArrayList<>();
        for (Apostador apostador : this.apostadores) {
            for (Apuesta apuesta : apostador.getApuestas()) {
                switch (apuesta.getTipo()) {
                    case PLENO -> {
                        if (apuesta.getN1() == this.numeroGanador) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 35 });
                        }
                    }
                    case CABALLO -> {
                        if (apuesta.getN1() == this.numeroGanador || apuesta.getN2() == this.numeroGanador) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 17 });
                        }
                    }
                    case TRANSVERSAL -> {
                        if (apuesta.getN1() == this.numeroGanador || apuesta.getN2() == this.numeroGanador
                                || apuesta.getN3() == this.numeroGanador) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 11 });
                        }
                    }
                    case CUADRO -> {
                        if (apuesta.getN1() == this.numeroGanador || apuesta.getN2() == this.numeroGanador
                                || apuesta.getN3() == this.numeroGanador || apuesta.getN4() == this.numeroGanador) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 8 });
                        }
                    }
                    case COLOR -> {
                        if (this.numeroGanador == 0) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 0.5 });
                        } else if (this.getColorGanador().equals(apuesta.getColor().toString().toLowerCase())) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                        }
                    }
                    case PAR -> {
                        if (this.numeroGanador == 0) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 0.5 });
                        } else if ((this.numeroGanador % 2 == 0 && !apuesta.isImpar())
                                || (this.numeroGanador % 2 != 0 && apuesta.isImpar())) {
                            ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                        }
                    }
                    case OTROS -> {
                        switch (apuesta.getOtros()) {
                            case ALTO -> {
                                if (this.numeroGanador == 0) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 0.5 });
                                } else if (this.numeroGanador >= 19) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case BAJO -> {
                                if (this.numeroGanador == 0) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 0.5 });
                                } else if (this.numeroGanador <= 18 && this.numeroGanador != 0) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case DOCENA1 -> {
                                if (this.numeroGanador >= 1 && this.numeroGanador <= 12) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case DOCENA2 -> {
                                if (this.numeroGanador >= 13 && this.numeroGanador <= 24) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case DOCENA3 -> {
                                if (this.numeroGanador >= 25 && this.numeroGanador <= 36) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case DOBLEDOCENA1 -> {
                                if ((this.numeroGanador >= 1 && this.numeroGanador <= 12)
                                        || (this.numeroGanador >= 13 && this.numeroGanador <= 24)) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 1.5 });
                                }
                            }
                            case DOBLEDOCENA2 -> {
                                if ((this.numeroGanador >= 13 && this.numeroGanador <= 24)
                                        || (this.numeroGanador >= 25 && this.numeroGanador <= 36)) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 1.5 });
                                }
                            }
                            case COLUMNA1 -> {
                                if (this.numeroGanador == 1 || this.numeroGanador == 4 || this.numeroGanador == 7
                                        || this.numeroGanador == 10 || this.numeroGanador == 13
                                        || this.numeroGanador == 16 || this.numeroGanador == 19
                                        || this.numeroGanador == 22 || this.numeroGanador == 25
                                        || this.numeroGanador == 28 || this.numeroGanador == 31
                                        || this.numeroGanador == 34) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case COLUMNA2 -> {
                                if (this.numeroGanador == 2 || this.numeroGanador == 5 || this.numeroGanador == 8
                                        || this.numeroGanador == 11 || this.numeroGanador == 14
                                        || this.numeroGanador == 17 || this.numeroGanador == 20
                                        || this.numeroGanador == 23 || this.numeroGanador == 26
                                        || this.numeroGanador == 29 || this.numeroGanador == 32
                                        || this.numeroGanador == 35) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case COLUMNA3 -> {
                                if (this.numeroGanador == 3 || this.numeroGanador == 6 || this.numeroGanador == 9
                                        || this.numeroGanador == 12 || this.numeroGanador == 15
                                        || this.numeroGanador == 18 || this.numeroGanador == 21
                                        || this.numeroGanador == 24 || this.numeroGanador == 27
                                        || this.numeroGanador == 30 || this.numeroGanador == 33
                                        || this.numeroGanador == 36) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 2 });
                                }
                            }
                            case DOBLECOLUMNA1 -> {
                                if ((this.numeroGanador == 1 || this.numeroGanador == 4 || this.numeroGanador == 7
                                        || this.numeroGanador == 10 || this.numeroGanador == 13
                                        || this.numeroGanador == 16 || this.numeroGanador == 19
                                        || this.numeroGanador == 22 || this.numeroGanador == 25
                                        || this.numeroGanador == 28 || this.numeroGanador == 31
                                        || this.numeroGanador == 34) ||
                                        (this.numeroGanador == 2 || this.numeroGanador == 5 || this.numeroGanador == 8
                                                || this.numeroGanador == 11 || this.numeroGanador == 14
                                                || this.numeroGanador == 17 || this.numeroGanador == 20
                                                || this.numeroGanador == 23 || this.numeroGanador == 26
                                                || this.numeroGanador == 29 || this.numeroGanador == 32
                                                || this.numeroGanador == 35)) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 1.5 });
                                }
                            }
                            case DOBLECOLUMNA2 -> {
                                if ((this.numeroGanador == 2 || this.numeroGanador == 5 || this.numeroGanador == 8
                                        || this.numeroGanador == 11 || this.numeroGanador == 14
                                        || this.numeroGanador == 17 || this.numeroGanador == 20
                                        || this.numeroGanador == 23 || this.numeroGanador == 26
                                        || this.numeroGanador == 29 || this.numeroGanador == 32
                                        || this.numeroGanador == 35) ||
                                        (this.numeroGanador == 3 || this.numeroGanador == 6 || this.numeroGanador == 9
                                                || this.numeroGanador == 12 || this.numeroGanador == 15
                                                || this.numeroGanador == 18 || this.numeroGanador == 21
                                                || this.numeroGanador == 24 || this.numeroGanador == 27
                                                || this.numeroGanador == 30 || this.numeroGanador == 33
                                                || this.numeroGanador == 36)) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 1.5 });
                                }
                            }
                            case SEISENA1 -> {
                                if (this.numeroGanador >= 1 && this.numeroGanador <= 6) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                            case SEISENA2 -> {
                                if (this.numeroGanador >= 7 && this.numeroGanador <= 12) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                            case SEISENA3 -> {
                                if (this.numeroGanador >= 13 && this.numeroGanador <= 18) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                            case SEISENA4 -> {
                                if (this.numeroGanador >= 19 && this.numeroGanador <= 24) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                            case SEISENA5 -> {
                                if (this.numeroGanador >= 25 && this.numeroGanador <= 30) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                            case SEISENA6 -> {
                                if (this.numeroGanador >= 31 && this.numeroGanador <= 36) {
                                    ganadores.add(new Object[] { apostador, apuesta.getCantidad() * 5 });
                                }
                            }
                        }
                    }
                }
            }
        }
        return ganadores;
    }
}