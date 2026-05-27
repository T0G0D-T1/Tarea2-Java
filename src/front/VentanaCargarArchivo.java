package front;

import back.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;


public class VentanaCargarArchivo extends JDialog implements EventListener {
    private GestorAplicacion gestor;
    private JLabel labelArchivo;
    private JButton botonExaminar;
    private JButton botonCargar;
    private JButton botonCancelar;
    private File archivoSeleccionado;

    public VentanaCargarArchivo(Frame parent, GestorAplicacion gestor) {
        super(parent, "Cargar Archivo de Ejercicios", true);
        this.gestor = gestor;
        this.gestor.suscribirse(this);

        setSize(500, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

   
    //componentes de la ventana
    
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titulo
        JLabel titulo = new JLabel("Seleccionar archivo CSV", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel labelInstruccion = new JLabel("Seleccione el archivo CSV con los ejercicios:");
        labelInstruccion.setFont(new Font("Arial", Font.PLAIN, 11));
        panelCentral.add(labelInstruccion, gbc);

        // Panel para archivo
        JPanel panelArchivo = new JPanel(new BorderLayout(10, 0));
        labelArchivo = new JLabel("Ningún archivo seleccionado");
        labelArchivo.setFont(new Font("Arial", Font.ITALIC, 11));
        labelArchivo.setForeground(Color.GRAY);
        panelArchivo.add(labelArchivo, BorderLayout.CENTER);

        botonExaminar = new JButton("Examinar...");
        botonExaminar.addActionListener(e -> abrirSelectorArchivo());
        panelArchivo.add(botonExaminar, BorderLayout.EAST);

        panelCentral.add(panelArchivo, gbc);

        panel.add(panelCentral, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        botonCargar = new JButton("Cargar");
        botonCargar.setEnabled(false);
        botonCargar.addActionListener(e -> cargarArchivo());
        panelBotones.add(botonCargar);

        botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        panelBotones.add(botonCancelar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    //selector archivo
    private void abrirSelectorArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos CSV (*.csv)", "csv"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            labelArchivo.setText(archivoSeleccionado.getName());
            labelArchivo.setForeground(Color.BLACK);
            botonCargar.setEnabled(true);
        }
    }

    //carga archivo
    private void cargarArchivo() {
        if (archivoSeleccionado != null) {
            botonCargar.setEnabled(false);
            botonExaminar.setEnabled(false);
            botonCargar.setText("Cargando...");

            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    gestor.cargarEjerciciosDesdeArchivo(archivoSeleccionado.getAbsolutePath());
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    } catch (Exception ex) {
                        gestor.notificarError("Error al cargar archivo: " + ex.getMessage());
                    }
                }
            };

            worker.execute();
        }
    }

    @Override
    public void onEvent(back.Event event) {
        if (event.getTipo().equals(back.Event.ARCHIVO_CARGADO)) {
            JOptionPane.showMessageDialog(this, "Archivo cargado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else if (event.getTipo().equals(back.Event.ERROR_CARGA)) {
            String mensaje = (String) event.getDato();
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            botonCargar.setEnabled(true);
            botonExaminar.setEnabled(true);
            botonCargar.setText("Cargar");
        }
    }
}