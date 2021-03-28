/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

/**
 * @author DNS
 */
//Класс, описывающий координаты(x,y), ячейки лабиринта
public class Coord {

    public int x;
    public int y;
    public boolean isExtreme;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    public boolean isExtreme() {
        return isExtreme;
    }

    public void setExtreme(boolean extreme) {
        isExtreme = extreme;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
