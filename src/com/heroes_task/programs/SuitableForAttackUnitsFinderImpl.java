package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        for (List<Unit> row : unitsByRow) {
            if (row.isEmpty()) {
                continue;
            }

            // Сортируем по координате Y, используя CoordinateStorage
            row.sort(Comparator.comparingInt(u -> {
                com.heroes_task.programs.CoordinateStorage CoordinateStorage = null;
                int[] coords = CoordinateStorage.getCoordinates(u);
                // coords[0] = x, coords[1] = y
                return coords[1];
            }));

            if (isLeftArmyTarget) {
                // Атакует игрок → цель левая армия → "самый левый" = min Y
                // теперь это "первый" живой в списке
                for (Unit candidate : row) {
                    if (candidate.isAlive()) {
                        suitableUnits.add(candidate);
                        break; // нашли, выходим из цикла
                    }
                }
            } else {
                // Атакует компьютер → цель правая армия → "самый правый" = max Y
                // берём последний живой в списке
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit candidate = row.get(i);
                    if (candidate.isAlive()) {
                        suitableUnits.add(candidate);
                        break;
                    }
                }
            }
        }

        return suitableUnits;
    }
}