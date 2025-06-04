# Árboles B (B-Trees) - Teoría Completa

## Introducción
Los **árboles B** son estructuras de datos de árbol que mantienen los datos ordenados y permiten búsquedas, acceso secuencial, inserciones y eliminaciones en tiempo logarítmico. Son una generalización de los árboles de búsqueda binarios donde un nodo puede tener más de dos hijos.

Los árboles B están optimizados para sistemas que leen y escriben grandes bloques de datos, siendo comúnmente utilizados en **bases de datos** y **sistemas de archivos**.

## Árbol Multicamino M-ario

### Definición
Un árbol multicamino M-ario es una estructura donde:
- Cada nodo tiene hasta **M-1 claves** y **M hijos**
- Las claves se mantienen **ordenadas** dentro de cada nodo
- Los subárboles mantienen la **propiedad de búsqueda generalizada**

### Propiedad de Búsqueda Generalizada
Para un nodo con claves [k₁, k₂, ..., kₘ₋₁]:
- Todas las claves en el subárbol hijo₀ < k₁
- k₁ < todas las claves en el subárbol hijo₁ < k₂
- k₂ < todas las claves en el subárbol hijo₂ < k₃
- ...
- kₘ₋₁ < todas las claves en el subárbol hijoₘ₋₁

## Árbol B de Orden N

### Definición
Un **árbol B de orden N** es un árbol de búsqueda balanceado que cumple las siguientes propiedades:

1. **Número de claves por nodo:**
   - Máximo: **2N claves**
   - Mínimo: **N claves** (excepto la raíz)
   - La raíz puede tener de 1 a 2N claves

2. **Relación claves-hijos:**
   - Un nodo con **m claves** tiene **m+1 hijos**

3. **Estructura balanceada:**
   - **Todas las hojas están al mismo nivel**

4. **Ordenamiento:**
   - Las claves dentro de cada nodo están ordenadas
   - Se mantiene la propiedad de búsqueda del BST

### Parámetros en el Código
```java
public BTree(int order) {
    this.minKeySize = order;              // N
    this.minChildrenSize = minKeySize + 1; // N + 1
    this.maxKeySize = 2 * minKeySize;     // 2N
    this.maxChildrenSize = maxKeySize + 1; // 2N + 1
}
```

## Operaciones en Árboles B

### 1. Búsqueda

#### Algoritmo
1. **Búsqueda secuencial** en el nodo actual
2. Según la comparación, **seguir en el subárbol correspondiente**
3. Si el puntero es **nulo**, la clave no está en el árbol

#### Implementación
```java
private Node<T> getNode(T value) {
    Node<T> node = root;
    while (node != null) {
        // Verificar si es menor que la primera clave
        T lesser = node.getKey(0);
        if (value.compareTo(lesser) < 0) {
            node = node.getChild(0);
            continue;
        }

        // Verificar si es mayor que la última clave
        int numberOfKeys = node.numberOfKeys();
        T greater = node.getKey(numberOfKeys - 1);
        if (value.compareTo(greater) > 0) {
            node = node.getChild(numberOfKeys);
            continue;
        }

        // Buscar en claves internas
        for (int i = 0; i < numberOfKeys; i++) {
            T currentValue = node.getKey(i);
            if (currentValue.compareTo(value) == 0) {
                return node; // ¡Encontrado!
            }
            // Determinar subárbol correcto
            // ...
        }
    }
    return null;
}
```

### 2. Inserción

#### Principios
- **Siempre se inserta en una hoja**
- Si se excede el límite de claves, se realiza **división (split)**
- La división puede propagarse hacia arriba (**recursiva**)

#### Algoritmo de Inserción
1. Navegar hasta la hoja correspondiente
2. Insertar la clave en la hoja
3. Si el nodo excede `maxKeySize`, realizar división

#### División (Split)
```java
private void split(Node<T> nodeToSplit) {
    Node<T> node = nodeToSplit;
    int numberOfKeys = node.numberOfKeys();
    int medianIndex = numberOfKeys / 2;
    T medianValue = node.getKey(medianIndex);

    // Crear nodo izquierdo
    Node<T> left = new Node<T>(null, maxKeySize, maxChildrenSize);
    for (int i = 0; i < medianIndex; i++) {
        left.addKey(node.getKey(i));
    }

    // Crear nodo derecho
    Node<T> right = new Node<T>(null, maxKeySize, maxChildrenSize);
    for (int i = medianIndex + 1; i < numberOfKeys; i++) {
        right.addKey(node.getKey(i));
    }

    // Mover hijos si existen
    if (node.numberOfChildren() > 0) {
        // Distribuir hijos entre left y right...
    }

    if (node.parent == null) {
        // Nueva raíz (aumenta altura del árbol)
        Node<T> newRoot = new Node<T>(null, maxKeySize, maxChildrenSize);
        newRoot.addKey(medianValue);
        root = newRoot;
        newRoot.addChild(left);
        newRoot.addChild(right);
    } else {
        // Subir mediana al padre
        Node<T> parent = node.parent;
        parent.addKey(medianValue);
        parent.removeChild(node);
        parent.addChild(left);
        parent.addChild(right);

        // Si el padre se desborda, dividir recursivamente
        if (parent.numberOfKeys() > maxKeySize) {
            split(parent);
        }
    }
}
```

