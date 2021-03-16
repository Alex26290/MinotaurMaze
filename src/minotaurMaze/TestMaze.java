/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

import fordfulcerson.Edge;
import fordfulcerson.Graph;
import fordfulcerson.Node;

import java.util.*;

/**
 * @author DNS
 */
public class TestMaze {
    static int maxFlow = 0;
    Cell cell = new Cell();

    public static List<Edge> passedPathes;
    public static List<Coord> visited = new ArrayList<>();
    public static List<Coord> visitedCoords = new ArrayList<>();

    public TestMaze(int cols, int rows) {
        Ranges.setSize(new Coord(cols, rows));
    }

    public void startNewMaze() {
        System.out.println("Построен новый лабиринт");
        cell.start();
        Graph g = cell.createGraphFromCoords();

        Map<String, Node> map = g.getNodes();
        for (Map.Entry entry : map.entrySet()) {
            Node node = (Node) entry.getValue();
            System.out.println("Координата вершины - " + node.getCoord() + " outputleadingOrder = " + node.getOutLeadingOrder());
        }
        if (g.getIstok().getCoord().isExtreme) {
            System.out.println("Поток = 0, минотавр находится у края лабиринта");
            maxFlow = 0;
        } else {
            LinkedHashMap<Edge, Integer> flow = getMaxFlow(g, g.getIstok().getCoord(), g.getStok().getCoord());
            System.out.println("Поток1 = " + maxFlow);
            List<Coord> visited = new ArrayList<>();
            List<Edge> list = g.getEdges();
            for (Map.Entry entry : g.getNodes().entrySet()) {
                Node node = (Node) entry.getValue();
                System.out.println("Координата = " + node.getCoord() + " ,outLeadOrder = " + node.getOutLeadingOrder());
            }
            for (Map.Entry entry : flow.entrySet()) {
                Edge edge = (Edge) entry.getKey();
                Coord start = edge.getStart();
                Coord target = edge.getTarget();
                g.getNode(start.toString()).removeEdge(edge);
//                g.getNode(target.toString()).removeEdge(edge);
                if (passedPathes.contains(edge)) {
                    list.remove(edge);
                    visited.add(target);
                }
            }
            for (Map.Entry entry : flow.entrySet()) {
                Edge edge = (Edge) entry.getKey();
                Coord start = edge.getStart();
                Coord target = edge.getTarget();
                if (visited.contains(start)) {
                    list.remove(edge);
                    g.getNode(start.toString()).removeEdge(edge);
//                    g.getNode(target.toString()).removeEdge(edge);
                }
            }
            for (int i = 0; i < 2; i++) {
                List<Edge> path = new LinkedList<>();
                Coord start = null;
                for (Edge e : g.getEdges()) {
                    System.out.println("Грань - " + e);
                    if (path.size() == 0) {
                        if (e.getStart().equals(g.getIstok().getCoord())) {
                            start = e.getTarget();
                            path.add(e);
                        }
                    } else {
                        if (e.getStart().equals(start)) {
                            start = e.getTarget();
                            path.add(e);
                            if (e.getTarget().equals(g.getStok().getCoord())) {
                                maxFlow++;
                                break;
                            }
                        }
                    }
                }
                System.out.println("path = " + path);
                for (Edge e : path) {
                    list.remove(e);
                    g.getNode(start.toString()).removeEdge(e);
                }
            }
            System.out.println("Поток2 = " + maxFlow);
            maxFlow = 0;
        }
    }

    public void startMaze() {
        System.out.println("Построен новый лабиринт");
        Graph g = cell.createGraphFromCoords();

        if (g.getIstok().getCoord().isExtreme) {
            System.out.println("Поток = 0, минотавр находится у края лабиринта");
        } else {
            LinkedHashMap<Edge, Integer> flow = getMaxFlow(g, g.getIstok().getCoord(), g.getStok().getCoord());
            System.out.println("Поток1 = " + maxFlow);
            List<Coord> visited = new ArrayList<>();
            List<Edge> list = g.getEdges();
            for (Map.Entry entry : g.getNodes().entrySet()) {
                Node node = (Node) entry.getValue();
                System.out.println("Координата = " + node.getCoord() + " ,outLeadOrder = " + node.getOutLeadingOrder());
            }
            for (Map.Entry entry : flow.entrySet()) {
                Edge edge = (Edge) entry.getKey();
                Coord start = edge.getStart();
                Coord target = edge.getTarget();
                g.getNode(start.toString()).removeEdge(edge);
//                g.getNode(target.toString()).removeEdge(edge);
                if (passedPathes.contains(edge)) {
                    list.remove(edge);
                    visited.add(target);
                }
            }
            for (Map.Entry entry : flow.entrySet()) {
                Edge edge = (Edge) entry.getKey();
                Coord start = edge.getStart();
                Coord target = edge.getTarget();
                if (visited.contains(start)) {
                    list.remove(edge);
                    g.getNode(start.toString()).removeEdge(edge);
//                    g.getNode(target.toString()).removeEdge(edge);
                }
            }
            for (int i = 0; i < 2; i++) {
                List<Edge> path = new LinkedList<>();
                Coord start = null;
                for (Edge e : g.getEdges()) {
                    System.out.println("Грань - " + e);
                    if (path.size() == 0) {
                        if (e.getStart().equals(g.getIstok().getCoord())) {
                            start = e.getTarget();
                            path.add(e);
                        }
                    } else {
                        if (e.getStart().equals(start)) {
                            start = e.getTarget();
                            path.add(e);
                            if (e.getTarget().equals(g.getStok().getCoord())) {
                                maxFlow++;
                                break;
                            }
                        }
                    }
                }
                System.out.println("path = " + path);
                for (Edge e : path) {
                    list.remove(e);
                    g.getNode(start.toString()).removeEdge(e);
                }
            }
            System.out.println("Поток2 = " + maxFlow);
            maxFlow = 0;
        }
    }

