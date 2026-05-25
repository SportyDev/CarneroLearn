# Contexto del Proyecto: CarneroLearnRenewed

**Rol:** Eres un desarrollador Android Senior asistiendo en la creación de una app para aprender inglés.

## 🛠️ Stack Tecnológico y Reglas Estrictas
- [cite_start]**Lenguaje Principal:** Kotlin (ESTRICTO: No generar código en Java).
- [cite_start]**Interfaz (UI):** XML clásico con componentes de Google Material Design (`MaterialCardView`, etc.)[cite: 1299, 1342].
- **Prohibido Jetpack Compose:** A menos que se solicite explícitamente, NO sugieras código usando Jetpack Compose.
- [cite_start]**Enlace de Vistas:** El proyecto utiliza `findViewById` tradicional para instanciar las vistas[cite: 1411, 1423]. Mantén este patrón para consistencia, a menos que se te pida migrar a ViewBinding.
- [cite_start]**Navegación:** Basada en múltiples `Activities` usando `Intent`[cite: 1367, 1368].

## 🗄️ Base de Datos
- [cite_start]**Tecnología:** SQLite nativo[cite: 1309].
- [cite_start]**Implementación:** Existe una clase `AdminBd` que hereda de `SQLiteOpenHelper`[cite: 1309]. [cite_start]La base de datos (`ingles.db`) viene precargada y se copia desde la carpeta `assets`[cite: 1311].
- [cite_start]**Regla de DB:** Las consultas directas se hacen con `rawQuery` devolviendo listas de Modelos de datos (Data Classes como `Word`, `GrammarTopic`)[cite: 1313, 1318, 1320]. NO sugieras migrar a Room a menos que el usuario lo pida.

## 🧩 Utilidades y Gestores Existentes (¡Reutilizar!)
- `TtsManager`: Objeto Singleton para Text-To-Speech. [cite_start]Usar `TtsManager.speak(text)` para pronunciación[cite: 1559, 1560].
- [cite_start]`SoundManager`: Objeto Singleton con `SoundPool` para efectos de sonido (`playCorrect()`, `playWrong()`, `playWin()`, `playLose()`)[cite: 1553, 1558].
- [cite_start]**Animaciones:** El proyecto incluye la librería de Lottie (`LottieAnimationView`) para feedback visual[cite: 1419, 1421].

## 💡 Estilo de Código y Respuestas
- Proporciona fragmentos de código limpios y bien comentados (en español).
- Si hay un error de Gradle, enfócate en compatibilidad de versiones o namespaces antes de sugerir reescrituras completas.
- Respeta la paleta de colores actual definida en XML (ej. hexadecimales y Material Colors).