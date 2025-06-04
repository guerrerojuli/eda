# Teoría de Grafos - Representación y Uso

## 1. Introducción y Motivaciones

Los **grafos** son estructuras matemáticas utilizadas para modelar múltiples situaciones del mundo real donde existen relaciones entre elementos.

### Problemas Históricos y Aplicaciones

#### Puentes de Königsberg (Prusia)
- **Problema histórico** modelado como grafo no dirigido
- **Teorema de Euler**: Un camino euleriano existe solo si hay **0 o 2 nodos con grado impar**
- Demostró la imposibilidad de recorrer todos los puentes exactamente una vez

#### Aplicaciones Modernas
- **Rutas y transporte**: OpenStreetMap usa grafos dirigidos y ponderados para representar calles
- **Redes sociales**: Interacciones en Twitter, Facebook, DBLP permiten análisis de:
  - Comunidades y clustering
  - Influencia y propagación de información
  - Sistemas de recomendaciones
- **Compiladores y planificaciones**: 
  - Orden de ejecución de tareas
  - Dependencias de paquetes (ej. Maven)
  - Fabricación industrial
  - Planes de estudio universitarios

## 2. Tipos de Grafos

### Clasificación por Dirección y Multiplicidad

#### Grafos No Dirigidos
1. **Simple**: A lo sumo un eje entre pares de nodos. **No admite lazos**.
2. **Multigrafo**: **Múltiples ejes** entre nodos. Sin lazos.
3. **Pseudografo**: Múltiples ejes y **admite lazos** (self-loops).

#### Grafos Dirigidos (Digrafos)
1. **Simple digrafo**: Un solo eje por dirección entre nodos. Sin lazos.
2. **Multi digrafo**: **Múltiples ejes dirigidos** entre pares de nodos.
3. **Pseudo digrafo**: Múltiples ejes dirigidos y **admite lazos**.

### Clasificación Adicional por Peso
Si admiten **peso en los ejes**: Total de **16 tipos posibles** de grafos.

### Propiedades Específicas
Algunas propiedades y algoritmos solo aplican a ciertos tipos:
- **inDegree, outDegree**: Solo grafos dirigidos
- **Camino mínimo**: Requiere grafos ponderados
- **Detección de ciclos**: Algoritmos diferentes para dirigidos vs no dirigidos

## 3. Representación de Grafos

### Representación Formal
Un grafo G = (V, E) donde:
- **V**: Conjunto de vértices (vertices)
- **E**: Conjunto de aristas o ejes (edges)

### Estructuras para Representar Ejes

#### 1. Matriz de Adyacencia
```java
// Útil para grafos densos
boolean[][] adjMatrix = new boolean[n][n];
// adjMatrix[i][j] = true si existe eje de i a j
```
- **Ventajas**: Acceso O(1) para verificar existencia de eje
- **Desventajas**: Espacio O(V²), ineficiente para grafos dispersos

#### 2. Lista de Adyacencia
```java
// Ideal para grafos dispersos  
Map<V, Collection<InternalEdge>> adjacencyList = new HashMap<>();
```
- **Ventajas**: Espacio O(V + E), eficiente para grafos dispersos
- **Desventajas**: Verificar existencia de eje puede ser O(V)

#### 3. Matriz de Incidencia
- **Filas**: Vértices
- **Columnas**: Ejes
- Útil para análisis matemático, menos común en implementaciones

#### 4. Lista de Incidencia
- Representación compacta de la matriz anterior

## 4. Implementación con Factory y Builder

### Patrón Factory
Se usa `GraphFactory` para crear grafos sin conocer detalles de implementación:

```java
GraphService<V, E> graph = GraphFactory.create(
    Multiplicity.SIMPLE,     // SIMPLE | MULTIPLE
    EdgeMode.UNDIRECTED,     // DIRECTED | UNDIRECTED  
    SelfLoop.NO,             // YES | NO
    Weight.NO,               // YES | NO
    Storage.SPARSE           // SPARSE | DENSE
);
```

