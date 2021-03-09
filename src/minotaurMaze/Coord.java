/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

/**
 * @author DNS
 */
public class Coord {

    public int x;
    public int y;
    public boolean isExtreme;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

//    static ArrayList<Coord> getCoordsAround(Coord coord) {
//        Coord around;
//        ArrayList<Coord> list = new ArrayList<Coord>();
//        for (int x = coord.x - 1; x <= coord.x + 1; x++) {
//            for (int y = coord.y - 1; y <= coord.y + 1; y++) {
//                if (inRange(around = new Coord(x, y))) {
//                    if (!around.equals(coord)) {
//                        list.add(around);
//                    }
//                }
//            }
//        }
//        return list;
//    }

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
