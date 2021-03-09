package fordfulcerson;

import minotaurMaze.Coord;

import java.util.ArrayList;

/**
 * The node class that is used for DirectedGraph
 */
public class Node {
    private Coord coord;
//У вершины графа есть список ребёр, которые отходят от вершины.
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Edge> connectedNodes = new ArrayList<>();

    public Node(){

    }
    public Node(Coord coord){
        this.coord = coord;
    }
//обавляем ребро к вершине графа
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

//Получить ребро по числовому значению
public Edge getEdge(int number) {
            if (this.edges.size() <= number) {
            return null;
        } else {
            return this.edges.get(number);
        }
    }
//Возврщает размер списка ребёр для вершины
public int getOutLeadingOrder() {
        return this.edges.size();
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

}