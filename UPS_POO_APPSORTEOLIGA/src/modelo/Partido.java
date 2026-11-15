package modelo;

import java.io.Serializable;
import java.util.Random;

public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Equipo local;
    private final Equipo visitante;
    private Equipo ganador;

    public Partido(Equipo local, Equipo visitante) {
        this.local = local;
        this.visitante = visitante;
    }

    public Equipo getLocal() { return local; }
    public Equipo getVisitante() { return visitante; }
    public Equipo getGanador() { return ganador; }

    /** Dos partidos son iguales si enfrentan a los mismos equipos . */
    public boolean mismoEnfrentamiento(Partido otro) {
        return (local.equals(otro.local) && visitante.equals(otro.visitante))
            || (local.equals(otro.visitante) && visitante.equals(otro.local));
    }

    /** Define aleatoriamente quién avanza a la siguiente etapa. */
    public Equipo simularGanador(Random random) {
        ganador = random.nextBoolean() ? local : visitante;
        return ganador;
    }

    @Override
    public String toString() {
        return local + " vs " + visitante;
    }
}
