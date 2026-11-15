package modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Torneo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Equipo> equipos;
    private final Map<String, List<Partido>> etapas = new LinkedHashMap<>();
    private Equipo campeon;

    public Torneo(List<Equipo> equipos) throws NumeroEquiposInvalidoException {
        validarNumeroEquipos(equipos.size());
        this.equipos = new ArrayList<>(equipos);
    }

    /** Crea un torneo con los 16 equipos iniciales de la liga. */
    public static Torneo crearLigaPorDefecto() throws NumeroEquiposInvalidoException {
        String[] nombres = {
            "Independiente del Valle", "Barcelona Sporting Club", "Club Sport Emelec", "Universidad Católica", "Sociedad Deportiva Aucas", "Macará", "Mushuc Runa", "Orense SC",
            "Técnico Universitario", "Delfín SC", "Deportivo Cuenca", "Libertad FC", "Guayaquil City FC", "Leones FC", "Manta FC", "Nacional"
        };
        List<Equipo> lista = new ArrayList<>();
        for (String n : nombres) lista.add(new Equipo(n));
        return new Torneo(lista);
    }

    /** Valida que n sea potencia de 2 y al menos 2 (16, 8, 4, 2). */
    public static void validarNumeroEquipos(int n) throws NumeroEquiposInvalidoException {
        if (n < 2 || (n & (n - 1)) != 0) {
            throw new NumeroEquiposInvalidoException(
                "Número de equipos inválido: " + n + ". Debe ser una potencia de 2 (16, 8, 4, 2).");
        }
    }

    /** Realiza el sorteo de todas las etapas, avanzando ganadores simulados. */
    public void realizarSorteo() throws NumeroEquiposInvalidoException, SorteoInvalidoException {
        etapas.clear();
        campeon = null;
        Random random = new Random();
        List<Equipo> actuales = new ArrayList<>(equipos);

        while (actuales.size() >= 2) {
            validarNumeroEquipos(actuales.size());
            List<Equipo> barajados = new ArrayList<>(actuales);
            Collections.shuffle(barajados, random);

            List<Partido> partidos = new ArrayList<>();
            sortearEtapa(barajados, partidos);
            etapas.put(nombreEtapa(actuales.size()), partidos);

            List<Equipo> siguientes = new ArrayList<>();
            for (Partido p : partidos) siguientes.add(p.simularGanador(random));
            actuales = siguientes;
        }
        campeon = actuales.get(0);
    }

    /**
     * Algoritmo recursivo para emparejar equipos.
     * Caso base: no quedan equipos por emparejar.
     * Caso recursivo: empareja el primer equipo con un rival válido y repite con el resto.
     */
    private void sortearEtapa(List<Equipo> restantes, List<Partido> partidos) throws SorteoInvalidoException {
        if (restantes.isEmpty()) return; // caso base

        if (restantes.size() == 1) {
            throw new SorteoInvalidoException("El equipo " + restantes.get(0) + " no tiene oponente disponible.");
        }

        Equipo local = restantes.get(0);
        List<Equipo> candidatos = new ArrayList<>(restantes.subList(1, restantes.size()));
        Collections.shuffle(candidatos);

        for (Equipo rival : candidatos) {
            if (rival.equals(local)) continue;                   // no puede ser competidor y oponente a la vez
            Partido nuevo = new Partido(local, rival);
            if (existeEnfrentamiento(partidos, nuevo)) continue; // no repetir partido en la etapa

            List<Equipo> resto = new ArrayList<>(restantes);
            resto.remove(local);
            resto.remove(rival);

            partidos.add(nuevo);
            try {
                sortearEtapa(resto, partidos); // llamada recursiva
                return;
            } catch (SorteoInvalidoException e) {
                partidos.remove(nuevo);        // retroceso: probar otro rival
            }
        }
        throw new SorteoInvalidoException("No existe un oponente válido para el equipo " + local + ".");
    }

    private boolean existeEnfrentamiento(List<Partido> partidos, Partido p) {
        for (Partido existente : partidos) {
            if (existente.mismoEnfrentamiento(p)) return true;
        }
        return false;
    }

    private String nombreEtapa(int numEquipos) {
        switch (numEquipos) {
            case 16: return "OCTAVOS DE FINAL";
            case 8:  return "CUARTOS DE FINAL";
            case 4:  return "SEMIFINALES";
            case 2:  return "FINAL";
            default: return "RONDA DE " + numEquipos + " EQUIPOS";
        }
    }


    public void guardar(String ruta) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(this);
        }
    }

    public static Torneo cargar(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ruta))) {
            return (Torneo) in.readObject();
        }
    }

    public Map<String, List<Partido>> getEtapas() { return etapas; }
    public List<Equipo> getEquipos() { return equipos; }
    public Equipo getCampeon() { return campeon; }
    public boolean haySorteo() { return !etapas.isEmpty(); }
}
