# Algoritmo Metaphone

## Introducción
Metaphone es un algoritmo fonético que convierte palabras en códigos fonéticos, similar al algoritmo Soundex pero más sofisticado. Fue desarrollado por Lawrence Philips en 1990 como una mejora del algoritmo Soundex original. El objetivo principal es generar códigos que representen cómo suenan las palabras en inglés, permitiendo la búsqueda de palabras que suenan similar pero se escriben diferente.

## Características Principales
1. **Longitud Variable**: A diferencia de Soundex que siempre genera códigos de 4 caracteres, Metaphone puede generar códigos de longitud variable.
2. **Reglas Complejas**: Utiliza un conjunto más sofisticado de reglas para manejar diferentes combinaciones de letras y sonidos.
3. **Mejor Precisión**: Proporciona mejores resultados que Soundex para palabras en inglés moderno.

## Reglas Principales
El algoritmo Metaphone aplica las siguientes reglas principales:

1. **Vocales**: Solo se mantienen las vocales al inicio de la palabra.
2. **Consonantes Silenciosas**: Se ignoran ciertas combinaciones iniciales como KN, GN, PN, AE, WR, WH.
3. **Transformaciones Específicas**:
   - C → S antes de E, I, Y
   - C → K en otros casos
   - D → J antes de GE, GY, GI
   - G → J antes de E, I, Y
   - PH → F
   - S → X antes de H, IO, IA
   - T → 0 antes de H
   - V → F
   - X → KS
   - Z → S

## Ejemplo Práctico
Tomemos la palabra "KNIGHT" como ejemplo:

1. Se ignora la K inicial (regla de KN)
2. N se mantiene
3. I se ignora (no es vocal inicial)
4. G se mantiene
5. H se ignora
6. T se mantiene

El código Metaphone resultante sería: "NFT"

Otro ejemplo con "SMITH":
1. S se mantiene
2. M se mantiene
3. I se ignora
4. T se mantiene
5. H se ignora

El código Metaphone resultante sería: "SMT"

## Aplicaciones
El algoritmo Metaphone es útil en:
- Búsqueda de nombres
- Corrección de errores ortográficos
- Sistemas de recomendación
- Procesamiento de lenguaje natural
- Bases de datos para búsqueda fonética

## Limitaciones
- Diseñado principalmente para inglés
- Puede generar falsos positivos
- No maneja bien todos los acentos y caracteres especiales
- El rendimiento puede variar según el dialecto del inglés

## Comparación con Soundex
Metaphone es generalmente considerado superior a Soundex porque:
- Maneja mejor las consonantes silenciosas
- Tiene reglas más específicas para diferentes combinaciones de letras
- Proporciona códigos más precisos
- Es más efectivo con palabras modernas en inglés 