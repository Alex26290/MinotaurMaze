package fordfulcerson;

import minotaurMaze.Coord;

import java.util.Objects;

/**
 * The edge class that is used for DirectedGraph
 */
public class Edge {

    private final Coord target;
    private final Coord start;
    private int capacity;

    public Edge(Coord start, Coord target, int capacity) {
        this.start = start;
        this.target = target;
        this.capacity = capacity;
    }

    public Coord getTarget() {
        return target;
    }

    public Coord getStart() {
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(target, edge.target) && Objects.equals(start, edge.start);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, start);
    }

    @Override
    public String toString() {
        return this.start + "->" + this.target;
    }
}