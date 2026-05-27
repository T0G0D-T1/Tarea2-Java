package back;

import java.util.ArrayList;
 
public class Rutina {
    private String nombreCliente;
    private int numeroSemana;
    private ArrayList<Ejercicio>  ejercicios;
 
    //constructor
    public Rutina(String nombreCliente, int numeroSemana){
        this.nombreCliente = nombreCliente;
        this.numeroSemana = numeroSemana;
        this.ejercicios = new ArrayList<>();
    }
 
    public boolean agregarEjercicio(Ejercicio ejercicio, int numeroSemana){
 
        
        if (ejercicio.fueUsadoSemanaPasada(numeroSemana)){
            return false; 
        }
 
        ejercicios.add(ejercicio);
        ejercicio.setUltima_semana(numeroSemana); 
        return true;
    }
 
    //Metodos para navegacion y estadistica y getters
    public int getCantidadEjercicios(){
        return ejercicios.size();
    }
 
    public Ejercicio getEjercicioIndice(int indice){
        if (indice >= 0 && indice < ejercicios.size()){
            return ejercicios.get(indice);
        }
        return null;
    }
 
    public int calcularTiempoTotal(){
        int total = 0;
        for (Ejercicio ej : ejercicios){
            total += ej.getTiempo();
        }
        return total;
    }
 
    public int getCantidadPorTipo (String tipo){
        int contador = 0;
        for (Ejercicio ej : ejercicios){
            if (ej.getTipo().equalsIgnoreCase(tipo)){
                contador++;
            }
 
        }
        return contador;
    }
 
    public int getCantidadPorNivel (String nivel){
        int contador = 0;
        for (Ejercicio ej : ejercicios){
            if (ej.getIntensidad().equalsIgnoreCase(nivel)){
                contador++;
            }
 
        }
        return contador;
    }
 
    public String getNombreCliente(){
        return nombreCliente;
    }
 
    public int getNumeroSemana(){
        return numeroSemana;
    }
 
    public int getCantidadTotal() {
        return ejercicios.size();
    }
 
    public int getTiempoTotal() {
        return calcularTiempoTotal();
    }
 
    public Ejercicio getEjercicio(int indice) {
        return getEjercicioIndice(indice);
    }
 
    public int contarPorTipo(String tipo) {
        return getCantidadPorTipo(tipo);
    }
 
    public int contarPorIntensidad(String intensidad) {
        return getCantidadPorNivel(intensidad);
    }
 
    public boolean tieneEjercicioAnterior(int indice) {
        return indice > 0;
    }
 
    public boolean tieneEjercicioSiguiente(int indice) {
        return indice < ejercicios.size() - 1;
    }
 
    public boolean esUltimoEjercicio(int indice) {
        return indice == ejercicios.size() - 1;
    }
}
 