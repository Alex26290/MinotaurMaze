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
//Класс, содержащи двумерный массив ячеек, описывает лабиринт
class Matrix {

    private Box[][] matrix;

    //Конструктор для создания объекта
    public Matrix(Box defaultBox) {
        matrix = new Box[Ranges.getSize().x][Ranges.getSize().y];
        for (Coord coord : Ranges.getAllCoords()) {
            matrix[coord.x][coord.y] = defaultBox;
        }
    }
//Стандартные геттеры и сеттеры для установки значений
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
