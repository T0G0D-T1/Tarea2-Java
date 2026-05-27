package back;

public class Event {
    //tipos de eventos
    public static final String ARCHIVO_CARGADO = "ARCHIVO_CARGADO";
    public static final String ERROR_CARGA = "ERROR_CARGA";
    public static final String RUTINA_GENERADA = "RUTINA_GENERADA";
    public static final String ERROR_GENERACION = "ERROR_GENERACION";

    private String tipo;
    private Object dato;

    public Event(String tipo) {
        this.tipo = tipo;
        this.dato = null;
    }

    public Event(String tipo, Object dato) {
        this.tipo = tipo;
        this.dato = dato;
    }

    public String getTipo() {
        return tipo;
    }

    public Object getDato() {
        return dato;
    }
}
