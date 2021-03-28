package minotaurMaze;

import fordfulcerson.Edge;
import fordfulcerson.Graph;
import fordfulcerson.Node;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

//класс для построения лабиринта
public class Maze {

    public static Matrix matrix;
    public static List<Coord> nodesInGraph;
    public static List<Coord> visitedNodes;

    //старт построения лабиринта
    void start() {
        matrix = new Matrix(Box.OPENED);
        for (int j = 0; j < 8; j++) {
            Coord coord = Ranges.getRandomCoord();
            matrix.set(coord, Box.CLOSED);
            if (j == 7) {
                matrix.set(coord, Box.MINO);
            }
        }
    }

    public static Box get(Coord coord) {
        return matrix.get(coord);
    }

    //установка картинки с открытым полем в ячейку
    void setOpenedToBox(Coord coord) {
        if (matrix.get(coord) == Box.OPENED || matrix.get(coord) == Box.MINO) {
            matrix.set(coord, Box.CLOSED);
        } else if (matrix.get(coord) == Box.CLOSED) {
            matrix.set(coord, Box.OPENED);
        }
    }

    //проверка, что в данной клетке находится минотавр
    public boolean isMinotaur() {
        boolean isMinotaur = false;
        List<Coord> allCoords = Ranges.getAllCoords();
        for (Coord c : allCoords) {
            if (matrix.get(c) == Box.MINO) {
                isMinotaur = true;
            }
        }
        return isMinotaur;
    }

    //установка минотавра в ячейку
    void setMinotaurToBox(Coord coord) {
        if (!isMinotaur()) {
            matrix.set(coord, Box.MINO);
        }
    }

    //построение графа из текущего лабиринта
    public Graph createGraphFromCoords() {
        Graph graph = new Graph();
        nodesInGraph = new ArrayList<>();
        visitedNodes = new ArrayList<>();
        List<Coord> coords = Ranges.getAllCoords();
        //добавляем вершины в граф
        addNodesIntoGraph(graph, coords);
        //добавляем вершины со стоком в граф
        addStokNodeIntoGraph(graph);
        for (Coord coord : coords) {
            //ищем вершину с минотавром и строим от неё граф
            if (matrix.get(coord) == Box.MINO) {
                //добавляем клетку в список посещённых
                visitedNodes.add(coord);
                addNodeIntoGraph(graph, coord);
                //ищем клетки вокруг текущей
                ArrayList<Coord> coordsAround = Ranges.getCoordsAround(coord);
                //Если количество клеток вокруг текущей не 4(считаются только те, которые соприкасаются гранями,
                // а не расположенные по диагонали), то устанавливаем флаг, говорящий, что клетка находится на краю лабиринта
                if (coordsAround.size() != 4) {
                    coord.setExtreme(true);
                }
                //устанавливаем клетку истока в граф
                graph.setIstok(new Node(coord));
                //создаём грани для вершин графа
                for (Coord coord2 : coordsAround) {
                    if (nodesInGraph.contains(coord2)) {
                        visitedNodes.add(coord2);
                        Edge edge = graph.addEdge(coord, coord2, 1);
                        graph.getNode(coord.toString()).addEdge(edge);
                        Node node = graph.getNode(coord2.toString());
                        if (!node.hasEdge(coord2, coord, 1)) {
                            node.addEdge(edge);
                        }
                    }
                }

                List<Coord> coordsAroundBasis = findNewListOfAroundCoords(graph, coordsAround);
                //повторем процесс для всех клеток, пока граф не будет построен
                while (!isEdgesCreatedForAllCoords(coordsAroundBasis)) {
                    List<Coord> newCoordsAround = findNewListOfAroundCoords(graph, coordsAroundBasis);
                    coordsAroundBasis = newCoordsAround;
                }
            }
        }
        return graph;
    }

    //ищем новый список координат вокруг ячейки дл новой ячейки
    private List<Coord> findNewListOfAroundCoords(Graph graph, List<Coord> coordsAround) {
        List<Coord> newCoordsAround = new ArrayList<>();
        for (Coord coord2 : coordsAround) {
            if (nodesInGraph.contains(coord2)) {
                ArrayList<Coord> coordsAroundCurrent = Ranges.getCoordsAround(coord2);
                if (coordsAroundCurrent.size() != 4) {
                    Edge edge = graph.addEdge(coord2, graph.getStok().getCoord(), 1);
                    graph.getNode(coord2.toString()).addEdge(edge);
                } else {
                    for (Coord c : coordsAroundCurrent) {
                        if (nodesInGraph.contains(c)) {
                            if (!visitedNodes.contains(c)) {
                                Edge edge = graph.addEdge(coord2, c, 1);
                                graph.getNode(coord2.toString()).addEdge(edge);
                                Node node = graph.getNode(c.toString());
                                if (!node.hasEdge(c, coord2, 1)) {
                                    node.addEdge(edge);
                                }
                                if (!newCoordsAround.contains(c)) {
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

    //проверяем, что грани созданы для всех вершин
    private boolean isEdgesCreatedForAllCoords(List<Coord> newCoordsAround) {
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

    //добавляем вершины в граф
    private void addNodesIntoGraph(Graph graph, List<Coord> coords) {
        for (Coord coord : coords) {
            if (matrix.get(coord) == Box.OPENED) {
                addNodeIntoGraph(graph, coord);
            }
        }
    }

    //добавляем вершину стока в граф
    public void addStokNodeIntoGraph(Graph graph) {
        Coord stok = new Coord(-1, -1);
        graph.setStok(new Node(stok));
        addNodeIntoGraph(graph, stok);
    }

    //метод для добавления вершины в граф
    public void addNodeIntoGraph(Graph graph, Coord coord) {
        graph.addNode(coord);
        nodesInGraph.add(coord);
    }
}
