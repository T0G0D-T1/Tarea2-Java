package front;


import javax.swing.*;

import back.Event;
import back.EventListener;
import back.GestorAplicacion;
import back.Rutina;

import java.awt.*;
import java.awt.event.*;

//clase principal
public class main extends JFrame implements EventListener {
    private GestorAplicacion gestor;
    private JPanel panelPrincipal;
    private JLabel labelEstado;
    private JTextArea areaInformacion;
    
    //botones principales
    private JButton botonCargarArchivo;
    private JButton botonGenerarRutina;
    private JButton botonVerResumen;
    private JButton botonVerRevision;
    private JButton botonVerEjercicios;
    private JButton botonLimpiar;

    public main() {
        setTitle("Gestor de Rutinas de Ejercicio");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gestor = new GestorAplicacion();
        gestor.suscribirse(this);
        
        inicializarComponentes();
        setVisible(true);
    }

    //componentes de la interfaz
    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior
        JLabel titulo = new JLabel("Sistema de Gestión de Rutinas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // botones y area de información
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 20));
        
        // Panel izquierdo: botones
        JPanel panelBotones = new JPanel(new GridLayout(6, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Opciones Disponibles"));
        
        botonCargarArchivo = crearBoton("Cargar Archivo CSV", 
            e -> abrirVentanaCargarArchivo());
        botonGenerarRutina = crearBoton("Generar Rutina", 
            e -> abrirVentanaGenerarRutina());
        botonVerResumen = crearBoton("Ver Resumen de Rutina", 
            e -> abrirVentanaResumen());
        botonVerRevision = crearBoton("Ver Revisión de Rutina", 
            e -> abrirVentanaRevision());
        botonVerEjercicios = crearBoton("Ver Ejercicios Cargados", 
            e -> mostrarEjercicios());
        botonLimpiar = crearBoton("🔄 Limpiar", 
            e -> limpiarAplicacion());
        
        panelBotones.add(botonCargarArchivo);
        panelBotones.add(botonGenerarRutina);
        panelBotones.add(botonVerResumen);
        panelBotones.add(botonVerRevision);
        panelBotones.add(botonVerEjercicios);
        panelBotones.add(botonLimpiar);
        
        //panel derecho: informacion
        JPanel panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información"));
        
        //area de texto con scroll
        areaInformacion = new JTextArea();
        areaInformacion.setEditable(false);
        areaInformacion.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaInformacion.setText("Bienvenido al Sistema de Gestión de Rutinas\n\n" +
                "1. Cargue un archivo CSV con ejercicios\n" +
                "2. Genere una rutina\n" +
                "3. Visualice el resumen y detalles\n\n" +
                "Seleccione una opción en el menú de la izquierda");
        
        JScrollPane scrollPane = new JScrollPane(areaInformacion);
        panelInfo.add(scrollPane, BorderLayout.CENTER);
        
        panelCentral.add(panelBotones);
        panelCentral.add(panelInfo);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        //panel inferior: estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createTitledBorder("Estado"));
        labelEstado = new JLabel("Sistema listo");
        labelEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstado.add(labelEstado, BorderLayout.WEST);
        
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    //boton con estilo
    private JButton crearBoton(String texto, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 12));
        boton.addActionListener(action);
        return boton;
    }

    //abrir ventana para cargar archivo
    private void abrirVentanaCargarArchivo() {
        VentanaCargarArchivo ventana = new VentanaCargarArchivo(this, gestor);
        ventana.setVisible(true);
        labelEstado.setText("Ventana de carga de archivo abierta");
    }

    //abrir ventana para generar rutina
    private void abrirVentanaGenerarRutina() {
        VentanaGenerarRutina ventana = new VentanaGenerarRutina(this, gestor);
        ventana.setVisible(true);
        labelEstado.setText("Ventana de generación de rutina abierta");
    }

    //abrir ventana para ver resumen de rutina
    private void abrirVentanaResumen() {
        Rutina rutina = gestor.getRutinaActual();
        if (rutina == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, genere una rutina primero", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            labelEstado.setText("Intento de acceso a resumen sin rutina");
            return;
        }
        
        VentanaResumenRutina ventana = new VentanaResumenRutina(this, gestor, rutina);
        ventana.setVisible(true);
        labelEstado.setText("Ventana de resumen de rutina abierta");
    }

    //abrir ventana para ver revisión de rutina
    private void abrirVentanaRevision() {
        Rutina rutina = gestor.getRutinaActual();
        if (rutina == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, genere una rutina primero", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            labelEstado.setText("Intento de acceso a revisión sin rutina");
            return;
        }
        
        VentanaRevisionRutina ventana = new VentanaRevisionRutina(this, gestor);
        ventana.setVisible(true);
        labelEstado.setText("Ventana de revisión de rutina abierta");
    }

    //muestra los ejercicios cargados
    private void mostrarEjercicios() {
        int cantidadEjercicios = gestor.getCantidadEjerciciosCargados();
        if (cantidadEjercicios == 0) {
            areaInformacion.setText("=== EJERCICIOS CARGADOS ===\n\n" +
                    "No hay ejercicios cargados.\n" +
                    "Seleccione 'Cargar Archivo CSV' para cargar los ejercicios\n");
            labelEstado.setText("Sin ejercicios cargados");
            return;
        }
        
        areaInformacion.setText("=== EJERCICIOS CARGADOS ===\n\n" +
                "Total de ejercicios: " + cantidadEjercicios + "\n\n" +
                "Puede generar una rutina con estos ejercicios");
        labelEstado.setText("Mostrando ejercicios cargados");
    }

    //limpia la aplicación y reinicia el gestor
    private void limpiarAplicacion() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Desea limpiar toda la información cargada?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            //crear nuevo gestor para limpiar todo
            gestor = new GestorAplicacion();
            gestor.suscribirse(this);
            
            areaInformacion.setText("Bienvenido al Sistema de Gestión de Rutinas\n\n" +
                    "1. Cargue un archivo CSV con ejercicios\n" +
                    "2. Genere una rutina\n" +
                    "3. Visualice el resumen y detalles\n\n" +
                    "Seleccione una opción en el menú de la izquierda");
            labelEstado.setText("Sistema limpiado");
        }
    }

    //implementacion del metodo onEvent para manejar eventos del gestor
    @Override
    public void onEvent(Event event) {
        switch (event.getTipo()) {
            case Event.ARCHIVO_CARGADO:
                areaInformacion.setText("✓ Archivo cargado exitosamente\n\n" +
                        "Ahora puede:\n" +
                        "- Generar una rutina\n" +
                        "- Ver los ejercicios cargados");
                labelEstado.setText("Archivo cargado");
                break;
                
            case Event.RUTINA_GENERADA:
                Rutina rutina = (Rutina) event.getDato();
                if (rutina != null) {
                    areaInformacion.setText("✓ Rutina generada exitosamente\n\n" +
                            "Cantidad de ejercicios: " + rutina.getCantidadTotal() + "\n" +
                            "Tiempo total: " + rutina.getTiempoTotal() + " minutos\n\n" +
                            "Tipos:\n" +
                            "- Cardiovasculares: " + rutina.contarPorTipo("Cardiovascular") + "\n" +
                            "- De Fuerza: " + rutina.contarPorTipo("Fuerza"));
                }
                labelEstado.setText("Rutina generada");
                break;
                
            case Event.ERROR_CARGA:
                String error = (String) event.getDato();
                areaInformacion.setText("Error: " + error);
                labelEstado.setText("Error: " + error);
                break;
        }
    }

    //ejecución de la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new main());
    }
}