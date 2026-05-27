package front;

import back.*;
import javax.swing.*;
import java.awt.*;

//ventana para revisar cada ejercicio de la rutina con detalles y opciones para navegar entre ejercicios o ver resumen
public class VentanaRevisionRutina extends JDialog {
    private GestorAplicacion gestor;
    private Rutina rutinaActual;
    private int indiceActual;
    private JLabel labelNombre;
    private JLabel labelTipo;
    private JLabel labelIntensidad;
    private JLabel labelTiempo;
    private JTextArea areaDescripcion;
    private JButton botonAnterior;
    private JButton botonSiguiente;
    private JLabel labelProgreso;

    public VentanaRevisionRutina(Frame parent, GestorAplicacion gestor) {
        super(parent, "Revisión de Rutina", true);
        this.gestor = gestor;
        this.rutinaActual = gestor.getRutinaActual();
        this.indiceActual = 0;

        setSize(700, 550);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        mostrarEjercicio(0);
    }

    //componentes
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //titulo
        JLabel titulo = new JLabel("Revisión de Rutina", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        //info ejercicio
        JPanel panelEjercicio = new JPanel(new GridBagLayout());
        panelEjercicio.setBorder(BorderFactory.createTitledBorder("Ejercicio Actual"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        //nombre
        JLabel labelNombreTitle = new JLabel("Nombre:");
        labelNombreTitle.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panelEjercicio.add(labelNombreTitle, gbc);

        labelNombre = new JLabel();
        labelNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelEjercicio.add(labelNombre, gbc);

        //tipo
        JLabel labelTipoTitle = new JLabel("Tipo:");
        labelTipoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panelEjercicio.add(labelTipoTitle, gbc);

        labelTipo = new JLabel();
        labelTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelEjercicio.add(labelTipo, gbc);

        //intensidad
        JLabel labelIntensidadTitle = new JLabel("Nivel de Intensidad:");
        labelIntensidadTitle.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panelEjercicio.add(labelIntensidadTitle, gbc);

        labelIntensidad = new JLabel();
        labelIntensidad.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelEjercicio.add(labelIntensidad, gbc);

        //tiempo
        JLabel labelTiempoTitle = new JLabel("Tiempo Estimado:");
        labelTiempoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panelEjercicio.add(labelTiempoTitle, gbc);

        labelTiempo = new JLabel();
        labelTiempo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelEjercicio.add(labelTiempo, gbc);

        //descripcion
        JLabel labelDescripcionTitle = new JLabel("Descripción de Ejecución:");
        labelDescripcionTitle.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        panelEjercicio.add(labelDescripcionTitle, gbc);

        areaDescripcion = new JTextArea(5, 40);
        areaDescripcion.setEditable(false);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setFont(new Font("Arial", Font.PLAIN, 11));
        JScrollPane scrollDescripcion = new JScrollPane(areaDescripcion);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelEjercicio.add(scrollDescripcion, gbc);

        panel.add(panelEjercicio, BorderLayout.CENTER);

        //panel nav y botones
        JPanel panelNavegacion = new JPanel(new BorderLayout(10, 10));

        //progreso
        labelProgreso = new JLabel();
        labelProgreso.setFont(new Font("Arial", Font.PLAIN, 11));
        panelNavegacion.add(labelProgreso, BorderLayout.WEST);

        //botones anterior y siguiente
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        botonAnterior = new JButton("← Anterior");
        botonAnterior.addActionListener(e -> mostrarEjercicioAnterior());
        panelBotones.add(botonAnterior);

        botonSiguiente = new JButton("Siguiente →");
        botonSiguiente.addActionListener(e -> mostrarEjercicioSiguiente());
        panelBotones.add(botonSiguiente);

        panelNavegacion.add(panelBotones, BorderLayout.CENTER);

        //boton resumen
        JButton botonResumen = new JButton("Resumen de la Rutina");
        botonResumen.addActionListener(e -> mostrarResumen());
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelResumen.add(botonResumen);
        panelNavegacion.add(panelResumen, BorderLayout.EAST);

        panel.add(panelNavegacion, BorderLayout.SOUTH);

        add(panel);
    }

    //muestra el ejercicio actual segun indice
    private void mostrarEjercicio(int indice) {
        if (rutinaActual != null && indice >= 0 && indice < rutinaActual.getCantidadTotal()) {
            indiceActual = indice;
            Ejercicio ejercicio = rutinaActual.getEjercicio(indice);

            if (ejercicio != null) {
                labelNombre.setText(ejercicio.getNombre());
                labelTipo.setText(ejercicio.getTipo());
                labelIntensidad.setText(ejercicio.getNivelIntensidad());
                labelTiempo.setText(ejercicio.getTiempoMinutos() + " minutos");
                areaDescripcion.setText(ejercicio.getDescripcion());

                //actualizar estado boton anterior (delegado al backend)
                botonAnterior.setEnabled(rutinaActual.tieneEjercicioAnterior(indiceActual));
                
                //actualizar boton siguiente - cambiar texto si es ultimo ejercicio (delegado al backend)
                if (rutinaActual.esUltimoEjercicio(indiceActual)) {
                    botonSiguiente.setText("Resumen de la Rutina");
                } else {
                    botonSiguiente.setText("Siguiente →");
                }

                //actualizar progreso
                labelProgreso.setText(String.format("Ejercicio %d de %d",
                        indiceActual + 1, rutinaActual.getCantidadTotal()));

                //scroll (no necesario pero mejora experiencia)
                areaDescripcion.setCaretPosition(0);
            }
        }
    }

    //mostrar anterior
    private void mostrarEjercicioAnterior() {
        if (indiceActual > 0) {
            mostrarEjercicio(indiceActual - 1);
        }
    }

    //mostrar siguiente o resumen
    private void mostrarEjercicioSiguiente() {
        if (rutinaActual.tieneEjercicioSiguiente(indiceActual)) {
            mostrarEjercicio(indiceActual + 1);
        } else {
            mostrarResumen();
        }
    }

    //mostrar resumen rutina
    private void mostrarResumen() {
        VentanaResumenRutina ventanaResumen = new VentanaResumenRutina(
                (Frame) SwingUtilities.getWindowAncestor(this), gestor, rutinaActual);
        ventanaResumen.setVisible(true);
        dispose();
    }
}