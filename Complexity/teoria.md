# Teorema Maestro para el Cálculo de Complejidad Recursiva

El **teorema maestro** es una herramienta fundamental para analizar la complejidad temporal y espacial de algoritmos recursivos que pueden expresarse mediante recurrencias de la forma:

    T(N) = a * T(N/b) + c * N^d

donde:
- **a**: número de subproblemas en los que se divide el problema original.
- **b**: factor por el que se reduce el tamaño del problema en cada llamada recursiva.
- **c**: constante que multiplica el término de trabajo fuera de la recursión (no afecta la O grande).
- **d**: exponente del término de trabajo fuera de la recursión.

## Casos del Teorema Maestro

La complejidad asintótica (O grande) está dada por los siguientes 3 casos (la constante c no cuenta):

1. **Si a < b^d:**
   - T(N) = O(N^d)
2. **Si a = b^d:**
   - T(N) = O(N^d * log N)
3. **Si a > b^d:**
   - T(N) = O(N^(log_b a))

> **Nota:** El término constante (como el +4 en el ejemplo de Fibonacci) no afecta la complejidad asintótica.

## Ejemplo: Fibonacci Recursivo

La recurrencia para el algoritmo recursivo de Fibonacci es:

    T(N) = T(N-1) + T(N-2) + O(1)

Esta recurrencia **no** se ajusta exactamente a la forma del teorema maestro, pero si tuviéramos una recurrencia del tipo:

    T(N) = 2 * T(N/2) + c * N

Podríamos aplicar el teorema maestro con:
- a = 2
- b = 2
- d = 1 (porque el trabajo fuera de la recursión es lineal)

Entonces:
- a = b^d implica 2 = 2^1, por lo que **caso 2** aplica:
- T(N) = O(N * log N)

## Ejemplo: Búsqueda Binaria

En búsqueda binaria:
- a = 1 (una sola llamada recursiva)
- b = 2 (el tamaño se reduce a la mitad)
- d = 0 (el trabajo fuera de la recursión es constante)

    T(N) = T(N/2) + c

a = b^d implica 1 = 2^0, por lo que **caso 2** aplica:
- T(N) = O(log N)

## Complejidad Espacial

El teorema maestro también puede ayudar a estimar la complejidad espacial si el espacio usado sigue una recurrencia similar. Por ejemplo, si cada llamada recursiva crea subproblemas independientes, la complejidad espacial puede analizarse de forma análoga.

---

**Resumen:**
- Identifica la recurrencia de tu algoritmo.
- Compara los valores de a, b y d para aplicar el caso correcto del teorema maestro.
- Así obtienes la complejidad temporal (y, si aplica, espacial) del algoritmo recursivo.

---

## Cálculo de la complejidad no recursiva

Para analizar la complejidad de un código **no recursivo**, es importante contar la cantidad de veces que se ejecutan las instrucciones principales, considerando ciclos (anidados o paralelos), condicionales e invocaciones.

**Pasos generales:**
1. Identifica los bucles y su rango de iteración.
2. Expresa el número total de operaciones como una sumatoria o fórmula explícita.
3. Simplifica la expresión para obtener una función en términos de N (o la variable relevante).
4. Determina la O grande de la expresión resultante.

### Ejemplo de código no recursivo

Supongamos el siguiente código:

```java
static public int surprise(int[] arreglo, int dim) {
    int vble = 0;
    for (int rec = 0; rec < dim; rec++) {
        for (int j = rec + 1; j < dim; j++) {
            if (arreglo[rec] * arreglo[j] == 0)
                vble++;
        }
    }
    return vble;
}
```

#### Análisis de Times(dim):

- El ciclo externo va de rec = 0 hasta rec < dim (dim iteraciones).
- El ciclo interno va de j = rec + 1 hasta j < dim (dim - rec - 1 iteraciones para cada rec).
- Dentro del ciclo interno hay una operación constante.

La cantidad total de operaciones es:

    Times(dim) = suma para rec desde 0 hasta dim-1 de (3 + suma para j desde rec+1 hasta dim-1 de 5)

