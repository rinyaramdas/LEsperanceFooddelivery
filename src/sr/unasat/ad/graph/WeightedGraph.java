package sr.unasat.ad.graph;

import sr.unasat.ad.entities.Plaats;
import sr.unasat.ad.entities.VervoersType;
import sr.unasat.ad.queue.QueueB;
import sr.unasat.ad.stack.StackA;

public class WeightedGraph {
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000000;
    private Vertex vertexList[]; //array van vertices
    private int adjMat[][]; //adjacency matrix
    private int nVerts; //current number of vertices
    private int nTree;
    private DistPar sPath[];
    private int currentVert;
    private int startToCurrent;
    private StackA theStack;
    private QueueB theQueue;
    private QueueB weightQueue;

    public WeightedGraph() {
        vertexList = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS]; //adjacency matrix
        nVerts = 0;
        nTree = 0;

        for (int j=0; j<MAX_VERTS; j++) {//set adjacency
            for (int k=0; k<MAX_VERTS; k++) {//matrix to 0
                adjMat[j][k] = INFINITY;
            }
        }
        sPath = new DistPar[MAX_VERTS];
        theStack = new StackA();
        theQueue = new QueueB();
        weightQueue = new QueueB();
    }

    public void addVertex(Plaats p) {
        vertexList[nVerts++] = new Vertex(p);
    }

    public void addEdge(int start, int end, int weight) {
        adjMat[start][end] = weight;
    }

    public void path(int start, boolean minWeight) {
        int startTree = 0;
        vertexList[startTree].isInTree = true;
        weightQueue.insert(startTree);
        int weight;

        if (minWeight) {
            weight = INFINITY;
        }
        else {
            weight = -INFINITY;
        }
        nTree = 1;
        for(int j=0; j<nVerts; j++) {
            int tempDist = adjMat[startTree][j];
            sPath[j] = new DistPar(startTree, tempDist);
        }
        while (nTree < nVerts) {
            int index;
            if (minWeight) {
                index = getMin();
            } else {
                index = getMax();
            }
            int dist = sPath[index].distance;
            if (minWeight) {
                if (dist > weight) {
                    weight = dist;
                    weightQueue.insert(index);
                } else {
                    break;
                }
            } else {
                if (dist < weight) {
                    weight = dist;
                    weightQueue.insert(index);
                } else {
                    break;
                }
            }
            if (dist == INFINITY) {
                System.out.println("There are unreachable vertices");
                break;
            } else {
                currentVert = index;
                startToCurrent = sPath[index].distance;
            }
            vertexList[currentVert].isInTree = true;
            nTree++;
            if (minWeight) {
                adjust_sPath_Min();
            } else {
                adjust_sPath_Max();
            }
        }
        displayPaths(minWeight);
        nTree = 0;
        for (int j=0; j<nVerts; j++) {
            vertexList[j].isInTree = false;
        }
    }

    public int getMin() {
        int minDist = INFINITY;
        int indexMin = 0;
        for (int j=1; j<nVerts; j++) {
            if (!vertexList[j].isInTree && sPath[j].distance < minDist) {
                minDist = sPath[j].distance;
                indexMin = j;
            }
        }
        return indexMin;
    }

    public int getMax() {
        int maxDist = INFINITY;
        int indexMax = 0;
        for (int j=1; j<nVerts; j++) {
            if (!vertexList[j].isInTree && sPath[j].distance != INFINITY && sPath[j].distance > maxDist) {
                maxDist = sPath[j].distance;
                indexMax = j;
            }
        }
        return indexMax;
    }

    public void adjust_sPath_Min() {
        int column = 1;
        while (column < nVerts) {
            if (vertexList[column].isInTree) {
                column++;
                continue;
            }
            int currentToFringe = adjMat[currentVert][column];
            int startToFringe = startToCurrent + currentToFringe;
            int sPathDist = sPath[column].distance;

            if (startToFringe < sPathDist) {
                sPath[column].parentVert = currentVert;
                sPath[column].distance = startToFringe;
            }
            column++;
        }
    }

    public void adjust_sPath_Max() {
        int column = 1;
        while (column < nVerts) {
            if (vertexList[column].isInTree) {
                column++;
                continue;
            }
            int currentToFringe = adjMat[currentVert][column];
            if (currentToFringe != INFINITY) {
                int startToFringe = startToCurrent + currentToFringe;
                int sPathDist = sPath[column].distance;
                if (sPathDist != INFINITY) {
                    if (startToFringe > sPathDist) {
                        sPath[column].parentVert = currentVert;
                        sPath[column].distance = startToFringe;
                    }
                }
                else {
                    sPath[column].parentVert = currentVert;
                    sPath[column].distance = startToFringe;
                }
            }
            column++;
        }
    }

    public void displayPaths(boolean minWeight) {
        int index = weightQueue.remove();
        if (minWeight) {
            System.out.println("Min route voor " + vertexList[index].plaats.getPlaatsNaam() + ":");
        } else {
            System.out.println("Max route voor " + vertexList[index].plaats.getPlaatsNaam() + ":");
        }
        while (!weightQueue.isEmpty()) {
            index = weightQueue.remove();
            System.out.print(vertexList[index].plaats.getPlaatsNaam() + " (Bedrag: ");
            if (sPath[index].distance == INFINITY) {
                System.out.print("0)");
            } else {
                System.out.print(sPath[index].distance + ")");
            }
            if (!weightQueue.isEmpty()) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        }
//        for (int j = 0; j < nVerts; j++) {
//            System.out.println(vertexList[j].plaats + " = ");
//
//            if (sPath[j].distance == INFINITY) {
//                System.out.println("inf");
//            } else {
//                System.out.println(sPath[j].distance);
//            }
//            String parent = vertexList[sPath[j].parentVert].plaats;
//            System.out.println("(" + parent + ")");
//        }
//        System.out.println();

    public void displayVertex(int v) {
        System.out.println(vertexList[v].plaats.getPlaatsNaam());
    }

    public void dfs(int start, String plaats) {
        boolean found = false;
        int searchTerm = 0;

        String fullSearch = "";
        if (!plaats.isEmpty()) {
            searchTerm++;
            fullSearch += "naam \"" + plaats + "\"";
        }

        vertexList[start].wasVisited = true;
//        displayVertex(start);
        theStack.push(start);
        while (!theStack.isEmpty()) {
            int v = getAdjUnvisitedVertex(theStack.peek());
            if (v == -1) {
                theStack.pop();
            } else {
                vertexList[v].wasVisited = true;
                int foundTerm = 0;
                if (vertexList[v].plaats.getPlaatsNaam().replace(" ", "").equalsIgnoreCase(plaats.replace(" ", "")) || vertexList[v].plaats.getPlaatsNaam().replace(" ", "").equalsIgnoreCase(plaats.replace(" ", "")) || vertexList[v].plaats.getPlaatsNaam().replace(" ", "").equalsIgnoreCase(plaats.replace(" ", ""))) {
                    foundTerm++;
                }

                if (foundTerm == searchTerm) {
                    if (!found) {
                        if (searchTerm != 0) {
                            System.out.println("Plaatsen gevonden met " + fullSearch + " erna " + vertexList[start].plaats.getPlaatsNaam() + ":");
                        } else {
                            System.out.println("plaatsen gevonden erna " + vertexList[start].plaats.getPlaatsNaam() + ":");
                        }
                    }
                    found = true;
                    displayVertex(v);
                }
                theStack.push(v);
            }
        }
        if (!found) {
            if (searchTerm != 0) {
                System.out.println("Geen plaatsen gevonden met  " + fullSearch + " erna " + vertexList[start].plaats.getPlaatsNaam() + ":");
            } else {
                System.out.println("Geen plaatsen gevonden na het zoeken van " + vertexList[start].plaats.getPlaatsNaam());
            }
        }
        for (int j = 0; j < nVerts; j++) {
            vertexList[j].wasVisited = false;
        }
    }

    public void bfs(int start, int level) {
        boolean found = false;
        vertexList[start].wasVisited = true;
//        displayVertex(start);
        theQueue.insert(start);
        int lastVisited = start;
        int lastBreath = start;
        int breath = 0;
        int v2;
        while (!theQueue.isEmpty()) {
            int v1 = theQueue.remove();
            while ((v2 = getAdjUnvisitedVertex(v1)) != -1) {
                vertexList[v2].wasVisited = true;
                if (breath == level) {
                    if (!found) {
                        System.out.println("Plaatsen gevonden met " + String.valueOf(level) + " ... van " + vertexList[start].plaats.getPlaatsNaam() + ":");
                    }
                    found = true;
                    displayVertex(v2);
                }
                theQueue.insert(v2);
                lastVisited = v2;
            }
            if (v1 == lastBreath) {
                lastBreath = lastVisited;
                if (breath == level) {
                    break;
                } else {
                    breath++;
                }
            }
        }
        if (!found) {
            System.out.println("Geen plaatsen gevonden met " + String.valueOf(level) + " ... van " + vertexList[start].plaats.getPlaatsNaam());
        }
        for (int j = 0; j < nVerts; j++) {
            vertexList[j].wasVisited = false;
        }
    }

    public int getAdjUnvisitedVertex(int v) {
        for (int j=0; j<nVerts; j++) {
            if (adjMat[v][j] != INFINITY && vertexList[j].wasVisited == false) {
                return  j;
            }
        }
        return -1;
    }

}