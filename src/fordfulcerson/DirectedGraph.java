package fordfulcerson;

import minotaurMaze.Coord;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is represents a directed graph for the maximum flow calculation
 */
public class DirectedGraph {
    private HashMap<Coord, Node> nodes = new HashMap<>();
    private LinkedList<Edge> edges = new LinkedList<>();

    /**
     * Use this method to build the graph. It will add an edge to the graph and
     * also its nodes, if necessary. The node identifiers can be any object. Two
     * objects identify the same node, if they are equal according to their
     * equals function.
     */
    void addEdge(Coord istok, Coord stok, int capacity) {
        Node startNode;
        Node endNode;
        if (!this.nodes.containsKey(istok)) {
            startNode = new Node();
            this.nodes.put(istok, startNode);
        } else {
            startNode = this.nodes.get(istok);
        }
        if (!this.nodes.containsKey(stok)) {
            endNode = new Node();
            this.nodes.put(stok, endNode);
        } else {
            endNode = this.nodes.get(stok);
        }
//        Edge edge = new Edge(istok, stok, capacity);
//        startNode.addEdge(edge);
//        endNode.addEdge(edge);
//        this.edges.add(edge);
    }

    public Node getNode(Object nodeID) {
        return this.nodes.get(nodeID);
    }

    LinkedList<Edge> getEdges() {
        return this.edges;
    }
}