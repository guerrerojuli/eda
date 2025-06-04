package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DijkstraPath<V,E> {
	
	private Map<V,Integer> distancesFromSource;
	private Map<V,V> prevVertex; 
	
	public DijkstraPath( Map<V,Integer> distancesFromSource,  Map<V,V> prevVertex) {
		this.distancesFromSource= distancesFromSource;
		this.prevVertex= prevVertex;
		
	}
	
	
	@Override
	public String toString() {
		String rta= "";
		for(V aV: distancesFromSource.keySet()) {
			if ( distancesFromSource.get(aV) == Integer.MAX_VALUE )
				rta+= "INF: " + getShortestPathTo(aV) + "\n";
			else
				rta+= distancesFromSource.get(aV)+ ": " + getShortestPathTo(aV) + "\n";
		}
		
		return rta;
	}
	
	
	
	public String getShortestPathTo(V targetVertex){
		
		// habia camino?
		if (prevVertex.get(targetVertex) == null )
			return String.format( "[no path to %s]", targetVertex.toString() );
		
        List<V> path = new ArrayList<>();
 
        for(V vertex=targetVertex;vertex!=null;vertex=prevVertex.get(vertex)){
            path.add(vertex);
        }
 
        Collections.reverse(path);
        
         return path.toString();
    }
 
}
