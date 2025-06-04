# Árboles AVL - Teoría de Rotaciones

## Definición
Un **AVL** es un BST (Binary Search Tree) balanceado por altura donde la diferencia de alturas (factor de balance, fb) entre los subárboles izquierdo y derecho de cada nodo es como máximo 1.

Propuestos en 1962 por **Adelson-Velskii y Landis**.

## Factor de Balance
```
fb = altura(subárbol izquierdo) - altura(subárbol derecho)
```

**Condición de equilibrio:** Si |fb| > 1, el árbol no es AVL y requiere reestructuración mediante rotaciones.

### Valores posibles del factor de balance:
- **fb = -1**: Subárbol derecho es más alto por 1
- **fb = 0**: Ambos subárboles tienen la misma altura
- **fb = 1**: Subárbol izquierdo es más alto por 1
- **fb = -2 o fb = 2**: Árbol desbalanceado, requiere rotación

## Operaciones en AVL

### Inserción
Tras insertar un nodo en un BST, si el árbol deja de ser AVL, se realiza una rotación para restaurar el equilibrio.

**Regla importante:** Se rota usando el **pivote más joven desbalanceado** (más cercano al nodo insertado).

## Los 4 Casos de Rotaciones

Las rotaciones permiten mantener el orden (in-order traversal) y restablecer el equilibrio del árbol.

### CASO A: Inserción a izquierda de nodo con fb = 1 → se vuelve fb = 2

#### A1: Rotación Simple a Derecha (R)
**Cuándo se usa:** Cuando el nodo se inserta en el subárbol izquierdo del hijo izquierdo.

**Estructura del desbalance:**
```
    Y (fb = 2)
   /
  X (fb = 1)
 /
Z (nuevo nodo)
```

**Algoritmo de rotación a derecha:**
```java
private AVLNode<T> rotateRight(AVLNode<T> y) {
    AVLNode<T> x = y.left;
    AVLNode<T> T2 = x.right;

    // Realizar rotación
    x.right = y;
    y.left = T2;

    // Actualizar alturas
    updateHeight(y);
    updateHeight(x);

    return x; // Nueva raíz del subárbol
}
```

**Ejemplo visual:**
```
Antes:          Después:
    4 (fb=2)        2 (fb=0)
   /               / \
  2 (fb=1)        1   4
 /                   /
1                   3
```

#### A2: Rotación Doble Izquierda-Derecha (LR)
**Cuándo se usa:** Cuando el nodo se inserta en el subárbol derecho del hijo izquierdo.

**Estructura del desbalance:**
```
    Z (fb = 2)
   /
  X (fb = -1)
   \
    Y (nuevo nodo)
```

**Algoritmo:**
1. Primero rotación izquierda en X
2. Luego rotación derecha en Z

```java
// En el método insertRec:
if (balance > 1 && data.compareTo(node.left.data) > 0) {
    node.left = rotateLeft(node.left);  // Paso 1
    return rotateRight(node);           // Paso 2
}
```

**Ejemplo visual:**
```
Antes:              Paso 1 (L en 1):      Paso 2 (R en 4):
    4 (fb=2)            4 (fb=2)              3 (fb=0)
   /                   /                     / \
  1 (fb=-1)           3 (fb=1)             1   4
   \                 /                      \   /
    3               1                        2 2
```

### CASO B: Inserción a derecha de nodo con fb = -1 → se vuelve fb = -2

#### B1: Rotación Simple a Izquierda (L)
**Cuándo se usa:** Cuando el nodo se inserta en el subárbol derecho del hijo derecho.

**Estructura del desbalance:**
```
X (fb = -2)
 \
  Y (fb = -1)
   \
    Z (nuevo nodo)
```

**Algoritmo de rotación a izquierda:**
```java
private AVLNode<T> rotateLeft(AVLNode<T> x) {
    AVLNode<T> y = x.right;
    AVLNode<T> T2 = y.left;

    // Realizar rotación
    y.left = x;
    x.right = T2;

    // Actualizar alturas
    updateHeight(x);
    updateHeight(y);

    return y; // Nueva raíz del subárbol
}
```

**Ejemplo visual:**
```
Antes:          Después:
1 (fb=-2)           3 (fb=0)
 \                 / \
  3 (fb=-1)       1   4
   \               \
    4               2
```

#### B2: Rotación Doble Derecha-Izquierda (RL)
**Cuándo se usa:** Cuando el nodo se inserta en el subárbol izquierdo del hijo derecho.

**Estructura del desbalance:**
```
X (fb = -2)
 \
  Z (fb = 1)
 /
Y (nuevo nodo)
```

**Algoritmo:**
1. Primero rotación derecha en Z
2. Luego rotación izquierda en X

```java
// En el método insertRec:
if (balance < -1 && data.compareTo(node.right.data) < 0) {
    node.right = rotateRight(node.right); // Paso 1
    return rotateLeft(node);              // Paso 2
}
```

**Ejemplo visual:**
```
Antes:              Paso 1 (R en 4):      Paso 2 (L en 1):
1 (fb=-2)              1 (fb=-2)              3 (fb=0)
 \                      \                     / \
  4 (fb=1)               3 (fb=-1)           1   4
 /                        \                   \   /
3                          4                   2 2
 \                        /
  2                      2
```

## Detección de Casos en el Código

En el método `insertRec` del AVL.java, la detección se hace comparando el nuevo dato con los nodos involucrados:

```java
// Caso A1: Left Left
if (balance > 1 && data.compareTo(node.left.data) < 0) {
    return rotateRight(node);
}

// Caso B1: Right Right  
if (balance < -1 && data.compareTo(node.right.data) > 0) {
    return rotateLeft(node);
}

// Caso A2: Left Right
if (balance > 1 && data.compareTo(node.left.data) > 0) {
    node.left = rotateLeft(node.left);
    return rotateRight(node);
}

// Caso B2: Right Left
if (balance < -1 && data.compareTo(node.right.data) < 0) {
    node.right = rotateRight(node.right);
    return rotateLeft(node);
}
```

## Actualización de Alturas

Después de cada rotación, es crucial actualizar las alturas:

```java
private void updateHeight(AVLNode<T> node) {
    if (node != null) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
}
```

## Propiedades Importantes

1. **Complejidad temporal:** O(log n) para inserción, búsqueda y eliminación
2. **El recorrido in-order se mantiene:** Las rotaciones preservan el orden BST
3. **Balance automático:** Cada inserción/eliminación mantiene la propiedad AVL
4. **Altura máxima:** Aproximadamente 1.44 × log₂(n), garantizando eficiencia

## Resumen de Rotaciones

| Caso | Estructura | Rotación | Descripción |
|------|------------|----------|-------------|
| A1   | LL         | R        | Simple derecha |
| A2   | LR         | L + R    | Doble: izq-der |
| B1   | RR         | L        | Simple izquierda |
| B2   | RL         | R + L    | Doble: der-izq |

Las rotaciones AVL son fundamentales para mantener el equilibrio del árbol y garantizar operaciones eficientes en tiempo logarítmico. 