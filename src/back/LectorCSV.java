package back;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public class LectorCSV {
 
    public static ArrayList<Ejercicio> cargarEjercicios(String rutaArchivo)
            throws ArchivoInexistenteException, InformacionIncompletaException, FormatoIncorrectoException, IOException {
 
        ArrayList<Ejercicio> listaEjercicios = new ArrayList<>();
 
        //validar que archivo exista y se pueda leer
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            throw new ArchivoInexistenteException("El archivo no existe: " + rutaArchivo);
        }
        if (!archivo.canRead()){
            throw new ArchivoInexistenteException("No se puede leer el archivo: " + rutaArchivo);
        }
 
        //abrir el archivo y leer línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;
 
            
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
 
                //saltar encabezado 
                if (numeroLinea == 1 && linea.contains("codigo")) {
                    continue;
                }
 
                //separador por comas
                String[] campos = linea.split(",");
 
                //valida que tiene los campos necesarios (codigo, nombre, tipo, intensidad, tiempo estimado, descripcion, semana)
                if (campos.length < 7) {
                    throw new InformacionIncompletaException(
                            "Error en línea " + numeroLinea + ": Faltan datos obligatorios."
                    );
                }
 
                //elimina espacios en blanco y valida que no haya campos vacíos
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].trim();
                    if (campos[i].isEmpty()) {
                        throw new InformacionIncompletaException(
                                "Error en línea " + numeroLinea + ": Hay campos vacíos."
                        );
                    }
                }
 
                //varaibles para crear ejercicio
                String codigo = campos[0];
                String nombre = campos[1];
                String tipo = campos[2];
                String intensidad = campos[3];
                String strTiempo = campos[4];
                String descripcion = campos[5];
                String strSemana = campos[6];
 
                //conversion varaibles
                int tiempoEstimado;
                int ultimaSemanaUsado;
 
                try {
                    tiempoEstimado = Integer.parseInt(campos[4]);
                    ultimaSemanaUsado = Integer.parseInt(campos[6]);
                } catch (NumberFormatException e) {
                    throw new FormatoIncorrectoException(
                            "Error en línea " + numeroLinea + ": El tiempo o la semana no tienen un formato numérico válido."
                    );
                }
 
                //validar que el tiempo estimado sea un número positivo
                if (tiempoEstimado <= 0){
                    throw new FormatoIncorrectoException(
                            "Error en línea " + numeroLinea + ": El tiempo debe ser positivo."
                    );
                }
 
                //intesidades permitidas
                String[] intensidadesValidas = {
                        "Básico", "Intermedio", "Avanzado", "Alto rendimiento"
                };
                
                //validar que la intensidad sea una de las permitidas (ignorando mayusculas y acentos)
                boolean intensidadValida = false;
                for (String valida : intensidadesValidas) {
                    if (valida.equalsIgnoreCase(intensidad)) {
                        intensidad = valida; //normalizar
                        intensidadValida = true;
                        break;
                    }
                }
 
                if (!intensidadValida){
                    throw new FormatoIncorrectoException(
                            "Error en línea " + numeroLinea + ": Intensidad inválida ('" + intensidad + "')."
                    );
                }
 
                //convertir código (E001 -> 1) (sugerencia cloude)
                int codigoInt;
                try {
                    String numeroStr = codigo.replaceAll("[^0-9]", ""); // Quita letras, deja números
                    if (numeroStr.isEmpty()) {
                        codigoInt = codigo.hashCode(); // Si no hay números, usar hash
                    } else {
                        codigoInt = Integer.parseInt(numeroStr);
                    }
                } catch (NumberFormatException e) {
                    codigoInt = codigo.hashCode();
                }
 
                Ejercicio ejercicio;
                if (tipo.equalsIgnoreCase("Cardiovascular")) {
                    ejercicio = new Cardiovascular(codigoInt, nombre, intensidad, tiempoEstimado, descripcion);
                } else if (tipo.equalsIgnoreCase("Fuerza")) {
                    ejercicio = new Fuerza(codigoInt, nombre, intensidad, tiempoEstimado, descripcion);
                } else {
                    throw new FormatoIncorrectoException(
                            "Error en línea " + numeroLinea + ": Tipo de ejercicio desconocido ('" + tipo + "')."
                    );
                }
 
                //setear ultima semana usado (para generación de rutina)
                ejercicio.setUltima_semana(ultimaSemanaUsado);
 
                listaEjercicios.add(ejercicio);
            }
        }
 
        return listaEjercicios;
    }
}
 