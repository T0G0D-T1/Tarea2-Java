package front;

import back.*;
import javax.swing.*;
import java.awt.*;

//ventana resumen rutina con estadisticas y opciones para guardar o revisar rutina
public class VentanaResumenRutina extends JDialog {
    private GestorAplicacion gestor;
    private Rutina rutina;

    public VentanaResumenRutina(Frame parent, GestorAplicacion gestor, Rutina rutina) {
        super(parent, "Resumen de Rutina", true);
        this.gestor = gestor;
        this.rutina = rutina;

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    //componentes
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //titulo
        JLabel titulo = new JLabel("Resumen de la Rutina", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        //panel central
        JPanel panelResumen = new JPanel(new GridBagLayout());
        panelResumen.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridwidth = 2;

        //cantidad total
        gbc.gridy = 0;
        JPanel panelCantidad = crearPanelEstadistica("Cantidad Total de Ejercicios",
                String.valueOf(rutina.getCantidadTotal()));
        panelResumen.add(panelCantidad, gbc);

        //tiempo total
        gbc.gridy = 1;
        JPanel panelTiempo = crearPanelEstadistica("Tiempo Total de la Rutina",
                rutina.getTiempoTotal() + " minutos");
        panelResumen.add(panelTiempo, gbc);

        //separador
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelResumen.add(new JSeparator(), gbc);

        //ejercicios por tipo
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 20, 5, 20);
        JLabel labelTipos = new JLabel("Ejercicios por Tipo:", JLabel.LEFT);
        labelTipos.setFont(new Font("Arial", Font.BOLD, 12));
        panelResumen.add(labelTipos, gbc);

        int cardio = rutina.contarPorTipo("Cardiovascular");
        int fuerza = rutina.contarPorTipo("Fuerza");

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 40, 5, 20);
        gbc.gridwidth = 1;
        JLabel labelCardio = new JLabel("Cardiovasculares:", JLabel.LEFT);
        gbc.gridx = 0;
        panelResumen.add(labelCardio, gbc);
        JLabel valorCardio = new JLabel(String.valueOf(cardio), JLabel.RIGHT);
        valorCardio.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        panelResumen.add(valorCardio, gbc);

        gbc.gridy = 5;
        JLabel labelFuerza = new JLabel("De Fuerza:", JLabel.LEFT);
        gbc.gridx = 0;
        panelResumen.add(labelFuerza, gbc);
        JLabel valorFuerza = new JLabel(String.valueOf(fuerza), JLabel.RIGHT);
        valorFuerza.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        panelResumen.add(valorFuerza, gbc);

        //separador
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panelResumen.add(new JSeparator(), gbc);

        //ejercicio por nivel de intesidad
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 20, 5, 20);
        JLabel labelIntensidades = new JLabel("Ejercicios por Nivel de Intensidad:", JLabel.LEFT);
        labelIntensidades.setFont(new Font("Arial", Font.BOLD, 12));
        panelResumen.add(labelIntensidades, gbc);

        String[] niveles = {"Básico", "Intermedio", "Avanzado", "Alto rendimiento"};
        int[] conteos = new int[4];
        for (int i = 0; i < niveles.length; i++) {
            conteos[i] = rutina.contarPorIntensidad(niveles[i]);
        }

        int gridy = 8;
        gbc.insets = new Insets(5, 40, 5, 20);
        gbc.gridwidth = 1;
        for (int i = 0; i < niveles.length; i++) {
            gbc.gridy = gridy + i;
            JLabel label = new JLabel(niveles[i] + ":", JLabel.LEFT);
            gbc.gridx = 0;
            panelResumen.add(label, gbc);

            JLabel valor = new JLabel(String.valueOf(conteos[i]), JLabel.RIGHT);
            valor.setFont(new Font("Arial", Font.BOLD, 12));
            gbc.gridx = 1;
            panelResumen.add(valor, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(panelResumen);
        panel.add(scrollPane, BorderLayout.CENTER);

        //botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton botonGuardar = new JButton("Guardar Rutina");
        botonGuardar.addActionListener(e -> guardarRutina());
        panelBotones.add(botonGuardar);

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.addActionListener(e -> dispose());
        panelBotones.add(botonCerrar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    //panel con diseño para estadisticas
    private JPanel crearPanelEstadistica(String titulo, String valor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(labelTitulo, BorderLayout.WEST);

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Arial", Font.BOLD, 16));
        labelValor.setForeground(new Color(0, 100, 200));
        labelValor.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        labelValor.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(labelValor, BorderLayout.EAST);

        return panel;
    }

    //boton guardar rutina (simula guardado y muestra mensaje)
    private void guardarRutina() {
        JOptionPane.showMessageDialog(this,
                "Rutina guardada exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}