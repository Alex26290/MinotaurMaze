package minotaurMaze;

import fordfulcerson.Edge;
import fordfulcerson.Graph;
import fordfulcerson.Node;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    public static Matrix cellMap;
    public static List<Coord> nodesInGraph;
    public static List<Coord> visitedNodes;

    void start() {
        cellMap = new Matrix(Box.OPENED);
        for (int j = 0; j < 11; j++) {
            Coord coord = Ranges.getRandomCoord();
            cellMap.set(coord, Box.CLOSED);
            if (j == 10) {
                cellMap.set(coord, Box.MINO);
            }
        }
    }

    public static Box get(Coord coord) {
        return cellMap.get(coord);
    }

    void setOpenedToBox(Coord coord) {
        if (cellMap.get(coord) == Box.OPENED || cellMap.get(coord) == Box.MINO) {
            cellMap.set(coord, Box.CLOSED);
        } else if (cellMap.get(coord) == Box.CLOSED) {
            cellMap.set(coord, Box.OPENED);
        }
    }

    public boolean isMinotaur() {
        boolean isMinotaur = false;
        List<Coord> allCoords = Ranges.getAllCoords();
        for (Coord c : allCoords) {
            if (cellMap.get(c) == Box.MINO) {
                isMinotaur = true;
            }
        }
        return isMinotaur;
    }

    void setMinotaurToBox(Coord coord) {
        if (!isMinotaur()) {
            cellMap.set(coord, Box.MINO);
        }
    }

    public Graph createGraphFromCoords() {
        Graph graph = new Graph();
        nodesInGraph = new ArrayList<>();
        visitedNodes = new ArrayList<>();
        List<Coord> coords = Ranges.getAllCoords();
        addNodesIntoGraph(graph, coords);
        addStokNodeIntoGraph(graph);
        for (Coord coord : coords) {
            if (cellMap.get(coord) == Box.MINO) {
                visitedNodes.add(coord);
                System.out.println("Найден исток, координата = " + coord);
                addNodeIntoGraph(graph, coord);
                ArrayList<Coord> coordsAround = Ranges.getCoordsAround(coord);
                if (coordsAround.size() != 4) {
                    coord.setExtreme(true);
                }
                graph.setIstok(new Node(coord));
                for (Coord coord2 : coordsAround) {
                    if (nodesInGraph.contains(coord2)) {
                        visitedNodes.add(coord2);
                        System.out.println("Добавляем ребро для истока");
                        Edge edge = graph.addEdge(coord, coord2,1);
                        graph.getNode(coord.toString()).addEdge(edge);
                    }
                }
                for (Coord coord2 : coordsAround) {
                    if (nodesInGraph.contains(coord2)) {
                        createEdgesForCoords(graph, coord2);
                    }
                }
            }
        }
        return graph;
    }

    private void addNodesIntoGraph(Graph graph, List<Coord> coords) {
        for (Coord coord : coords) {
            if (cellMap.get(coord) == Box.OPENED) {
                addNodeIntoGraph(graph, coord);
            }
        }
    }

    public void addStokNodeIntoGraph(Graph graph) {
        Coord stok = new Coord(-1, -1);
        graph.setStok(new Node(stok));
        addNodeIntoGraph(graph, stok);
    }

    public void createEdgesForCoords(Graph graph, Coord coord) {
        System.out.println("Создаём связи для нода с координатами " + coord);
        ArrayList<Coord> coordsAround = Ranges.getCoordsAround(coord);
        int coordsAroundSize = coordsAround.size();
        if (coordsAroundSize == 4) {
            System.out.println("У нода с координатой " + coord + " 4 клетки вокруг");
            for (Coord coord2 : coordsAround) {
                if (nodesInGraph.contains(coord2) && !visitedNodes.contains(coord2)) {
                    Edge edge = graph.addEdge(coord, coord2,1);
                    Node node = graph.getNode(coord.toString());
                    if (node != null) {
                        node.addEdge(edge);
                    }
                }
            }
            for (Coord coord2 : coordsAround) {
                if (nodesInGraph.contains(coord2) && !visitedNodes.contains(coord2)) {
                    visitedNodes.add(coord2);
                    createEdgesForCoords(graph, coord2);
                }
            }
        } else {
            if (nodesInGraph.contains(coord)) {
                createStokEdgeForCoord(graph, coord);
            }
        }

    }

    private void createStokEdgeForCoord(Graph graph, Coord coord) {
            System.out.println("Создаём связь от вершины к стоку");
            Edge edge = graph.addEdge(coord, graph.getStok().getCoord(),1);
            Node node = graph.getNode(coord.toString());
            node.addEdge(edge);
    }

    public void addNodeIntoGraph(Graph graph, Coord coord) {
        graph.addNode(coord);
        nodesInGraph.add(coord);
    }
}
