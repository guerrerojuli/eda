Agregar a messages.properties
dijkstraWithNegativeWeight=Dijkstra cannot be calculate with edge source %s target %s with negative weight %d
dijkstraNotForMulti=Classical Dijkstra algorithm is not defined for Multigraphs.
printAllPathsNotDefinedForMulti=printAllPaths is not defined for Multigraphs
printAllPathsNotDefinedForGraphsWithSlefLoops=printAllPaths is not defined for graphs with self-loops



En Multi.java:

public DijkstraPath<V,E> dijsktra(V source) {
		throw new RuntimeException(Messages.getString("dijkstraNotForMulti"));
	}




En SimpleOrDefault.java


	@Override
	// Dijsktra exige que los pesos sean positivos!!!
	public DijkstraPath<V, E> dijsktra(V source) {
		if (source == null || !existsVertex(source) )
			throw new IllegalArgumentException(Messages.getString("vertexParamError")); 

		
		PriorityQueue<NodePQ> pq= new PriorityQueue<>();
	
		//stores shortest distance from source to every vertex
		Map<V,Integer> costo = new HashMap<>();
		Map<V,V> prev= new HashMap<>();
	
		// empieza vacio
		Set<V> nodesVisisted= new HashSet<>();
	
		// inicializacion+
		for(V aV: getAdjacencyList().keySet() ) {
			if (aV.equals(source)) {
			pq.add(new NodePQ(source, 0));
			costo.put(source, 0);
			}
			else {
			costo.put(aV, Integer.MAX_VALUE);
			}
		prev.put(aV, null);
		}
		
		while( ! pq.isEmpty()) {
			NodePQ current = pq.poll(); // el menor
			
			if (nodesVisisted.contains(current.vertex)) // ya lo procese
				continue;
	
			// a procesarlo! Con esto tambien se ignora self-loop
			nodesVisisted.add(current.vertex);
	
			// ahora recorrer todos los ejes incidentes a current
			Collection<AdjacencyListGraph<V, E>.InternalEdge> adjList = getAdjacencyList().get(current.vertex);
			for(InternalEdge neighbor: adjList) {
				// si fue visitado seguir. Esto tambien excluye los self loops...
				if ( nodesVisisted.contains(neighbor.target)) {
					continue;
				}
	
				// invocando a getWeight (se ha validado en insercion)
				int weight=0;
				// peso de ese eje?
				try {
					Method fn = neighbor.edge.getClass().getMethod("getWeight");
					weight = (int) fn.invoke(neighbor.edge);
				}
				catch (Exception e) {
				throw new RuntimeException(e);
				}

				// verificacion
				if (weight < 0 )
					throw new IllegalArgumentException(
							String.format(Messages.getString("dijkstraWithNegativeWeight"),
										current.vertex, neighbor.target, weight));
	
				// cual seria el costo de neighbor viniendo desde current?
				int newCosto = costo.get(current.vertex) + weight;

				// es una mejora?
				if (newCosto < costo.get(neighbor.target) ) {
					// insertar neighbor con ese valor mejorado
					costo.put(neighbor.target, newCosto);
					pq.add(new NodePQ(neighbor.target, newCosto));
					
					// armar camino
					prev.put(neighbor.target, current.vertex);
				}
			}
		}
		return new DijkstraPath<>(costo, prev);
	}



y  una clase inner



class NodePQ implements Comparable<NodePQ> {
		V vertex;
		Double distance;

		public NodePQ(V vertex, double distance) {
			this.vertex= vertex;
			this.distance= distance;
		}
		
		@Override
		public int compareTo(NodePQ o2) {
			return Double.compare( distance, o2.distance);
		}
	}


