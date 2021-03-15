/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

/**
 *
 * @author DNS
 */
class Matrix {

    private Box[][] matrix;

    public Matrix(Box defaultBox) {
        matrix = new Box[Ranges.getSize().x][Ranges.getSize().y];
        for (Coord coord : Ranges.getAllCoords()) {
            matrix[coord.x][coord.y] = defaultBox;
        }
    }

    public Box get(Coord coord) {
        if (Ranges.inRange(coord)) {
            return matrix[coord.x][coord.y];
        }
        return null;
    }

    public void set(Coord coord, Box box) {

        if (Ranges.inRange(coord)) {
            matrix[coord.x][coord.y] = box;
        }
    }

}
