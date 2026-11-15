package controlador;

import java.io.IOException;

import modelo.NumeroEquiposInvalidoException;
import modelo.SorteoInvalidoException;
import modelo.Torneo;
import vista.VistaConsola;

/** Controlador: mediador entre la carpeta Vista y el Modelo. */
public class ControladorTorneo {
    private static final String ARCHIVO = "torneo.ser";

    private Torneo torneo;
    private final VistaConsola vista;

    public ControladorTorneo(VistaConsola vista) {
        this.vista = vista;
    }

    public void iniciar() {
        try {
            torneo = Torneo.crearLigaPorDefecto();
        } catch (NumeroEquiposInvalidoException e) {
            vista.mostrarError(e.getMessage());
            return;
        }
        vista.mostrarEquipos(torneo.getEquipos());

        boolean salir = false;
        while (!salir) {
            vista.mostrarMenu();
            switch (vista.leerOpcion()) {
                case "1": sortear(); break;
                case "2": mostrar(); break;
                case "3": guardar(); break;
                case "4": cargar(); break;
                case "0": salir = true; vista.mostrarMensaje("¡Hasta luego!"); break;
                default: vista.mostrarError("Opción no válida.");
            }
        }
    }

    private void sortear() {
        try {
            torneo.realizarSorteo();
            mostrar();
        } catch (NumeroEquiposInvalidoException | SorteoInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void mostrar() {
        if (!torneo.haySorteo()) {
            vista.mostrarMensaje("Aún no se ha realizado ningún sorteo.");
            return;
        }
        vista.mostrarSorteo(torneo.getEtapas(), torneo.getCampeon());
    }

    private void guardar() {
        try {
            torneo.guardar(ARCHIVO);
            vista.mostrarMensaje("Torneo guardado en " + ARCHIVO);
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar el torneo: " + e.getMessage());
        }
    }

    private void cargar() {
        try {
            torneo = Torneo.cargar(ARCHIVO);
            vista.mostrarMensaje("Torneo cargado correctamente.");
            mostrar();
        } catch (IOException e) {
            vista.mostrarError("No se pudo leer el archivo " + ARCHIVO + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            vista.mostrarError("Formato de archivo incompatible: " + e.getMessage());
        }
    }
}
