En Multi:
	public void printAllPaths(V start, V end) {
		throw new RuntimeException(Messages.getString("printAllPathsNotDefinedForMulti"));
	}

En Simple:

	@Override
	public void printAllPaths(V startNode, V endNode) {
		if (startNode == null || !existsVertex(startNode) || endNode == null || !existsVertex(endNode) )
			throw new IllegalArgumentException(Messages.getString("vertexParamError"));
	
		if (acceptSelfLoop)
			throw new IllegalArgumentException(Messages.getString("printAllPathsNotDefinedForGraphsWithSlefLoops"));
		
		Set<V> visited= new HashSet<V>();
		ArrayList<V> path= new ArrayList<>();
		printAllPaths(startNode, endNode, visited, path);
	}
	
	
	public void printAllPaths(V startNode, V endNode, Set<V> visited, ArrayList<V> path ) {
		// proceso el nodo
		path.add(startNode);
		visited.add(startNode);
		
		// end?
		if (startNode.equals(endNode)) {
			System.out.println(path);
			
			// deshago aca porque no voy a pasar por flujo normal
			visited.remove(endNode);
			path.remove(endNode);
			return;
		}
		
		// go on
		Collection<InternalEdge> adjListOther = getAdjacencyList().get(startNode);
		for (InternalEdge internalEdge : adjListOther) {
			if (! visited.contains(internalEdge.target)) {
				printAllPaths(internalEdge.target, endNode, visited, path);
			}
		}
		
		// lo deshago
		visited.remove(startNode);
		path.remove(startNode);
	}