### Patrón Builder
Alternativa flexible al factory:

```java
GraphService<Character, EmptyEdgeProp> graph = 
    new GraphBuilder<Character, EmptyEdgeProp>()
        .withMultiplicity(Multiplicity.SIMPLE)
        .withDirected(EdgeMode.UNDIRECTED)
        .withAcceptSelfLoop(SelfLoop.NO)
        .withAcceptWeight(Weight.YES)
        .withStorage(Storage.SPARSE)
        .build();
```

**Ventajas del Builder:**
- Especificar propiedades en cualquier orden
- Usar valores por defecto para propiedades no especificadas
- Sintaxis más legible y flexible

## 5. Implementación Interna

### Jerarquía de Clases
```
GraphService<V,E> (interface)
    ↑
AdjacencyListGraph<V,E> (abstract class)
    ↑                    ↑
SimpleOrDefault<V,E>   Multi<V,E>
```

### Clase Base: AdjacencyListGraph
```java
abstract public class AdjacencyListGraph<V, E> implements GraphService<V, E> {
    private boolean isSimple;
    protected boolean isDirected;
    private boolean acceptSelfLoop;
    private boolean isWeighted;
    
    // Estructura principal: HashMap de listas de adyacencia
    private Map<V, Collection<InternalEdge>> adjacencyList = new HashMap<>();
}
```

### Clase InternalEdge
```java
class InternalEdge {
    E edge;      // Propiedades del eje (peso, tipo, etc.)
    V target;    // Vértice destino
    
    // Implementa equals() y hashCode() para comparaciones correctas
}
```

### Diferencias entre Implementaciones

#### SimpleOrDefault
- **No permite** más de un eje entre dos vértices
- **Validación estricta** en `addEdge()`:
```java
// Check if edge already exists (simple graph constraint)
Collection<InternalEdge> adjacentEdges = getAdjacencyList().get(aVertex);
for (InternalEdge existingEdge : adjacentEdges) {
    if (existingEdge.target.equals(otherVertex)) {
        throw new RuntimeException("Simple graph does not allow multiple edges");
    }
}
```

#### Multi
- **Permite múltiples ejes** entre dos vértices
- **Cuidado al eliminar**: Puede lanzar excepción si hay ambigüedad
- Necesita especificar **qué eje específico** eliminar

## 6. Algoritmos Fundamentales

### Algoritmos de Recorrido

#### BFS (Breadth-First Search)
```java
@Override
public void printBFS(V vertex) {
    Queue<V> queue = new LinkedList<>();
    Set<V> visited = new HashSet<>();
    
    queue.offer(vertex);
    visited.add(vertex);
    
    while (!queue.isEmpty()) {
        V current = queue.poll();
        System.out.print(current + " ");
        
        for (InternalEdge edge : getAdjacencyList().get(current)) {
            if (!visited.contains(edge.target)) {
                visited.add(edge.target);
                queue.offer(edge.target);
            }
        }
    }
}
```

#### DFS (Depth-First Search)
```java
@Override
public void printDFS(V vertex) {
    Set<V> visited = new HashSet<>();
    dfsRecursive(vertex, visited);
}

private void dfsRecursive(V vertex, Set<V> visited) {
    visited.add(vertex);
    System.out.print(vertex + " ");
    
    for (InternalEdge edge : getAdjacencyList().get(vertex)) {
        if (!visited.contains(edge.target)) {
            dfsRecursive(edge.target, visited);
        }
    }
}
```

### Algoritmo de Dijkstra
**Requisitos**: Grafo ponderado con pesos **no negativos**