    public Box getBox(Coord coord) {
        return cell.get(coord);
    }

    public void pressLeftButton(Coord coord) {
        cell.setOpenedToBox(coord);
    }

    public void pressRightButton(Coord coord) {
        cell.setMinotaurToBox(coord);
    }


    static LinkedHashMap<Edge, Integer> getMaxFlow(Graph g, Coord source,
                                                   Coord sink) {
        // The path from source to sink that is found in each iteration
        LinkedList<Edge> path;
        passedPathes = new LinkedList<>();
        // The flow, i.e. the capacity of each edge that is actually used
        LinkedHashMap<Edge, Integer> flow = new LinkedHashMap<Edge, Integer>();
        // Create initial empty flow.
        for (Edge e : g.getEdges()) {
            flow.put(e, 0);
        }

        // The Algorithm itself
        System.out.println("flow = " + flow);
        while ((path = bfs(g, source, sink, flow)) != null) {
            maxFlow++;
            System.out.println("path = " + path);
            passedPathes.addAll(path);
            // Activating this output will illustrate how the algorithm works
            // System.out.println(path);
            // Find out the flow that can be sent on the found path.
            int minCapacity = Integer.MAX_VALUE;
            Object lastNode = source;
            for (Edge edge : path) {
                int c;
                // Although the edges are directed they can be used in both
                // directions if the capacity is partially used, so this if
                // statement is necessary to find out the edge's actual
                // direction.
                if (edge.getStart().equals(lastNode)) {
                    c = edge.getCapacity() - flow.get(edge);
                    lastNode = edge.getTarget();
                } else {
                    c = flow.get(edge);
                    lastNode = edge.getStart();
                }
                if (c < minCapacity) {
                    minCapacity = c;
                }
            }

            // Change flow of all edges of the path by the value calculated
            // above.
            lastNode = source;
            for (Edge edge : path) {
                // If statement like above
                if (edge.getStart().equals(lastNode)) {
                    flow.put(edge, flow.get(edge) + minCapacity);
                    lastNode = edge.getTarget();
                } else {
                    flow.put(edge, flow.get(edge) - minCapacity);
                    lastNode = edge.getStart();
                }
            }
        }
        return flow;
    }


    static LinkedList<Edge> bfs(Graph g, Coord start, Coord target,
                                HashMap<Edge, Integer> flow) {
        // The edge by which a node was reached.
        HashMap<Coord, Edge> parent = new HashMap<Coord, Edge>();
        // All outer nodes of the current search iteration.
        LinkedList<Coord> fringe = new LinkedList<>();
        // We need to put the start node into those two.
        parent.put(start, null);
        fringe.add(start);
        // The actual algorithm
        all:
        while (!fringe.isEmpty()) {
            // This variable is needed to prevent the JVM from having a
            // concurrent modification
            LinkedList<Coord> newFringe = new LinkedList<>();
            // Iterate through all nodes in the fringe.
            for (Coord coord : fringe) {
//                Node node = g.getNode(nodeID);
                Node node = g.getNode(coord.toString());
                // Iterate through all the edges of the node.
                for (int i = 0; i < node.getOutLeadingOrder(); i++) {
                    Edge e = node.getEdge(i);
                    // Only add the node if the flow can be changed in an out
                    // leading direction. Also break, if the target is reached.
                    if (flow.get(e) == null) {
                        break all;
                    }
                    if (e.getStart().equals(coord)
                            && !parent.containsKey(e.getTarget())
                            && flow.get(e) < e.getCapacity()) {
                        parent.put(e.getTarget(), e);
                        if (e.getTarget().equals(target)) {
                            break all;
                        }
                        newFringe.add(e.getTarget());
                    } else if (e.getTarget().equals(coord)
                            && !parent.containsKey(e.getStart())
                            && flow.get(e) > 0) {
                        parent.put(e.getStart(), e);
                        if (e.getStart().equals(target)) {
                            break all;
                        }
                        newFringe.add(e.getStart());
                    }
                }
            }
            // Replace the fringe by the new one.
            fringe = newFringe;
        }

        // Return null, if no path was found.
        if (fringe.isEmpty()) {
            return null;
        }
        // If a path was found, reconstruct it.
        Coord node = target;
        LinkedList<Edge> path = new LinkedList<Edge>();
        while (!node.equals(start)) {
            Edge e = parent.get(node);
            path.addFirst(e);
            if (e != null) {
                if (e.getStart() != null) {
                    if (e.getStart().equals(node)) {
                        node = e.getTarget();
                    } else {
                        node = e.getStart();
                    }
                }
            } else {
                return null;
            }
        }
        return path;
    }
}
