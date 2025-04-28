# Algoritmo Knuth-Morris-Pratt (KMP)

## Introducción
El algoritmo Knuth-Morris-Pratt (KMP) es un algoritmo eficiente para buscar patrones en textos. Fue desarrollado por Donald Knuth, Vaughan Pratt y James Morris en 1977. Su principal ventaja sobre el algoritmo de búsqueda de fuerza bruta es que evita comparaciones innecesarias, logrando una complejidad temporal de O(n + m), donde n es la longitud del texto y m es la longitud del patrón.

## Conceptos Clave

### Prefijo y Sufijo
- Un **prefijo** de una cadena es cualquier subcadena que comienza desde el primer carácter.
- Un **sufijo** de una cadena es cualquier subcadena que termina en el último carácter.
- Un **prefijo propio** es un prefijo que no es igual a la cadena completa.
- Un **sufijo propio** es un sufijo que no es igual a la cadena completa.

### Array LPS (Longest Prefix Suffix)
El array LPS es fundamental para el funcionamiento del algoritmo KMP. Para cada posición i en el patrón, LPS[i] almacena la longitud del prefijo más largo que también es sufijo en la subcadena patrón[0...i].

## Funcionamiento del Algoritmo

### Fase de Preprocesamiento
1. Se construye el array LPS para el patrón.
2. Para cada posición i en el patrón:
   - Si el carácter actual coincide con el carácter en la posición len, incrementamos len y guardamos el valor en LPS[i].
   - Si no coincide y len no es 0, retrocedemos len al valor anterior usando LPS[len-1].
   - Si len es 0, simplemente avanzamos al siguiente carácter.

### Fase de Búsqueda
1. Se comparan los caracteres del patrón con el texto.
2. Cuando hay una coincidencia, se avanzan ambos índices.
3. Cuando hay un desacuerdo:
   - Si el índice del patrón no es 0, se usa el array LPS para saltar caracteres.
   - Si el índice del patrón es 0, simplemente se avanza en el texto.
4. Si se encuentra el patrón completo, se registra la posición y se continúa la búsqueda.

## Ventajas del Algoritmo KMP
1. **Eficiencia**: Complejidad temporal O(n + m).
2. **Evita comparaciones innecesarias**: Utiliza información de comparaciones previas.
3. **No requiere retroceso en el texto**: Una vez que se lee un carácter del texto, no es necesario volver a leerlo.

## Ejemplo Práctico
Consideremos el patrón "ABABC" y el texto "ABABABC":
1. Preprocesamiento del patrón:
   - LPS = [0, 0, 1, 2, 0]
2. Búsqueda:
   - Se encuentra el patrón en la posición 2 del texto.

## Implementación
La implementación incluye:
- `nextComputation`: Preprocesa el patrón y construye el array LPS.
- `indexOf`: Encuentra la primera ocurrencia del patrón.
- `findAll`: Encuentra todas las ocurrencias del patrón.
- Métodos de conveniencia para arrays de caracteres y enteros.

## Aplicaciones
El algoritmo KMP es útil en:
- Búsqueda de texto en editores
- Procesamiento de ADN
- Sistemas de búsqueda
- Compresión de datos
- Detección de plagio 