#### Ejemplo Visual de Inserción y División
```
Insertar 7 en árbol B de orden 2 (máximo 4 claves):

Antes:                    División necesaria:
[1, 3, 5, 9, 11]         [1, 3, 5, 7, 9, 11] → Excede máximo

Después de división:
      [5]           ← Mediana sube
     /   \
  [1, 3] [7, 9, 11]  ← División en dos nodos
```

### 3. Eliminación (Borrado)

#### Casos de Eliminación
1. **Clave en hoja**: Eliminar directamente
2. **Clave en nodo interno**: Reemplazar por sucesor in-order

#### Algoritmo de Eliminación
```java
private T remove(T value, Node<T> node) {
    if (node == null) return null;

    T removed = null;
    int index = node.indexOf(value);
    removed = node.removeKey(value);

    if (node.numberOfChildren() == 0) {
        // HOJA: verificar si queda con pocas claves
        if (node.parent != null && node.numberOfKeys() < minKeySize) {
            combined(node); // Fusionar con hermanos
        }
    } else {
        // NODO INTERNO: reemplazar con sucesor
        Node<T> lesser = node.getChild(index);
        Node<T> greatest = getGreatestNode(lesser);
        T replaceValue = removeGreatestValue(greatest);
        node.addKey(replaceValue);
        
        // Verificar si el nodo afectado necesita fusión
        if (greatest.numberOfKeys() < minKeySize) {
            combined(greatest);
        }
    }
    return removed;
}
```

#### Fusión (Combined)
Cuando un nodo queda con menos claves de las permitidas:

```java
private boolean combined(Node<T> node) {
    Node<T> parent = node.parent;
    
    // Intentar "pedir prestado" de hermanos
    // Si no es posible, fusionar con un hermano
    
    if (rightNeighbor != null && rightNeighborSize > minKeySize) {
        // Pedir prestado del hermano derecho
        T parentValue = parent.removeKey(prev);
        T neighborValue = rightNeighbor.removeKey(0);
        node.addKey(parentValue);
        parent.addKey(neighborValue);
    } else {
        // Fusionar con hermano
        T parentValue = parent.removeKey(prev);
        parent.removeChild(rightNeighbor);
        node.addKey(parentValue);
        
        // Copiar todas las claves del hermano
        for (int i = 0; i < rightNeighbor.keysSize; i++) {
            node.addKey(rightNeighbor.getKey(i));
        }
        
        // Si el padre queda con pocas claves, repetir recursivamente
        if (parent.numberOfKeys() < minKeySize) {
            combined(parent);
        }
    }
    return true;
}
```

#### Ejemplo Visual de Eliminación
```
Eliminar 6 de árbol B orden 2:

Antes:           Nodo interno:         Reemplazar con sucesor:
    [6, 10]          [6, 10]              [7, 10]
   /   |   \    →   /   |   \       →    /   |   \
[1,3] [7,8] [12]  [1,3] [7,8] [12]    [1,3] [8] [12]

El nodo [8] queda con 1 clave (< minKeySize=2), necesita fusión.
```

## Propiedades y Ventajas

### Complejidad Temporal
- **Búsqueda**: O(log n)
- **Inserción**: O(log n)
- **Eliminación**: O(log n)

### Ventajas
1. **Eficiencia en I/O**: Reduce accesos a disco
2. **Balanceado automáticamente**: Todas las hojas al mismo nivel
3. **Utilización del espacio**: Nodos parcialmente llenos minimizan desperdicio
4. **Escalabilidad**: Ideal para grandes volúmenes de datos

### Invariantes del Árbol B
El método `validate()` verifica:
```java
private boolean validateNode(Node<T> node) {
    // 1. Claves ordenadas dentro del nodo
    // 2. Número correcto de claves (min/max)
    // 3. Número correcto de hijos
    // 4. Relación claves-hijos (m claves → m+1 hijos)
    // 5. Propiedad de búsqueda entre nodos padre-hijo
    // 6. Todas las hojas al mismo nivel (implícito por construcción)
}
```

## Parámetros de Configuración

### Constructor por Defecto (Árbol 2-3)
```java
public BTree() {
    minKeySize = 1;      // N = 1
    maxKeySize = 2;      // 2N = 2
    minChildrenSize = 2; // N + 1 = 2
    maxChildrenSize = 3; // 2N + 1 = 3
}
```

### Constructor Parametrizado
```java
public BTree(int order) {
    this.minKeySize = order;              // N
    this.maxKeySize = 2 * minKeySize;     // 2N
    this.minChildrenSize = minKeySize + 1; // N + 1
    this.maxChildrenSize = maxKeySize + 1; // 2N + 1
}
```

## Casos de Uso
- **Sistemas de bases de datos**: Índices B+ tree
- **Sistemas de archivos**: Organización de directorios
- **Motores de búsqueda**: Índices invertidos
- **Almacenamiento en disco**: Minimizar accesos a disco

## Comparación con Otros Árboles

| Característica | Árbol B | AVL | BST |
|----------------|---------|-----|-----|
| Factor de ramificación | Alto (N-ario) | 2 | 2 |
| Altura | O(log_N n) | O(log n) | O(n) worst |
| Operaciones I/O | Pocas | Medias | Muchas |
| Uso típico | Discos/DB | Memoria | Memoria |
| Balanceo | Automático | Rotaciones | Ninguno |

Los árboles B son fundamentales en el diseño de sistemas que manejan grandes volúmenes de datos, proporcionando un equilibrio óptimo entre el número de accesos a disco y la eficiencia de las operaciones. 