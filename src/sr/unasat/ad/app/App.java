package sr.unasat.ad.app;

import sr.unasat.ad.entities.Plaats;
import sr.unasat.ad.entities.VervoersType;
import sr.unasat.ad.graph.WeightedGraph;

public class App {
    public static void main(String[] args) {
        WeightedGraph theGraph = new WeightedGraph();

        Plaats plaats0 = new Plaats("p01", "Waterloo");

        Plaats plaats1 = new Plaats("p02", "Nieuw Nickerie");
        Plaats plaats2 = new Plaats("p03", "Van Drimmelen Polder");
        Plaats plaats3 = new Plaats("p04", "Van Petten Polder");
        Plaats plaats4 = new Plaats("p05", "Hamptoncourt Polder");
        Plaats plaats5 = new Plaats("p06", "Henar Polder");
        Plaats plaats6 = new Plaats("p07", "Wageningen");

        theGraph.addVertex(plaats0);
        theGraph.addVertex(plaats1);
        theGraph.addVertex(plaats2);
        theGraph.addVertex(plaats3);
        theGraph.addVertex(plaats4);
        theGraph.addVertex(plaats5);
        theGraph.addVertex(plaats6);

        theGraph.addEdge(0, 1, 20);
        theGraph.addEdge(0, 4, 45);

        theGraph.addEdge(1, 2, 40);
        theGraph.addEdge(1, 3, 35);

        theGraph.addEdge(2, 3, 25);

        theGraph.addEdge(4, 5, 60);
        theGraph.addEdge(4, 6, 85);

        theGraph.path(0, true);

        System.out.println();

        theGraph.path(0, false);

        System.out.println();

        theGraph.dfs(0, "Waterloo");

        System.out.println();

        theGraph.bfs(0, 1);

    }
}
