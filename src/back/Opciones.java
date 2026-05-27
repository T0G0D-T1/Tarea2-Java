package back;

import java.util.ArrayList;
import java.util.List;

public class Opciones {
    private List<Ejercicio> lista;

    public Opciones() {
        this.lista = new ArrayList<>();
    }

    //agrega ejercicio a la lista
    public boolean crearEjercicio(Ejercicio ejercicio) {
        return lista.add(ejercicio);
    }

    //actualiza ejercicio 
    public boolean actualizarEjercicio(int codigo, String nombre, String tipo, String intensidad, int tiempo_estimado, String descripcion) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigo) {
                lista.get(i).actualizar(nombre, tipo, intensidad, tiempo_estimado, descripcion);
                return true;
            }
        }
        return false;
    }

    // Elimina un ejercicio por código. Devuelve true si se eliminó.
    public boolean eliminarEjercicio(int codigo) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigo) {
                lista.remove(i);
                return true;
            }
        }
        return false;
    }

    // Devuelve el ejercicio con el código dado o null si no existe.
    public Ejercicio mostrarEjercicio(int codigo) {
        for (Ejercicio e : lista) {
            if (e.getCodigo() == codigo) {
                return e;
            }
        }
        return null;
    }

    // Busca ejercicios por intensidad y devuelve la lista encontrada.
    public List<Ejercicio> buscarPorIntensidad(String intensidad) {
        List<Ejercicio> encontrados = new ArrayList<>();
        for (Ejercicio e : lista) {
            if (e.getIntensidad().equalsIgnoreCase(intensidad)) {
                encontrados.add(e);
            }
        }
        return encontrados;
    }

    // Genera una lista de ejercicios según criterios y devuelve los ejercicios seleccionados.
    public List<Ejercicio> generarRutina(int cantidad_deseada, String intensidad, int semana_actual) {
        List<Ejercicio> seleccion = new ArrayList<>();
        int contador_ejercicios = 0;

        for (int i = 0; i < lista.size() && contador_ejercicios < cantidad_deseada; i++) {
            Ejercicio e = lista.get(i);
            if (e.getIntensidad().equalsIgnoreCase(intensidad) && e.getUltimaSemana() != semana_actual) {
                seleccion.add(e);
                contador_ejercicios++;
            }
        }

        return seleccion;
    }

    // ========== NUEVOS MÉTODOS (compatibilidad con GestorAplicacion) ==========
    public int getCantidadTotalEjercicios() {
        return lista.size();
    }

    public Ejercicio obtenerEjercicio(int indice) {
        if (indice >= 0 && indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }
}
