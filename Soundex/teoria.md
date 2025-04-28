# Algoritmo Soundex

## Introducción
Soundex es un algoritmo fonético desarrollado por Robert C. Russell y Margaret K. Odell en 1918. Es uno de los primeros algoritmos de codificación fonética y fue diseñado para indexar nombres por su sonido en inglés. El objetivo principal es generar códigos que representen cómo suenan las palabras, permitiendo la búsqueda de nombres que suenan similar pero se escriben diferente.

## Características Principales
1. **Longitud Fija**: Siempre genera códigos de 4 caracteres.
2. **Simplicidad**: Utiliza un conjunto básico de reglas para la codificación.
3. **Primera Letra**: Mantiene la primera letra de la palabra original.
4. **Dígitos**: Usa dígitos del 0 al 6 para representar grupos de sonidos similares.

## Reglas de Codificación
El algoritmo Soundex asigna valores numéricos a las letras de la siguiente manera:

- 0: A, E, I, O, U, H, W, Y (sonidos que se ignoran)
- 1: B, F, P, V
- 2: C, G, J, K, Q, S, X, Z
- 3: D, T
- 4: L
- 5: M, N
- 6: R

## Pasos del Algoritmo
1. **Primera Letra**: Mantener la primera letra de la palabra.
2. **Eliminar Vocales**: Ignorar todas las vocales y las letras H, W, Y.
3. **Codificar**: Convertir las letras restantes a números según la tabla anterior.
4. **Eliminar Duplicados**: Si hay números consecutivos iguales, mantener solo uno.
5. **Rellenar**: Si el código resultante tiene menos de 4 caracteres, rellenar con ceros.

## Ejemplo Práctico
Tomemos la palabra "ROBERT" como ejemplo:

1. Primera letra: R
2. Codificar letras restantes:
   - O → ignorar
   - B → 1
   - E → ignorar
   - R → 6
   - T → 3
3. Eliminar duplicados: No hay duplicados en este caso
4. Rellenar: R163

Otro ejemplo con "RUPERT":
1. Primera letra: R
2. Codificar letras restantes:
   - U → ignorar
   - P → 1
   - E → ignorar
   - R → 6
   - T → 3
3. Eliminar duplicados: No hay duplicados
4. Rellenar: R163

Nota: Tanto "ROBERT" como "RUPERT" generan el mismo código Soundex "R163".

## Aplicaciones
El algoritmo Soundex es útil en:
- Búsqueda de nombres en bases de datos
- Genealogía
- Sistemas de corrección de errores ortográficos
- Aplicaciones de reconocimiento de voz
- Sistemas de recomendación

## Limitaciones
- Diseñado principalmente para inglés
- No maneja bien todos los acentos y caracteres especiales
- Puede generar falsos positivos
- No es tan preciso como algoritmos más modernos (como Metaphone)

## Comparación con Metaphone
Soundex es más simple pero menos preciso que Metaphone porque:
- Usa menos reglas de transformación
- No maneja bien las consonantes silenciosas
- Es menos efectivo con palabras modernas
- Genera códigos de longitud fija 