```java
@Override
public DijkstraPath<V, E> dijkstra(V source) {
    if (!isWeighted()) {
        throw new RuntimeException("Dijkstra requires weighted graph");
    }
    
    PriorityQueue<NodePQ> pq = new PriorityQueue<>();
    Map<V, Integer> cost = new HashMap<>();
    Map<V, V> previous = new HashMap<>();
    Set<V> visited = new HashSet<>();
    
    // Inicialización
    for (V vertex : getAdjacencyList().keySet()) {
        cost.put(vertex, vertex.equals(source) ? 0 : Integer.MAX_VALUE);
        previous.put(vertex, null);
    }
    
    pq.add(new NodePQ(source, 0));
    
    while (!pq.isEmpty()) {
        NodePQ current = pq.poll();
        
        if (visited.contains(current.vertex)) continue;
        visited.add(current.vertex);
        
        // Relajación de ejes
        for (InternalEdge neighbor : getAdjacencyList().get(current.vertex)) {
            if (!visited.contains(neighbor.target)) {
                int weight = getEdgeWeight(neighbor.edge);
                int newCost = cost.get(current.vertex) + weight;
                
                if (newCost < cost.get(neighbor.target)) {
                    cost.put(neighbor.target, newCost);
                    previous.put(neighbor.target, current.vertex);
                    pq.add(new NodePQ(neighbor.target, newCost));
                }
            }
        }
    }
    
    return new DijkstraPath<>(cost, previous);
}
```

### Detección de Ciclos

#### Para Grafos Dirigidos
```java
private boolean hasDirectedCycle() {
    Set<V> visited = new HashSet<>();
    Set<V> recursionStack = new HashSet<>();
    
    for (V vertex : getVertices()) {
        if (!visited.contains(vertex)) {
            if (hasDirectedCycleDFS(vertex, visited, recursionStack)) {
                return true;
            }
        }
    }
    return false;
}

private boolean hasDirectedCycleDFS(V vertex, Set<V> visited, Set<V> recursionStack) {
    visited.add(vertex);
    recursionStack.add(vertex);
    
    for (InternalEdge edge : getAdjacencyList().get(vertex)) {
        V neighbor = edge.target;
        
        if (!visited.contains(neighbor)) {
            if (hasDirectedCycleDFS(neighbor, visited, recursionStack)) {
                return true;
            }
        } else if (recursionStack.contains(neighbor)) {
            return true; // Back edge encontrado = ciclo
        }
    }
    
    recursionStack.remove(vertex);
    return false;
}
```

#### Para Grafos No Dirigidos
```java
private boolean hasUndirectedCycle() {
    Set<V> visited = new HashSet<>();
    
    for (V vertex : getVertices()) {
        if (!visited.contains(vertex)) {
            if (hasUndirectedCycleDFS(vertex, visited, null)) {
                return true;
            }
        }
    }
    return false;
}

private boolean hasUndirectedCycleDFS(V vertex, Set<V> visited, V parent) {
    visited.add(vertex);
    
    for (InternalEdge edge : getAdjacencyList().get(vertex)) {
        V neighbor = edge.target;
        
        if (!visited.contains(neighbor)) {
            if (hasUndirectedCycleDFS(neighbor, visited, vertex)) {
                return true;
            }
        } else if (!neighbor.equals(parent)) {
            return true; // Ciclo encontrado
        }
    }
    return false;
}
```

### Verificación de Bipartición
```java
@Override
public boolean isBipartite() {
    Map<V, Integer> colors = new HashMap<>();
    
    for (V vertex : getVertices()) {
        if (!colors.containsKey(vertex)) {
            if (!isBipartiteComponent(vertex, colors)) {
                return false;
            }
        }
    }
    return true;
}

private boolean isBipartiteComponent(V startVertex, Map<V, Integer> colors) {
    Queue<V> queue = new LinkedList<>();
    queue.offer(startVertex);
    colors.put(startVertex, 0);
    
    while (!queue.isEmpty()) {
        V current = queue.poll();
        int currentColor = colors.get(current);
        
        for (InternalEdge edge : getAdjacencyList().get(current)) {
            V neighbor = edge.target;
            
            if (!colors.containsKey(neighbor)) {
                colors.put(neighbor, 1 - currentColor);
                queue.offer(neighbor);
            } else if (colors.get(neighbor) == currentColor) {
                return false; // Conflicto de color
            }
        }
    }
    return true;
}
```

