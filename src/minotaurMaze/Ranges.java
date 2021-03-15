/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurMaze;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author DNS
 */
public class Ranges {

    private static Coord size;
    public static ArrayList<Coord> allCoords;
    private static Random random = new Random();

    static void setSize(Coord _size) {
        size = _size;
        allCoords = new ArrayList<>();
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                allCoords.add(new Coord(x, y));
            }
        }
    }

    //получение размеров поля
    public static Coord getSize() {
        return size;
    }

    public static ArrayList<Coord> getAllCoords() {
        return allCoords;
    }

    static boolean inRange(Coord coord) {
        return coord.x >= 0 && coord.x < size.x
                && coord.y >= 0 && coord.y < size.y;
    }

    static Coord getRandomCoord() {
        return new Coord(random.nextInt(size.x), random.nextInt(size.y));
    }

    public static ArrayList<Coord> getCoordsAround(Coord coord) {
        Coord around;
        ArrayList<Coord> list = new ArrayList<Coord>();
        for (int x = coord.x - 1; x <= coord.x + 1; x++) {
            for (int y = coord.y - 1; y <= coord.y + 1; y++) {
                if (inRange(around = new Coord(x, y))) {
                    if (!(!(coord.x == around.x) && !(coord.y == around.y))) {
                        if (!around.equals(coord)) {
                            list.add(around);
                        }
                    }
                }
            }
        }
        return list;
    }
}
