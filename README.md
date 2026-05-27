# Gestor de Rutinas de Ejercicio

## Descripción
Aplicación Java Swing para gestionar ejercicios y generar rutinas. Separa la interfaz (`front`) de la lógica de negocio (`back`). Permite cargar ejercicios desde un archivo CSV, administrar ejercicios, y generar rutinas según criterios de tipo e intensidad.

## Alcance
- Cargar ejercicios desde CSV.
- Mantener un catálogo de ejercicios (crear, actualizar, eliminar, listar).
- Generar una rutina combinando ejercicios de tipo `Cardiovascular` y `Fuerza` respetando intensidades y evitando reutilizar ejercicios en la semana inmediatamente anterior.
- Interfaz gráfica básica (ventanas para cargar archivo, generar rutina, ver resumen y revisión).
- Sistema simple de eventos (`Event` / `EventListener`) para notificaciones UI ⇄ lógica.

## Supuestos
- La aplicación asume que los registros de ejercicios son correctos según el formato CSV descrito más abajo. El `LectorCSV` valida y lanza excepciones si hay problemas.
- Actualmente los tipos válidos son `Cardiovascular` y `Fuerza`.
- Intensidades reconocidas: `Básico`, `Intermedio`, `Avanzado`, `Alto rendimiento` (comparaciones case-insensitive).


## Estructura del proyecto
- `src/front/` — código de la interfaz Swing (ventanas y `main`).
- `src/back/` — lógica de dominio y utilidades (`Ejercicio`, `Rutina`, `GestorAplicacion`, `LectorCSV`, `Opciones`, excepciones, eventos).
- `ejercicios_prueba.csv` — ejemplo de archivo CSV.

## Requisitos
- JDK 11+ (se recomienda usar la versión con la que compilarás. El proyecto fue probado con JDK 17+).
- Herramienta de compilación opcional (IDE: IntelliJ/Eclipse/VS Code).

## Formato del archivo CSV de ejercicios
El CSV debe contener al menos 7 columnas en este orden:

1. `codigo` — identificador (por ejemplo `E001` o `123`). El lector intentará extraer números. Si no hay números, usa un hash.
2. `nombre` — nombre del ejercicio.
3. `tipo` — `Cardiovascular` o `Fuerza`.
4. `intensidad` — una de `Básico`, `Intermedio`, `Avanzado`, `Alto rendimiento`.
5. `tiempo_estimado` — entero positivo (minutos).
6. `descripcion` — texto descriptivo.
7. `ultima_semana` — entero (número de semana en la que se usó por última vez).

- El archivo puede incluir una fila de encabezado. `LectorCSV` saltará la primera línea si detecta la palabra `codigo`.
- Separador: coma `,`. Los campos no deben estar vacíos (el lector validará esto).

Ejemplo de línea válida:

```
E001,Sentadillas,Fuerza,Básico,15,Sentadillas con peso corporal,12
```

## Cómo cargar el archivo (UI)
1. Ejecuta la aplicación (ver sección "Ejecución").
2. En la ventana principal pulse `Cargar Archivo CSV`.
3. Seleccione el archivo CSV desde el diálogo de archivo. El sistema validará y mostrará errores si el archivo no existe o tiene formato inválido.

Errores comunes mostrados por la UI:
- `Archivo no existe` o `No se puede leer el archivo` → verifique la ruta y permisos.
- `Faltan datos obligatorios` → filas con menos de 7 columnas o campos vacíos.
- `Formato numérico inválido` → columnas `tiempo_estimado` o `ultima_semana` no son enteros.
- `Tipo de ejercicio desconocido` → `tipo` distinto a `Cardiovascular` / `Fuerza`.

Cuando el archivo se carga correctamente, la UI recibirá un evento `ARCHIVO_CARGADO` y actualizará la interfaz para permitir generar rutinas.

## Ejecución (línea de comandos)
Desde la raíz del proyecto, compile y ejecute con `javac`/`java` (suponiendo JDK instalado):

Windows PowerShell ejemplo:

```powershell
# Compilar
javac -d bin -sourcepath src src\back\*.java src\front\*.java

# Ejecutar
java -cp bin front.main
```

En IDEs como IntelliJ/Eclipse o VS Code (Java extension), importe el proyecto como proyecto Java y ejecute la clase `front.main`.

> Nota: La clase principal debe llamarse `front.main` si se mantiene ese nombre de clase.


## Manejo de errores y eventos
La comunicación entre la lógica y la UI usa `Event` y `EventListener`. Eventos relevantes:
- `ARCHIVO_CARGADO` — archivo cargado con éxito.
- `ERROR_CARGA` — error al cargar archivo (dato: mensaje de error).
- `RUTINA_GENERADA` — rutina generada con éxito (dato: `Rutina`).

# Francisco Molinet - Gonzalo Yuseff - Maximiliano Parra