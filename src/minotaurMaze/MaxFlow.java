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
public class MaxFlow {
    static int maxFlow = 0;
    Maze maze = new Maze();

    public static List<Edge> passedPathes;
//    public static List<Coord> visited = new ArrayList<>();
//    public static List<Coord> visitedCoords = new ArrayList<>();

    public MaxFlow(int cols, int rows) {
        Ranges.setSize(new Coord(cols, rows));
    }

    public void startNewMaze() {
        maze.start();
        startMaze();
    }

    public void startMaze() {
        System.out.println("Построен новый лабиринт");
        Graph g = maze.createGraphFromCoords();
        if (g.getIstok() != null) {
            if (g.getIstok().getCoord().isExtreme) {
                System.out.println("Поток = 0, минотавр находится у края лабиринта");
            } else {
                getMaxFlowSize(g);
            }
        } else {
            System.out.println("Не установлена иконка минотавра");
        }
    }

    public void getMaxFlowSize(Graph g) {

        //Поиск потока в виде связанной хеш-таблицы, содержащей грани как ключи
        LinkedHashMap<Edge, Integer> flow = getMaxFlow(g, g.getIstok().getCoord(), g.getStok().getCoord());
        List<Coord> visited = new ArrayList<>();
        List<Edge> list = g.getEdges();
        for (Map.Entry entry : flow.entrySet()) {
            Edge edge = (Edge) entry.getKey();
            Coord start = edge.getStart();
            Coord target = edge.getTarget();
            g.getNode(start.toString()).removeEdge(edge);
            if (passedPathes.contains(edge)) {
                list.remove(edge);
                visited.add(target);
            }
        }
        for (Map.Entry entry : flow.entrySet()) {
            Edge edge = (Edge) entry.getKey();
            Coord start = edge.getStart();
            if (visited.contains(start)) {
                list.remove(edge);
                g.getNode(start.toString()).removeEdge(edge);
            }
        }
        for (int i = 0; i < 2; i++) {
            List<Edge> path = new LinkedList<>();
            Coord start = null;
            for (Edge e : g.getEdges()) {
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
            for (Edge e : path) {
                list.remove(e);
                g.getNode(start.toString()).removeEdge(e);
            }
        }
        System.out.println("Поток = " + maxFlow);
        maxFlow = 0;
    }

    public Box getBox(Coord coord) {
        return maze.get(coord);
    }

    public void pressLeftButton(Coord coord) {
        maze.setOpenedToBox(coord);
    }

    public void pressRightButton(Coord coord) {
        maze.setMinotaurToBox(coord);
    }


    static LinkedHashMap<Edge, Integer> getMaxFlow(Graph g, Coord source,
                                                   Coord stok) {
        //Путь от истока к стоку, найденный для каждой итерации
        LinkedList<Edge> path;
        passedPathes = new LinkedList<>();
        // Поток, то есть ёмкость каждой грани, которая была использована
        LinkedHashMap<Edge, Integer> flow = new LinkedHashMap<Edge, Integer>();
        // Создаём начальный пустой поток
        for (Edge e : g.getEdges()) {
            flow.put(e, 0);
        }

        // Основной алгоритм нахождения максимального потока
        while ((path = bfs(g, source, stok, flow)) != null) {
            maxFlow++;
            passedPathes.addAll(path);
        //path - новый найденный путь от истока к стоку
            int minCapacity = Integer.MAX_VALUE;
            Object lastNode = source;
            for (Edge edge : path) {
                int c;
                // Грани могут быть использованы в обоих направлениях
                // поэтому добавляем это условие для того, чтобы найти действительное направление грани
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
            //Изменяем поток всех граней пути, в оответствии с вычисленным значением(см. выше)
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
        return flow;
    }

//нахождение конкретного пути, не пересекающегося с ранее найденными
    static LinkedList<Edge> bfs(Graph g, Coord start, Coord target,
                                HashMap<Edge, Integer> flow) {
        // Грань, по которой была достигнута вершина нрафа
        HashMap<Coord, Edge> parent = new HashMap<Coord, Edge>();
        // Все внешние вершины для текущей итерафии поиска
        LinkedList<Coord> path = new LinkedList<>();
        parent.put(start, null);
        path.add(start);
        // Алгоритм нахождения пути
        //метка, к которой возвращаемся в случае , если найден сток
        all:
        while (!path.isEmpty()) {
            // Эта переменная нужна, чтобы предотвратить concurrent modification exception у JVM
            LinkedList<Coord> newPath = new LinkedList<>();
            // Итерация по всем вершинам в переменной ringe
            for (Coord coord : path) {
//                Node node = g.getNode(nodeID);
                Node node = g.getNode(coord.toString());
                // Iterate through all the edges of the node.
                for (int i = 0; i < node.getOutLeadingOrder(); i++) {
                    Edge e = node.getEdge(i);
                //Выходим из цикла, если достигнут сток
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
                        newPath.add(e.getTarget());
                    } else if (e.getTarget().equals(coord)
                            && !parent.containsKey(e.getStart())
                            && flow.get(e) > 0) {
                        parent.put(e.getStart(), e);
                        if (e.getStart().equals(target)) {
                            break all;
                        }
                        newPath.add(e.getStart());
                    }
                }
            }
            // Заменяем путь на новый
            path = newPath;
        }

        // Возвращаем null, если путь пуст
        if (path.isEmpty()) {
            return null;
        }
        // If a path was found, reconstruct it.
        Coord node = target;
        LinkedList<Edge> fpath = new LinkedList<Edge>();
        while (!node.equals(start)) {
            Edge e = parent.get(node);
            fpath.addFirst(e);
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
        return fpath;
    }
}
