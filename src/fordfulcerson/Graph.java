package fordfulcerson;

import minotaurMaze.Coord;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes = new HashMap<>();
    private Node istok;
    private Node stok;
    private LinkedList<Edge> edges = new LinkedList<>();


    public void addNode(Coord coord) {
        nodes.put(coord.toString(), new Node(coord));
    }

    public Edge addEdge(Coord istok, Coord stok, int capacity) {
        Edge edge = new Edge(istok, stok, capacity);
        System.out.println(edge);
        if (!this.edges.contains(edge)) {
            this.edges.add(edge);
        }
        return edge;
    }

    public Map<String, Node> getNodes() {
        return this.nodes;
    }

    public Node getIstok() {
        return istok;
    }

    public void setIstok(Node istok) {
        this.istok = istok;
    }

    public Node getStok() {
        return stok;
    }

    public void setStok(Node stok) {
        this.stok = stok;
    }

    public Node getNode(String coord) {
        return this.nodes.get(coord);
    }

    public LinkedList<Edge> getEdges() {
        return this.edges;
    }

}
