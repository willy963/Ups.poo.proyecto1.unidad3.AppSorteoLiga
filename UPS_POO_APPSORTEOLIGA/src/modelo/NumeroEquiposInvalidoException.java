package modelo;

/** Se lanza cuando el número de equipos no es válido para eliminación directa. */
public class NumeroEquiposInvalidoException extends Exception {
    private static final long serialVersionUID = 1L;

    public NumeroEquiposInvalidoException(String mensaje) {
        super(mensaje);
    }
}
