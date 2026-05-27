package back;
 
import java.util.ArrayList;
import java.util.List;
 
public class GestorAplicacion {
    private Opciones opciones;
    private Rutina rutinaActual;
    private List<EventListener> listeners;
 
    public GestorAplicacion() {
        this.opciones = new Opciones();
        this.rutinaActual = null;
        this.listeners = new ArrayList<>();
    }
 
    //sistema suscriptor de eventos: permite a la interfaz gráfica suscribirse para recibir notificaciones
    public void suscribirse(EventListener listener) {
        listeners.add(listener);
    }
 
    private void notificar(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
 
    public void notificarError(String mensaje) {
        notificar(new Event(Event.ERROR_GENERACION, mensaje));
    }
 
    public void notificarRutinaGenerada(Rutina rutina) {
        notificar(new Event(Event.RUTINA_GENERADA, rutina));
    }
 
    //gesto ejercicios
    public void crearEjercicio(Ejercicio ejercicio) {
        opciones.crearEjercicio(ejercicio);
    }
 
    public void actualizarEjercicio(int codigo, String nombre, String tipo, String intensidad, 
                                    int tiempo_estimado, String descripcion) {
        opciones.actualizarEjercicio(codigo, nombre, tipo, intensidad, tiempo_estimado, descripcion);
    }
 
    public void eliminarEjercicio(int codigo) {
        opciones.eliminarEjercicio(codigo);
    }
 
    public void mostrarEjercicio(int codigo) {
        opciones.mostrarEjercicio(codigo);
    }
 
    // cargar ejercicios desde archivo CSV
    public void cargarEjerciciosDesdeArchivo(String ruta) {
        try {
            //cargar ejercicios usando el LectorCSV
            ArrayList<Ejercicio> ejerciciosCargados = LectorCSV.cargarEjercicios(ruta);
            
            //agrega ejercicios a lista
            for (Ejercicio ej : ejerciciosCargados) {
                opciones.crearEjercicio(ej);
            }
            
            System.out.println("✓ Ejercicios cargados: " + ejerciciosCargados.size());
            notificar(new Event(Event.ARCHIVO_CARGADO));
 
        } catch (ArchivoInexistenteException e) {
            notificar(new Event(Event.ERROR_CARGA, e.getMessage()));
        } catch (InformacionIncompletaException e) {
            notificar(new Event(Event.ERROR_CARGA, e.getMessage()));
        } catch (FormatoIncorrectoException e) {
            notificar(new Event(Event.ERROR_CARGA, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            notificar(new Event(Event.ERROR_CARGA, "Error al cargar archivo: " + e.getMessage()));
        }
    }
 
    //validaciones para generacion de rutina
    public boolean validarParametrosRutina(int cantCardio, int cantFuerza, int[] cantidadPorIntensidad) {
        //validar que la cantidad total de ejercicios solicitados sea mayor a 0
        int cantidadTotal = cantCardio + cantFuerza;
        if (cantidadTotal == 0) {
            notificar(new Event(Event.ERROR_GENERACION, 
                "Debe especificar al menos un ejercicio"));
            return false;
        }
 
        //validar que haya suficientes ejercicios cargados para generar la rutina
        if (cantidadTotal > opciones.getCantidadTotalEjercicios()) {
            notificar(new Event(Event.ERROR_GENERACION, 
                "No hay suficientes ejercicios cargados. Disponibles: " + 
                opciones.getCantidadTotalEjercicios()));
            return false;
        }
 
        return true;
    }
 
    public Rutina generarRutina(int cantCardio, int cantFuerza, int[] cantidadPorIntensidad) {
        Rutina rutina = new Rutina("Cliente", 1); // Nombre y semana placeholder
        int contadorCardio = 0;
        int contadorFuerza = 0;
 
        //cuenta ejercicios de cada tipo e intensidad necesarios
        int[] necesariosPorIntensidad = new int[4];
        for (int i = 0; i < 4; i++) {
            necesariosPorIntensidad[i] = cantidadPorIntensidad[i];
        }
 
        //recorre lista de ejercicios cargados para agregar a la rutina según preferencias
        for (int i = 0; i < opciones.getCantidadTotalEjercicios(); i++) {
            Ejercicio ejercicio = opciones.obtenerEjercicio(i);
            if (ejercicio == null) continue;
 
            String intensidad = ejercicio.getIntensidad().toLowerCase();
            int nivelIntensidad = mapearIntensidad(intensidad);
 
            //verifica si necesitamos más ejercicios de esta intensidad
            if (necesariosPorIntensidad[nivelIntensidad] <= 0) {
                continue;
            }
 
            //agrega segun cardio o fuerza
            if (ejercicio.getTipo().equalsIgnoreCase("Cardiovascular") && contadorCardio < cantCardio) {
                //agrega ejercicio a la rutina segun tipo
                if (rutina.agregarEjercicio(ejercicio, 1)) {
                    contadorCardio++;
                    necesariosPorIntensidad[nivelIntensidad]--;
                }
            } else if (ejercicio.getTipo().equalsIgnoreCase("Fuerza") && contadorFuerza < cantFuerza) {
                //agrega ejercicio a la rutina segun tipo
                if (rutina.agregarEjercicio(ejercicio, 1)) {
                    contadorFuerza++;
                    necesariosPorIntensidad[nivelIntensidad]--;
                }
            }
 
            //si no quedan ejercicios, salir del ciclo
            if (contadorCardio >= cantCardio && contadorFuerza >= cantFuerza) {
                break;
            }
        }
 
        //validacion que se puede crear rutina con los ejercicios disponibles
        if (rutina.getCantidadTotal() < (cantCardio + cantFuerza)) {
            notificar(new Event(Event.ERROR_GENERACION, 
                "No se pudo generar la rutina con los parámetros especificados"));
            return null;
        }
 
        return rutina;
    }
 
    //mapea intensidad a un nivel numérico para facilitar comparación (sugerencia cloude)
    private int mapearIntensidad(String intensidad) {
        if (intensidad.contains("basico") || intensidad.contains("básico")) {
            return 0;
        } else if (intensidad.contains("intermedio")) {
            return 1;
        } else if (intensidad.contains("avanzado")) {
            return 2;
        } else if (intensidad.contains("alto") || intensidad.contains("rendimiento")) {
            return 3;
        }
        return 0; //por defecto, si no se reconoce la intensidad, se asigna al nivel más bajo
    }
 
    public void setRutinaActual(Rutina rutina) {
        this.rutinaActual = rutina;
    }
 
    public Rutina getRutinaActual() {
        return rutinaActual;
    }
 
    public int getCantidadEjerciciosCargados() {
        return opciones.getCantidadTotalEjercicios();
    }
}