Desarrollando la sumatoria interna:

    Times(dim) = suma para rec desde 0 hasta dim-1 de (3 + 5 * (dim - rec - 1))

Simplificando:

    Times(dim) = suma para rec desde 0 hasta dim-1 de (5 * dim - 5 * rec - 2)

Separando términos:

    Times(dim) = suma para rec desde 0 hasta dim-1 de (5 * dim - 2) menos suma para rec desde 0 hasta dim-1 de (5 * rec)

    Times(dim) = (5 * dim - 2) * dim + 5 * suma para rec desde 0 hasta dim-1 de rec

Sabemos que la suma de los primeros n números es n * (n + 1) / 2, así que:

    suma para rec desde 0 hasta dim-1 de rec = (dim - 1) * dim / 2

Por lo tanto:

    Times(dim) = (5 * dim - 2) * dim + 5 * ((dim - 1) * dim / 2)

Esto es una expresión cuadrática, por lo que la complejidad es:

    O(dim^2)

**Conclusión:**
- Para código no recursivo, el análisis se basa en contar iteraciones y operaciones, usando sumas y simplificaciones algebraicas.
- El resultado final se expresa en notación O grande.

---

## Ejemplo de aplicación del Teorema Maestro

Veamos el siguiente código recursivo:

```java
public static int surprise(int N) {
    if (N < 4)
        return 16;
    int aux1 = surprise(N / 3);
    int aux2 = surprise(N / 3);
    return aux1 + aux2;
}
```

La cantidad de operaciones principales (Times(N)) se puede expresar como:

    Times(N) = 2 * Times(N / 3) + 4    si N >= 4
    Times(N) = 1                       si N < 4

Donde el 4 representa el costo constante de las operaciones fuera de la recursión (asignaciones y suma).

### ¿Se puede aplicar el Teorema Maestro?

Sí. La recurrencia tiene la forma:

    T(N) = a * T(N / b) + c

En este caso:
- a = 2 (dos llamadas recursivas)
- b = 3 (el tamaño se divide por 3)
- c = 4 (trabajo fuera de la recursión)
- d = 0 (el término fuera de la recursión es constante)

### ¿Qué caso del teorema maestro aplica?

Comparamos a y b^d:
- b^d = 3^0 = 1
- a = 2

Como a > b^d, corresponde el **caso 3** del teorema maestro:

    T(N) = O(N^(log base b de a))

En este ejemplo:
- log base 3 de 2 ≈ 0.63

Por lo tanto:

    T(N) = O(N^0.63)

**Conclusión:**
- El algoritmo tiene una complejidad sublineal, O(N^0.63), según el teorema maestro.

---

## Ejemplo variante: Recurrencia con ciclo for

Considera el siguiente código:

```java
public static int surprise(int N) {
    if (N < 4)
        return 16;
    for (int i = 0; i < N; i++) {
        System.out.println(i);
    }
    int aux1 = surprise(N / 3);
    int aux2 = surprise(N / 3);
    return aux1 + aux2;
}
```

La cantidad de operaciones principales (Times(N)) se puede expresar como:

    Times(N) = 2 * Times(N / 3) + O(N)    si N >= 4
    Times(N) = 1                          si N < 4

Donde O(N) representa el costo del ciclo for.

### ¿Se puede aplicar el Teorema Maestro?

Sí. La recurrencia tiene la forma:

    T(N) = a * T(N / b) + c * N^d

En este caso:
- a = 2 (dos llamadas recursivas)
- b = 3 (el tamaño se divide por 3)
- d = 1 (el trabajo fuera de la recursión es lineal en N)

### ¿Qué caso del teorema maestro aplica?

Comparamos a y b^d:
- b^d = 3^1 = 3
- a = 2

Como a < b^d, corresponde el **caso 1** del teorema maestro:

    T(N) = O(N^d)

En este ejemplo:
- d = 1

Por lo tanto:

    T(N) = O(N)

**Conclusión:**
- El algoritmo tiene complejidad lineal, O(N), según el teorema maestro. 