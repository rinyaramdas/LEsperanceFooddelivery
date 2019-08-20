package sr.unasat.ad.graph;


import sr.unasat.ad.entities.Plaats;
import sr.unasat.ad.queue.QueueB;
import sr.unasat.ad.stack.StackA;

import java.util.Stack;


public class WeightedGraph {
    private final int MAX_VERTS = 20;
    private final int INFINITY = -1000000;
    private Vertex vertexList[];
    private int adjMat[][];
    private int nVerts;
    private int nTree;
    private DistPar sPath[];
    private int currentVert;
    private int startToCurrent;
    private StackA theStack;
    private QueueB theQueue;
    private QueueB weightQueue;

    public WeightedGraph() {
        vertexList = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        nTree = 0;
        for (int j = 0; j < MAX_VERTS; j++) {
            for (int k = 0; k < MAX_VERTS; k++) {
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

    public void path(int start, boolean maxWeight) {
        int startTree = start;
        vertexList[startTree].isInTree = true;
        weightQueue.insert(startTree);
        int weight;
        if (maxWeight) {
            weight = INFINITY;
        } else {
            weight = -INFINITY;
        }
        nTree = 1;
        for (int j = 0; j < nVerts; j++) {
            int tempDist = adjMat[startTree][j];
            sPath[j] = new DistPar(startTree, tempDist);
        }
        while (nTree < nVerts) {
            int index;
            if (maxWeight) {
                index = getMax();
            } else {
                index = getMin();
            }
            int dist = sPath[index].distance;
            if (maxWeight) {
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
                System.out.println("There are unreachable traders");
                break;
            } else {
                currentVert = index;
                startToCurrent = sPath[index].distance;
            }
            vertexList[currentVert].isInTree = true;
            nTree++;
            if (maxWeight) {
                adjust_sPath_Max();
            } else {
                adjust_sPath_Min();
            }
        }
        displayPaths(maxWeight);
        nTree = 0;
        for (int j = 0; j < nVerts; j++) {
            vertexList[j].isInTree = false;
        }
    }

    public int getMax() {
        int maxDist = INFINITY;
        int indexMax = 0;
        for (int j = 1; j < nVerts; j++) {
            if (!vertexList[j].isInTree && sPath[j].distance > maxDist) {
                maxDist = sPath[j].distance;
                indexMax = j;
            }
        }
        return indexMax;
    }

    public int getMin() {
        int minDist = -INFINITY;
        int indexMin = 0;
        for (int j = 1; j < nVerts; j++) {
            if (!vertexList[j].isInTree && sPath[j].distance != INFINITY && sPath[j].distance < minDist) {
                minDist = sPath[j].distance;
                indexMin = j;
            }
        }
        return indexMin;
    }

    public void adjust_sPath_Max() {
        int column = 1;
        while (column < nVerts) {
            if (vertexList[column].isInTree) {
                column++;
                continue;
            }
            int currentToFringe = adjMat[currentVert][column];
            int startToFringe = startToCurrent + currentToFringe;
            int sPathDist = sPath[column].distance;
            if (startToFringe > sPathDist) {
                sPath[column].parentVert = currentVert;
                sPath[column].distance = startToFringe;
            }
            column++;
        }
    }

    public void adjust_sPath_Min() {
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
                    if (startToFringe < sPathDist) {
                        sPath[column].parentVert = currentVert;
                        sPath[column].distance = startToFringe;
                    }
                } else {
                    sPath[column].parentVert = currentVert;
                    sPath[column].distance = startToFringe;
                }
            }
            column++;
        }
    }

    public void displayPaths(boolean maxWeight) {
//        for (int j = 0; j < nVerts; j++) {
//            System.out.print(vertexList[j].trader.getName() + " (Profit: ");
//            if (sPath[j].distance == INFINITY) {
//                System.out.print("0");
//            } else {
//                System.out.print(sPath[j].distance);
//            }
//            String parent = vertexList[sPath[j].parentVert].trader.getName();
//            System.out.println(" - After trade with " + parent + ")");
//        }
//        System.out.println();
        int index = weightQueue.remove();
        if (maxWeight) {
            System.out.println("Max prijs " + vertexList[index].plaats.getPlaatsNaam() + ":");
        } else {
            System.out.println("Min prijs " + vertexList[index].plaats.getPlaatsNaam() + ":");
        }
        while (!weightQueue.isEmpty()) {
            index = weightQueue.remove();
            System.out.print(vertexList[index].plaats.getPlaatsNaam() + " (Prijs: ");
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

    public void displayVertex(int v) {
        System.out.println(vertexList[v].plaats.getPlaatsNaam());
    }

    public void dfs(int start, String plaats) {
        boolean found = false;
        int searchTerm = 0;
        String fullSearch = "";
        if (!plaats.isEmpty()) {
            searchTerm++;
            fullSearch += "plaats \"" + plaats + "\"";
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
                if (vertexList[v].plaats.getPlaatsNaam().contains(plaats)) {
                    foundTerm++;
                }
                if (foundTerm == searchTerm) {
                    if (!found) {
                        if (searchTerm != 0) {
                            System.out.println("Trader(s) found with " + fullSearch + " after " + vertexList[start].plaats.getPlaatsNaam() + ":");
                        } else {
                            System.out.println("Trader(s) found after " + vertexList[start].plaats.getPlaatsNaam() + ":");
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
                System.out.println("No trader(s) found with " + fullSearch + " after " + vertexList[start].plaats.getPlaatsNaam() + ":");
            } else {
                System.out.println("No trader(s) found searching after " + vertexList[start].plaats.getPlaatsNaam());
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
                        System.out.println("Trader(s) found with " + String.valueOf(level) + " middleman(s) from " + vertexList[start].plaats.getPlaatsNaam() + ":");
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
            System.out.println("No traders found with " + String.valueOf(level) + " middleman(s) from " + vertexList[start].plaats.getPlaatsNaam());
        }
        for (int j = 0; j < nVerts; j++) {
            vertexList[j].wasVisited = false;
        }
    }

    public int getAdjUnvisitedVertex(int v) {
        for (int j = 0; j < nVerts; j++) {
            if (adjMat[v][j] != INFINITY && vertexList[j].wasVisited == false) {
                return  j;
            }
        }
        return -1;
    }

}