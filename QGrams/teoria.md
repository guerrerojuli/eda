# Teoría de Q-Grams

## ¿Qué son los Q-Grams?

Los Q-Grams son substrings de longitud fija (q) que se extraen de un texto. Son una técnica comúnmente utilizada en procesamiento de texto y comparación de strings.

## Ejemplo Básico

Supongamos que tenemos el texto "HOLA" y queremos generar Q-Grams con q=2:

```
Texto: H O L A
Q-Grams (q=2): HO OL LA
```

Cada Q-Gram se genera deslizando una ventana de tamaño q sobre el texto.

## Aplicación en Comparación de Strings

Los Q-Grams son particularmente útiles para medir la similitud entre dos strings. Veamos un ejemplo:

### Ejemplo de Comparación

Consideremos dos palabras: "CASA" y "CAMA"

1. Generamos Q-Grams con q=2:

```
"CASA":
- CA
- AS
- SA

"CAMA":
- CA
- AM
- MA
```

2. Contamos las frecuencias:

```
Frecuencias para "CASA":
- CA: 1
- AS: 1
- SA: 1

Frecuencias para "CAMA":
- CA: 1
- AM: 1
- MA: 1
```

3. Calculamos la similitud:
- Q-Grams compartidos: CA
- Q-Grams únicos en "CASA": AS, SA
- Q-Grams únicos en "CAMA": AM, MA
- Total de Q-Grams: 6
- Q-Grams no compartidos: 4
- Similitud = (6 - 4) / 6 = 0.33

## Ventajas de los Q-Grams

1. **Robustez**: Son menos sensibles a pequeños cambios en el texto
2. **Eficiencia**: Fáciles de calcular y comparar
3. **Flexibilidad**: Se pueden ajustar según la longitud q

## Casos de Uso Comunes

1. **Corrección Ortográfica**: Identificar palabras similares
2. **Búsqueda de Texto**: Encontrar documentos similares
3. **Detección de Plagio**: Comparar similitudes entre textos
4. **Procesamiento de Lenguaje Natural**: Análisis de texto

## Implementación en el Código

La clase `QGram` implementa esta funcionalidad con los siguientes métodos principales:

- `getQGrams(String text)`: Genera los Q-Grams de un texto
- `printTokens(String text)`: Imprime los Q-Grams
- `similarity(String text1, String text2)`: Calcula la similitud entre dos textos

## Ejemplo de Uso del Código

```java
QGram qGram = new QGram(2);  // Crear instancia con q=2

// Generar y mostrar Q-Grams
qGram.printTokens("HOLA");  // Imprime: HO OL LA

// Calcular similitud
double similitud = qGram.similarity("CASA", "CAMA");
System.out.println("Similitud: " + similitud);  // Imprime: 0.33
```

## Consideraciones Importantes

1. **Elección de q**: 
   - q pequeño: Más sensible a cambios pequeños
   - q grande: Más robusto pero menos preciso

2. **Rendimiento**:
   - La complejidad aumenta con la longitud del texto
   - Se recomienda usar q pequeño para textos largos

3. **Casos Especiales**:
   - Textos vacíos o null
   - Textos más cortos que q
   - Caracteres especiales o espacios 