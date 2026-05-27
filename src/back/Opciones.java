package back;

public class Opciones {
    private Ejercicio[] lista;
    private int cantidadEjercicios;

    public Opciones() {
        this.lista = new Ejercicio[100];
        this.cantidadEjercicios = 0;
    }

    public void crearEjercicio(Ejercicio ejercicio) {
        if (cantidadEjercicios < 100) {
            this.lista[cantidadEjercicios] = ejercicio;
            cantidadEjercicios++;
        } else {
            System.out.println("No se pueden agregar mas ejercicios, limite alcanzado.");
        }
    }

    public void actualizarEjercicio(int codigo, String nombre, String tipo, String intensidad, int tiempo_estimado, String descripcion) {
        for (int i = 0; i < cantidadEjercicios; i++) {
            if (lista[i].getCodigo() == codigo) {
                lista[i].actualizar(nombre, tipo, intensidad, tiempo_estimado, descripcion);
                System.out.println("Ejercicio actualizado exitosamente.");
                return;
            }
        }
        System.out.println("Ejercicio con codigo " + codigo + " no encontrado.");
    }

    public void eliminarEjercicio(int codigo) {
        for (int i = 0; i < cantidadEjercicios; i++) {
            if (lista[i].getCodigo() == codigo) {
                for (int j = i; j < cantidadEjercicios - 1; j++) {
                    lista[j] = lista[j + 1];
                }
                cantidadEjercicios--;
                System.out.println("Ejercicio eliminado exitosamente.");
                return;
            }
        }
    }

    public void mostrarEjercicio(int codigo) {
        for (int i = 0; i < cantidadEjercicios; i++) {
            lista[i].mostrar();
            System.out.println("------------------------");
        }
    }

    public void buscarPorIntensidad(String intensidad) {
        for (int i = 0; i < cantidadEjercicios; i++) {
            if (lista[i].getIntensidad().equals(intensidad)) {
                lista[i].mostrar();
                System.out.println("------------------------");
            }
        }
    }

    public void generarRutina(int cantidad_deseada, String intensidad, int semana_actual) {
        int contador_ejercicios = 0;
        int tiempo_estimado = 0;

        System.out.println("\n--- Rutina generada ---");

        for (int i = 0; i < cantidadEjercicios && contador_ejercicios < cantidad_deseada; i++) {
            if (lista[i].getIntensidad().equals(intensidad) &&
                lista[i].getUltimaSemana() != semana_actual) {

                lista[i].mostrar();
                System.out.println("------------------------");
                tiempo_estimado += lista[i].getTiempo();
                contador_ejercicios++;

                lista[i].getUltimaSemana();
            }
        }

        if (contador_ejercicios == 0) {
            System.out.println("No se encontraron ejercicios con esa intensidad");
        }

        System.out.println("Tiempo total estimado para la rutina: " + tiempo_estimado + " minutos");
    }

    // ========== NUEVOS MÉTODOS (compatibilidad con GestorAplicacion) ==========
    public int getCantidadTotalEjercicios() {
        return cantidadEjercicios;
    }

    public Ejercicio obtenerEjercicio(int indice) {
        if (indice >= 0 && indice < cantidadEjercicios) {
            return lista[indice];
        }
        return null;
    }
}