## 7. Conceptos de Grado

### Grado (Grafos No Dirigidos)
```java
@Override
public int degree(V vertex) {
    if (isDirected) {
        throw new RuntimeException("Degree is not defined for directed graphs");
    }
    
    int degree = 0;
    Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
    
    for (InternalEdge edge : edges) {
        degree++;
        // Self-loop contribuye dos veces al grado
        if (edge.target.equals(vertex)) {
            degree++;
        }
    }
    return degree;
}
```

### InDegree y OutDegree (Grafos Dirigidos)
```java
@Override
public int inDegree(V vertex) {
    if (!isDirected) {
        throw new RuntimeException("InDegree is only defined for directed graphs");
    }
    
    int inDegree = 0;
    // Contar ejes que llegan a vertex desde otros vértices
    for (V otherVertex : getVertices()) {
        if (!otherVertex.equals(vertex)) {
            for (InternalEdge edge : getAdjacencyList().get(otherVertex)) {
                if (edge.target.equals(vertex)) {
                    inDegree++;
                }
            }
        }
    }
    
    // Self-loops contribuyen tanto a inDegree como outDegree
    for (InternalEdge edge : getAdjacencyList().get(vertex)) {
        if (edge.target.equals(vertex)) {
            inDegree++;
        }
    }
    
    return inDegree;
}

@Override
public int outDegree(V vertex) {
    if (!isDirected) {
        throw new RuntimeException("OutDegree is only defined for directed graphs");
    }
    
    return getAdjacencyList().get(vertex).size();
}
```

## 8. Consideraciones de Implementación

### HashMap vs LinkedHashMap
```java
// HashMap no respeta el orden de inserción
private Map<V, Collection<InternalEdge>> adjacencyList = new HashMap<>();

// LinkedHashMap respeta el orden de llegada (útil para testing)
// private Map<V, Collection<InternalEdge>> adjacencyList = new LinkedHashMap<>();
```

### Validaciones de Tipos
- **V (Vertex)**: Debe implementar `equals()` y `hashCode()` correctamente
- **E (Edge)**: Debe implementar `equals()` para eliminación correcta
- **Grafos ponderados**: E debe implementar método `getWeight()`

### Validación de Reflection para Pesos
```java
if (isWeighted) {
    Class<? extends Object> c = theEdge.getClass();
    try {
        c.getDeclaredMethod("getWeight");
    } catch (NoSuchMethodException | SecurityException e) {
        throw new RuntimeException(
            type + " is weighted but method getWeight() not declared in edge");
    }
}
```

## 9. Complejidades Temporales

| Operación | Lista de Adyacencia | Matriz de Adyacencia |
|-----------|-------------------|---------------------|
| Agregar vértice | O(1) | O(V²) |
| Agregar eje | O(1) amortizado | O(1) |
| Verificar eje | O(grado(v)) | O(1) |
| Eliminar eje | O(grado(v)) | O(1) |
| Espacio | O(V + E) | O(V²) |

## 10. Casos de Uso y Patrones

### Cuándo Usar Cada Tipo
- **Lista de Adyacencia**: Grafos dispersos (pocas conexiones)
- **Matriz de Adyacencia**: Grafos densos (muchas conexiones)
- **Grafo Simple**: Relaciones únicas entre entidades
- **Multigrafo**: Múltiples tipos de relaciones entre entidades

### Aplicaciones por Algoritmo
- **BFS**: Camino más corto (sin pesos), componentes conexas
- **DFS**: Detección de ciclos, ordenamiento topológico
- **Dijkstra**: Camino más corto con pesos positivos
- **Detección de ciclos**: Validación de dependencias

Los grafos son estructuras fundamentales en ciencias de la computación, proporcionando una base sólida para modelar y resolver problemas complejos del mundo real. 