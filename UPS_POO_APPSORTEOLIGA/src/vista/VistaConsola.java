package vista;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import modelo.Equipo;
import modelo.Partido;

/** Vista: solo entrada/salida, sin lógica de negocio como se pide en la tarea. */
public class VistaConsola {
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        System.out.println("\n=== SORTEO DE LIGA PROFESIONAL ===");
        System.out.println("1. Realizar nuevo sorteo");
        System.out.println("2. Mostrar sorteo actual");
        System.out.println("3. Guardar torneo (torneo.ser)");
        System.out.println("4. Cargar torneo (torneo.ser)");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public String leerOpcion() {
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
    }

    public void mostrarEquipos(List<Equipo> equipos) {
        System.out.println("\nEquipos participantes (" + equipos.size() + "):");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i));
        }
    }

    public void mostrarSorteo(Map<String, List<Partido>> etapas, Equipo campeon) {
        for (Map.Entry<String, List<Partido>> etapa : etapas.entrySet()) {
            System.out.println("\n--- " + etapa.getKey() + " ---");
            int n = 1;
            for (Partido p : etapa.getValue()) {
                System.out.println("Partido " + n++ + ": " + p + "  -> avanza: " + p.getGanador());
            }
        }
        if (campeon != null) {
            System.out.println("\n*** CAMPEÓN: " + campeon + " ***");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }
}
