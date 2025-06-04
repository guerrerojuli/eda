package Test;

import core.DijkstraPath;
import core.GraphFactory;
import core_service.GraphBuilder;
import core_service.GraphService;
import use.EmptyEdgeProp;
import use.WeightedEdge;

public class Test {
    public static void main(String[] args) {
        GraphService<Integer, EmptyEdgeProp> g = new GraphBuilder<Integer,EmptyEdgeProp>().
                withMultiplicity(GraphService.Multiplicity.SIMPLE).withDirected(GraphService.EdgeMode.DIRECTED).
                withAcceptSelfLoop(GraphService.SelfLoop.YES).
                withAcceptWeight(GraphService.Weight.NO).
                withStorage(GraphService.Storage.SPARSE).build();
        g.addVertex(8);
        g.addVertex(2);
        g.addEdge(2, 9, new EmptyEdgeProp());
        g.addEdge(2, 8, new EmptyEdgeProp());
        g.addEdge(8, 3, new EmptyEdgeProp());
        g.addEdge(9, 4, new EmptyEdgeProp());
        g.addEdge(4, 5, new EmptyEdgeProp());
        g.addEdge(3, 5, new EmptyEdgeProp());
        g.dump();

        System.out.println("BFS: ");
        g.printBFS(2);
        System.out.println("Iterable BFS");
        for(Integer node: g.getBFS(2))
            System.out.print((node + " "));
        System.out.println();

        System.out.println("DFS: ");
        g.printBFS(2);
        System.out.println("Iterable DFS");
        for(Integer node: g.getDFS(2))
            System.out.print((node + " "));
        System.out.println();

        System.out.println("Dijkstra Path:");
        GraphService<Character, WeightedEdge> d =  GraphFactory.create(GraphService.Multiplicity.SIMPLE, GraphService.EdgeMode.DIRECTED, GraphService.SelfLoop.NO, GraphService.Weight.YES, GraphService.Storage.SPARSE);
        d.addEdge('A', 'B', new WeightedEdge(10));
        d.addEdge('A', 'C', new WeightedEdge(3));
        d.addEdge('B', 'C', new WeightedEdge(1));
        d.addEdge('B', 'D', new WeightedEdge(2));
        d.addEdge('C', 'A', new WeightedEdge(1));
        d.addEdge('C', 'B', new WeightedEdge(4));
        d.addEdge('C', 'D', new WeightedEdge(8));
        d.addEdge('C', 'E', new WeightedEdge(2));
        d.addEdge('D', 'E', new WeightedEdge(7));
        d.addEdge('E', 'D', new WeightedEdge(9));
        d.addEdge('Z', 'K', new WeightedEdge(17));
        d.addEdge('K', 'A', new WeightedEdge(19));
        DijkstraPath<Character, WeightedEdge> pathRta = d.dijsktra('A');
        System.out.println(pathRta);
    }
}
