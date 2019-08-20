package sr.unasat.ad.graph;

import sr.unasat.ad.entities.Plaats;
import sr.unasat.ad.entities.VervoersType;

public class Vertex {
    public Plaats plaats;
    public boolean isInTree;
    public boolean wasVisited;

    public Vertex(Plaats p) {
        plaats = p;
        isInTree = false;
        wasVisited = false;
    }
}
