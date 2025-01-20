package com.heroes_task.programs;

import com.battle.heroes.army.Unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Внешнее хранилище координат: Map<Unit, int[] {x, y}>.
 */
public class CoordinateStorage {

    private static final Map<Unit, int[]> coordsMap = new HashMap<>();

    /**
     * Присвоить юниту координаты (x, y).
     */
    public static void setCoordinates(Unit unit, int x, int y) {
        coordsMap.put(unit, new int[]{x, y});
    }

    public static int[] getCoordinates(Unit unit) {
        return coordsMap.getOrDefault(unit, new int[]{-1, -1});
    }
}