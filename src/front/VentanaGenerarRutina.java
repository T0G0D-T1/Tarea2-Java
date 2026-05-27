package front;

import back.*;
import javax.swing.*;
import java.awt.*;

//parametros para genera rutina personalizada junto con el nivel de exigencia de cada ejercicio.
public class VentanaGenerarRutina extends JDialog implements EventListener {
    private GestorAplicacion gestor;
    private JSpinner spinnerCardio;
    private JSpinner spinnerFuerza;
    private JComboBox<String> comboBasico;
    private JComboBox<String> comboIntermedio;
    private JComboBox<String> comboAvanzado;
    private JComboBox<String> comboAltoRendimiento;
    private JButton botonGenerar;
    private JButton botonCancelar;

    public VentanaGenerarRutina(Frame parent, GestorAplicacion gestor) {
        super(parent, "Generar Rutina", true);
        this.gestor = gestor;
        this.gestor.suscribirse(this);

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    //componentes ventana
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // titulo
        JLabel titulo = new JLabel("Configurar Nueva Rutina", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        // panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // cantidad por tipo de ejercicio
        JLabel seccion1 = new JLabel("Cantidad de ejercicios por tipo");
        seccion1.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panelCentral.add(seccion1, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        JLabel labelCardio = new JLabel("Ejercicios Cardiovasculares:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCentral.add(labelCardio, gbc);

        spinnerCardio = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        gbc.gridx = 1;
        panelCentral.add(spinnerCardio, gbc);

        JLabel labelFuerza = new JLabel("Ejercicios de Fuerza:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCentral.add(labelFuerza, gbc);

        spinnerFuerza = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        gbc.gridx = 1;
        panelCentral.add(spinnerFuerza, gbc);

        // separador
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 3;
        JSeparator separator = new JSeparator();
        panelCentral.add(separator, gbc);

        // niveles de intensidad
        JLabel seccion2 = new JLabel("Nivel de intensidad para cada ejercicio");
        seccion2.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 4;
        panelCentral.add(seccion2, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;

        JLabel labelNivelBasico = new JLabel("Ejercicios Básicos - Cantidad:");
        gbc.gridx = 0;
        panelCentral.add(labelNivelBasico, gbc);

        comboBasico = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        panelCentral.add(comboBasico, gbc);

        JLabel labelNivelIntermedio = new JLabel("Ejercicios Intermedio - Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panelCentral.add(labelNivelIntermedio, gbc);

        comboIntermedio = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        panelCentral.add(comboIntermedio, gbc);

        JLabel labelNivelAvanzado = new JLabel("Ejercicios Avanzado - Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panelCentral.add(labelNivelAvanzado, gbc);

        comboAvanzado = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        panelCentral.add(comboAvanzado, gbc);

        JLabel labelNivelAltoRendimiento = new JLabel("Ejercicios Alto Rendimiento - Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panelCentral.add(labelNivelAltoRendimiento, gbc);

        comboAltoRendimiento = new JComboBox<>(new String[]{"0", "1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        panelCentral.add(comboAltoRendimiento, gbc);

        JScrollPane scrollPane = new JScrollPane(panelCentral);
        panel.add(scrollPane, BorderLayout.CENTER);

        // botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        botonGenerar = new JButton("Generar Rutina");
        botonGenerar.addActionListener(e -> generarRutina());
        panelBotones.add(botonGenerar);

        botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        panelBotones.add(botonCancelar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    //generar rutina con los parametros seleccionados
    private void generarRutina() {
        try {
            int cantCardio = (Integer) spinnerCardio.getValue();
            int cantFuerza = (Integer) spinnerFuerza.getValue();

            //contar ejercicios por intensidad
            int[] cantidadPorIntensidad = new int[4];
            cantidadPorIntensidad[0] = Integer.parseInt((String) comboBasico.getSelectedItem());
            cantidadPorIntensidad[1] = Integer.parseInt((String) comboIntermedio.getSelectedItem());
            cantidadPorIntensidad[2] = Integer.parseInt((String) comboAvanzado.getSelectedItem());
            cantidadPorIntensidad[3] = Integer.parseInt((String) comboAltoRendimiento.getSelectedItem());

            //validar parametros en el back
            if (!gestor.validarParametrosRutina(cantCardio, cantFuerza, cantidadPorIntensidad)) {
                return;
            }

            botonGenerar.setEnabled(false);
            botonGenerar.setText("Generando...");

            // Ejecutar en hilo separado (sugerencia cloude) para no bloquear UI
            SwingWorker<Rutina, Void> worker = new SwingWorker<Rutina, Void>() {
                @Override
                protected Rutina doInBackground() throws Exception {
                    return gestor.generarRutina(cantCardio, cantFuerza, cantidadPorIntensidad);
                }

                @Override
                protected void done() {
                    try {
                        Rutina rutina = get();
                        if (rutina != null) {
                            gestor.setRutinaActual(rutina);
                            gestor.notificarRutinaGenerada(rutina);
                        }
                    } catch (Exception ex) {
                        gestor.notificarError("Error al generar rutina: " + ex.getMessage());
                    }
                }
            };

            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Verifique que los valores ingresados sean números válidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onEvent(back.Event event) {
        if (event.getTipo().equals(back.Event.RUTINA_GENERADA)) {
            JOptionPane.showMessageDialog(this,
                    "Rutina generada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

            // mostrar revision
            Window parent = SwingUtilities.getWindowAncestor(this);
            if (parent instanceof Frame) {
                VentanaRevisionRutina ventanaRevision = new VentanaRevisionRutina((Frame) parent, gestor);
                ventanaRevision.setVisible(true);
            }
        } else if (event.getTipo().equals(back.Event.ERROR_GENERACION)) {
            String mensaje = (String) event.getDato();
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            botonGenerar.setEnabled(true);
            botonGenerar.setText("Generar Rutina");
        }
    }
}