package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String nombre;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        return nombre.equalsIgnoreCase(((Equipo) o).nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre;
    }
}
