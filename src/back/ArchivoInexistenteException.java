package back;

public class ArchivoInexistenteException extends RuntimeException {
    public ArchivoInexistenteException(String mensaje) {
        super(mensaje);
    }
}
