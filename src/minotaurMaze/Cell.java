package minotaurMaze;

import fordfulcerson.Edge;
import fordfulcerson.Graph;
import fordfulcerson.Node;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    public static Matrix cellMap;
    public static List<Coord> nodesInGraph;
    public static List<Coord> visitedNodes;

    void start() {
        cellMap = new Matrix(Box.OPENED);
        for (int j = 0; j < 8; j++) {
            Coord coord = Ranges.getRandomCoord();
            cellMap.set(coord, Box.CLOSED);
            if (j == 7) {
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
                addNodeIntoGraph(graph, coord);
                ArrayList<Coord> coordsAround = Ranges.getCoordsAround(coord);
                if (coordsAround.size() != 4) {
                    coord.setExtreme(true);
                }
                graph.setIstok(new Node(coord));
                for (Coord coord2 : coordsAround) {
                    if (nodesInGraph.contains(coord2)) {
                        visitedNodes.add(coord2);
                        Edge edge = graph.addEdge(coord, coord2, 1);
                        graph.getNode(coord.toString()).addEdge(edge);
                        Node node = graph.getNode(coord2.toString());
                        if (!node.hasEdge(coord2, coord, 1)) {
                            System.out.println("!!!!Новая грань");
                            node.addEdge(edge);
                        }
                    }
                }

                List<Coord> coordsAroundBasis = findNewListOfAroundCoords(graph, coordsAround);
                for (Coord coord1 : coordsAroundBasis) {
                    System.out.println("Координата в списке вокруг базисных " + coord1);
                }
                while (!isEdgesCreatedForAllCoords(coordsAroundBasis)) {
                    List<Coord> newCoordsAround = findNewListOfAroundCoords(graph, coordsAroundBasis);
                    coordsAroundBasis = newCoordsAround;
                    for (Coord coord1 : newCoordsAround) {
                        System.out.println("Координата в списке " + coord1);
                    }
//                System.out.println("isEdgesCreatedForAllNodesAround = " + isEdgesCreatedForAllCoords(newCoordsAround));
                }
            }
        }
        return graph;
    }

    private List<Coord> findNewListOfAroundCoords(Graph graph, List<Coord> coordsAround) {
        List<Coord> newCoordsAround = new ArrayList<>();
        for (Coord coord2 : coordsAround) {
            System.out.println("test1, coord 2 = " + coord2);
            if (nodesInGraph.contains(coord2)) {
                System.out.println("test2");
                ArrayList<Coord> coordsAroundCurrent = Ranges.getCoordsAround(coord2);
                if (coordsAroundCurrent.size() != 4) {
                    System.out.println("Создаём грань к стоку");
                    Edge edge = graph.addEdge(coord2, graph.getStok().getCoord(), 1);
                    graph.getNode(coord2.toString()).addEdge(edge);
                } else {
                    for (Coord c : coordsAroundCurrent) {
                        System.out.println("test3");
                        if (nodesInGraph.contains(c)) {
                            System.out.println("test4");
                            if (!visitedNodes.contains(c)) {
                                System.out.println("test5");
                                Edge edge = graph.addEdge(coord2, c, 1);
                                graph.getNode(coord2.toString()).addEdge(edge);
                                Node node = graph.getNode(c.toString());
                                if (!node.hasEdge(c, coord2, 1)) {
                                    node.addEdge(edge);
                                }
                                if (!newCoordsAround.contains(c)) {
                                    System.out.println("Test6");
//                                visitedNodes.add(c);
                                    newCoordsAround.add(c);
                                }
                            }
                        }
                    }
                }
                }
                visitedNodes.add(coord2);
            }
            return newCoordsAround;
        }

        private boolean isEdgesCreatedForAllCoords (List < Coord > newCoordsAround) {
            boolean allCreated = true;
            for (Coord coord : newCoordsAround) {
                if (nodesInGraph.contains(coord)) {
                    if (!visitedNodes.contains(coord)) {
                        allCreated = false;
                    }
                }
            }
            return allCreated;
        }

        private void addNodesIntoGraph (Graph graph, List < Coord > coords){
            for (Coord coord : coords) {
                if (cellMap.get(coord) == Box.OPENED) {
                    addNodeIntoGraph(graph, coord);
                }
            }
        }

        public void addStokNodeIntoGraph (Graph graph){
            Coord stok = new Coord(-1, -1);
            graph.setStok(new Node(stok));
            addNodeIntoGraph(graph, stok);
        }

        public void createEdgesForCoords (Graph graph, Coord coord){
            ArrayList<Coord> coordsAround = Ranges.getCoordsAround(coord);
            int coordsAroundSize = coordsAround.size();
            if (coordsAroundSize == 4) {
                for (Coord coord2 : coordsAround) {
                    if (nodesInGraph.contains(coord2) && !visitedNodes.contains(coord2)) {
                        Edge edge = graph.addEdge(coord, coord2, 1);
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

        private void createStokEdgeForCoord (Graph graph, Coord coord){
            Edge edge = graph.addEdge(coord, graph.getStok().getCoord(), 1);
            Node node = graph.getNode(coord.toString());
            node.addEdge(edge);
        }

        public void addNodeIntoGraph (Graph graph, Coord coord){
            graph.addNode(coord);
            nodesInGraph.add(coord);
        }
    }
