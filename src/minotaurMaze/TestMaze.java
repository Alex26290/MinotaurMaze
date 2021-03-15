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
public class TestMaze{
    Cell cell = new Cell();

    public static List<Coord> visited = new ArrayList<>();
    public static List<Coord> visitedCoords = new ArrayList<>();

    public TestMaze(int cols, int rows) {
        Ranges.setSize(new Coord(cols, rows));
    }

    public void startNewMaze() {
        System.out.println("Построен новый лабиринт");
        cell.start();
        Graph g = cell.createGraphFromCoords();

        if (g.getIstok().getCoord().isExtreme) {
            System.out.println("Поток = 0, минотавр находится у края лабиринта");
        } else {
            LinkedHashMap<Edge, Integer> flow = getMaxFlow(g, g.getIstok().getCoord(), g.getStok().getCoord());
            System.out.println("Поток = " + getFlowSize(flow, g, g.getIstok().getCoord()));
        }
    }
    public void startMaze() {
        System.out.println("Построен новый лабиринт");
        Graph g = cell.createGraphFromCoords();

        if (g.getIstok().getCoord().isExtreme) {
            System.out.println("Поток = 0, минотавр находится у края лабиринта");
        } else {
            LinkedHashMap<Edge, Integer> flow = getMaxFlow(g, g.getIstok().getCoord(), g.getStok().getCoord());
            System.out.println("Поток = " + getFlowSize(flow, g, g.getIstok().getCoord()));
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
        // The flow, i.e. the capacity of each edge that is actually used
        HashMap<Edge, Integer> flow = new LinkedHashMap<Edge, Integer>();
        // Create initial empty flow.
        for (Edge e : g.getEdges()) {
            if (e.getCapacity() == 1) {
                flow.put(e, 1);
            }
        }

        // Алгоритм нахождения максимального потока
        while ((path = bfs(g, source, sink, flow)) != null) {
            System.out.println("path = " + path);
            // Нахождение потока
            int minCapacity = Integer.MAX_VALUE;
            Coord lastNode = source;
            for (Edge edge : path) {
                int c = 0;
//Условие для нахождения действительного направления граней
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

//Изменяем поток всех граней пути в соответствии со значением, рассчитанным выше
            lastNode = source;
            for (Edge edge : path) {
                if (edge.getStart().equals(lastNode)) {
                    flow.put(edge, flow.get(edge) + minCapacity);
                    lastNode = edge.getTarget();
                } else {
                    flow.put(edge, flow.get(edge) - minCapacity);
                    lastNode = edge.getStart();
                }
            }
        }
        return (LinkedHashMap<Edge, Integer>) flow;
    }

    static int getFlowSize(LinkedHashMap<Edge, Integer> flow, Graph g,
                            Coord source) {
        HashMap<Coord, Integer> currentFlow = new HashMap<>();
        Node sourceNode = g.getNode(source.toString());

        List<LinkedList<Edge>> routes = new LinkedList<>();
        LinkedList<Edge> currentRoute = new LinkedList<>();
        LinkedList<Edge> usedEdges = new LinkedList<>();
        LinkedList<Edge> unUsedEdges = new LinkedList<>();
        int maximumFlow = 0;
        Edge currentEdge = null;
        for (Map.Entry entry : flow.entrySet()) {
            Edge nextEdge = (Edge) entry.getKey();
            usedEdges.add(nextEdge);
        }
        currentRoute = new LinkedList<>();
        Coord currentCoord;
        for (Coord coord : Ranges.getCoordsAround(source)) {
            if (Cell.cellMap.get(coord) == Box.OPENED) {
                currentCoord = coord;
                for (int i = 0; i < usedEdges.size(); i++) {
                    currentEdge = usedEdges.get(i);
                    if (!currentEdge.getTarget().equals(g.getStok().getCoord())) {
                        if (currentCoord.equals(currentEdge.getStart())) {
                            currentRoute.add(currentEdge);
                            currentRoute.add(currentEdge);
                            currentCoord = currentEdge.getTarget();
                            currentRoute = new LinkedList<>();
                            routes.add(currentRoute);
                        }
                    } else if (currentEdge.getTarget().equals(g.getStok().getCoord())) {
                        if (currentCoord.equals(currentEdge.getStart())) {
                            currentCoord = currentEdge.getTarget();
                            currentRoute.add(currentEdge);
                            System.out.println("test2");
                            routes.add(currentRoute);
                            currentRoute = new LinkedList<>();
                        }
                    }
                }
            }
        }
        maximumFlow = routes.size();
//        int crosses = checkRoutes(routes);
        for (LinkedList<Edge> list : routes) {
            for (Edge edge : list) {
                System.out.println(edge);
            }
            System.out.println("\n");
        }
        if (maximumFlow > sourceNode.getOutLeadingOrder()) {
            maximumFlow = sourceNode.getOutLeadingOrder();
        }
        return maximumFlow;
    }

    private static boolean hasNextEdge(Coord start, LinkedList<Edge> usedEdges) {
        boolean hasNextEdge = false;
        for (Edge edge : usedEdges) {
            if (edge.getStart().equals(start)) {
                hasNextEdge = true;
            }
        }
        return hasNextEdge;
    }

    private static int checkRoutes(List<LinkedList<Edge>> routes) {
        int countOfCrosses = 0;
        boolean ifFound = false;
        for (int i = 0; i < routes.size() - 1; i++) {
            LinkedList<Edge> currentRoute = routes.get(i);
            System.out.println("Список граней = " + currentRoute);
            LinkedList<Edge> nextRoute = routes.get(i + 1);
            System.out.println("Список граней 2 = " + nextRoute);
            for (int k = 0; k < currentRoute.size(); k++) {
                if (ifFound) {
                    ifFound = false;
                    break;
                }
                System.out.println("currentRoute.get() = " + currentRoute.get(k).toString());
                Edge current = currentRoute.get(k);
                for (int n = 0; n < nextRoute.size(); n++) {
                    Edge next = nextRoute.get(n);
                    System.out.println("nextRoute.get(n) = " + nextRoute.get(n).toString());
                    System.out.println(current.toString().equalsIgnoreCase(next.toString()));
                    if (current.toString().equalsIgnoreCase(next.toString())) {
                        countOfCrosses++;
                        ifFound = true;
                        break;
                    }
                }
            }
        }
        System.out.println("Количество пересечений = " + countOfCrosses);
        return countOfCrosses;
    }

    static LinkedList<Edge> bfs(Graph g, Coord start, Coord target,
                                HashMap<Edge, Integer> flow) {
        // The edge by which a node was reached.
        HashMap<Coord, Edge> parent = new HashMap<>();
        // All outer nodes of the current search iteration.
        LinkedList<Coord> fringe = new LinkedList<>();
        // We need to put the start node into those two.
        parent.put(start, null);
        fringe.add(start);
        // The actual algorithm
        all:
        while (!fringe.isEmpty()) {
            System.out.println("fringe не пустой, в нём есть нод истока");
            // This variable is needed to prevent the JVM from having a
            // concurrent modification
            LinkedList<Coord> newFringe = new LinkedList<>();
            // Iterate through all nodes in the fringe.
            System.out.println("fringe = " + fringe);
            for (Coord coord : fringe) {
                System.out.println("В цикле по координатам");
                System.out.println("\n");
                System.out.println("Получаем нод из fringe - " + g.getNode(coord.toString()));
                Node node = g.getNode(coord.toString());
                // Iterate through all the edges of the node.
                System.out.println("Количество граней для нода = " + node.getOutLeadingOrder());
                for (int i = 0; i < node.getOutLeadingOrder(); i++) {
                    Edge e = node.getEdge(i);
                    // Only add the node if the flow can be changed in an out
                    // leading direction. Also break, if the target is reached.
                    if (e.getStart().equals(coord)
                            && !parent.containsKey(e.getTarget())
                            && !(visited.contains(e.getStart()) || visited.contains(e.getTarget()))
                            && flow.get(e) < e.getCapacity()) {
                        System.out.println("Test Point 3");
                        parent.put(e.getStart(), e);
                        parent.put(e.getTarget(), e);
                        if (e.getTarget().equals(target)) {
                            System.out.println("Найден сток");
                            break all;
                        }
                        System.out.println("Заменяем стартовый нод на таргет из грани");
                        newFringe.add(e.getTarget());
                        visited.add(e.getTarget());
                    } else if (e.getTarget().equals(coord)
                            && !parent.containsKey(e.getStart())
                            && !(visited.contains(e.getStart()) || visited.contains(e.getTarget()))
                            && flow.get(e) > 0) {
                        parent.put(e.getStart(), e);
                        parent.put(e.getTarget(), e);
                        if (e.getStart().equals(target)) {
                            System.out.println("Найден сток");
                            break all;
                        }
                        newFringe.add(e.getStart());
                        visited.add(e.getTarget());
                    }
                }
            }
            // Replace the fringe by the new one.
            fringe = newFringe;
        }
        System.out.println("\n");
        System.out.println("fringe пуст? - " + fringe.isEmpty());
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
            if (e.getStart().equals(node)) {
                node = e.getTarget();
            } else {
                node = e.getStart();
            }
        }
        // Возвращаем путь
        return path;
    }
}
