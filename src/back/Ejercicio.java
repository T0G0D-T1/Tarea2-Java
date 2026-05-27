package back;
 
public class Ejercicio {
    private int codigo;
    private String nombre;
    private String tipo;
    private String intensidad;
    private int tiempo_estimado;
    private String descripcion;
    private int ultima_semana;
 
    public Ejercicio(int codigo, String nombre, String tipo, String intensidad, int tiempo_estimado, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.intensidad = intensidad;
        this.tiempo_estimado = tiempo_estimado;
        this.descripcion = descripcion;
        this.ultima_semana = 0;
    }
 
    public int getCodigo() {
        return this.codigo;
    }
 
    public String getNombre() {
        return this.nombre;
    }
 
    public String getTipo() {
        return this.tipo;
    }
 
    public String getIntensidad() {
        return this.intensidad;
    }
 
    public int getTiempo() {
        return this.tiempo_estimado;
    }
 
    public int getTiempoMinutos() {
        return this.tiempo_estimado;
    }
 
    public String getDescripcion() {
        return this.descripcion;
    }
 
    public String getNivelIntensidad() {
        return this.intensidad;
    }
 
    public int getUltimaSemana() {
        return this.ultima_semana;
    }
 
    public void setUltimaSemana(int semana) {
        this.ultima_semana = semana;
    }
 
    public void setUltima_semana(int semana) {
        this.ultima_semana = semana;
    }
 
    public boolean fueUsadoSemanaPasada(int semanaActual) {
        return this.ultima_semana == semanaActual - 1;
    }
 
    public void mostrar() {
        System.out.println("Codigo: " + this.codigo);
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Tipo: " + this.tipo);
        System.out.println("Intensidad: " + this.intensidad);
        System.out.println("Tiempo estimado: " + this.tiempo_estimado + " minutos");
        System.out.println("Descripcion: " + this.descripcion);
        System.out.println("Ultima semana realizado: " + this.ultima_semana);
    }
 
    public void actualizar(String nombreN, String tipoN, String intensidadN, int tiempo_estimadoN, String descripcionN) {
        this.nombre = nombreN;
        this.tipo = tipoN;
        this.intensidad = intensidadN;
        this.tiempo_estimado = tiempo_estimadoN;
        this.descripcion = descripcionN;
    }
}
 