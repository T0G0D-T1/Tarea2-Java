package back;

//hereda de exception para crear una excepción personalizada que se lanzará cuando falten datos en el CSV
public class InformacionIncompletaException extends Exception {

    // Este es el constructor que recibirá el texto del error (ej: "Faltan datos en la línea 5")
    public InformacionIncompletaException(String mensaje) {
        super(mensaje); // 'super' le pasa el mensaje a la clase Exception base
    }
}
