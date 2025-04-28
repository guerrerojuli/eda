# Teoría del Evaluador de Expresiones

## Descripción General

El evaluador implementa un sistema para procesar y evaluar expresiones matemáticas en notación infija (la notación común que usamos) y postfija (también conocida como notación polaca inversa). El proceso consta de dos pasos principales:

1. Conversión de infijo a postfijo
2. Evaluación de la expresión postfija

## Proceso de Evaluación

### 1. Conversión de Infijo a Postfijo

El método `convertInfixToPostfix` utiliza el algoritmo de Shunting Yard para convertir expresiones infijas a postfijas. El proceso sigue estas reglas:

- Los números se añaden directamente a la salida
- Los operadores se manejan según su precedencia:
  - `^` (potenciación): precedencia 3
  - `*`, `/`: precedencia 2
  - `+`, `-`: precedencia 1
- Los paréntesis se manejan de forma especial:
  - `(` se apila
  - `)` desapila operadores hasta encontrar el `(` correspondiente

### 2. Evaluación de Postfijo

El método `evaluatePostfix` utiliza una pila para evaluar la expresión postfija:
- Los números se apilan
- Cuando se encuentra un operador:
  - Se desapilan dos operandos
  - Se aplica la operación
  - El resultado se vuelve a apilar

## Ejemplo de Seguimiento de Stack

### Ejemplo 1: Expresión Infija a Postfija (Detallado)

Expresión: `3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3`

| Token | Stack | Salida | Explicación |
|-------|-------|--------|-------------|
| 3     | []    | 3      | Número: se añade directamente a la salida |
| +     | [+]   | 3      | Operador: se apila porque el stack está vacío |
| 4     | [+]   | 3 4    | Número: se añade directamente a la salida |
| *     | [+ *] | 3 4    | Operador: se apila porque * tiene mayor precedencia que + |
| 2     | [+ *] | 3 4 2  | Número: se añade directamente a la salida |
| /     | [+ /] | 3 4 2 * | Operador: / tiene igual precedencia que *, se desapila * y se apila / |
| (     | [+ / (] | 3 4 2 * | Paréntesis izquierdo: se apila siempre |
| 1     | [+ / (] | 3 4 2 * 1 | Número: se añade directamente a la salida |
| -     | [+ / ( -] | 3 4 2 * 1 | Operador: se apila porque está dentro de paréntesis |
| 5     | [+ / ( -] | 3 4 2 * 1 5 | Número: se añade directamente a la salida |
| )     | [+ /] | 3 4 2 * 1 5 - | Paréntesis derecho: se desapila hasta encontrar ( |
| ^     | [+ / ^] | 3 4 2 * 1 5 - | Operador: ^ tiene mayor precedencia que /, se apila |
| 2     | [+ / ^] | 3 4 2 * 1 5 - 2 | Número: se añade directamente a la salida |
| ^     | [+ / ^ ^] | 3 4 2 * 1 5 - 2 | Operador: ^ es asociativo por derecha, se apila |
| 3     | [+ / ^ ^] | 3 4 2 * 1 5 - 2 3 | Número: se añade directamente a la salida |
| EOF   | []    | 3 4 2 * 1 5 - 2 3 ^ ^ / + | Fin: se desapilan todos los operadores |

Expresión postfija final: `3 4 2 * 1 5 - 2 3 ^ ^ / +`

### Ejemplo 2: Evaluación de Postfijo (Detallado)

Expresión postfija: `3 4 2 * 1 5 - 2 3 ^ ^ / +`

| Token | Stack | Operación | Explicación |
|-------|-------|-----------|-------------|
| 3     | [3]   | -         | Número: se apila |
| 4     | [3, 4]| -         | Número: se apila |
| 2     | [3, 4, 2] | -     | Número: se apila |
| *     | [3, 8] | 4 * 2 = 8 | Operador: se desapilan 2 y 4, se multiplican, resultado se apila |
| 1     | [3, 8, 1] | -     | Número: se apila |
| 5     | [3, 8, 1, 5] | -     | Número: se apila |
| -     | [3, 8, -4] | 1 - 5 = -4 | Operador: se desapilan 5 y 1, se restan, resultado se apila |
| 2     | [3, 8, -4, 2] | -     | Número: se apila |
| 3     | [3, 8, -4, 2, 3] | - | Número: se apila |
| ^     | [3, 8, -4, 8] | 2 ^ 3 = 8 | Operador: se desapilan 3 y 2, se eleva 2 a la 3, resultado se apila |
| ^     | [3, 8, 65536] | (-4) ^ 8 = 65536 | Operador: se desapilan 8 y -4, se eleva -4 a la 8, resultado se apila |
| /     | [3, 0.000122] | 8 / 65536 ≈ 0.000122 | Operador: se desapilan 65536 y 8, se divide 8 entre 65536, resultado se apila |
| +     | [3.000122] | 3 + 0.000122 ≈ 3.000122 | Operador: se desapilan 0.000122 y 3, se suman, resultado se apila |

Resultado final: ≈ 3.000122

## Consideraciones Importantes

1. **Precedencia de Operadores**:
   - La potenciación (`^`) tiene la mayor precedencia
   - La multiplicación y división tienen precedencia media
   - La suma y resta tienen la menor precedencia

2. **Manejo de Errores**:
   - División por cero
   - Expresiones inválidas
   - Paréntesis desbalanceados
   - Operadores desconocidos

3. **Asociatividad**:
   - La potenciación es asociativa por la derecha
   - Los demás operadores son asociativos por la izquierda 