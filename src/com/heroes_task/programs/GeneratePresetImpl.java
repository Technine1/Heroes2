package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // 1. Сортируем по (baseAttack/cost) убыв., затем по (health/cost) убыв.
        unitList.sort((u1, u2) -> {
            double ratioA1 = (double) u1.getBaseAttack() / u1.getCost();
            double ratioA2 = (double) u2.getBaseAttack() / u2.getCost();
            int cmp = Double.compare(ratioA2, ratioA1);
            if (cmp != 0) return cmp;

            double ratioH1 = (double) u1.getHealth() / u1.getCost();
            double ratioH2 = (double) u2.getHealth() / u2.getCost();
            return Double.compare(ratioH2, ratioH1);
        });

        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(0);

        int remainingPoints = maxPoints;

        for (Unit templateUnit : unitList) {
            int count = 0;
            while (count < 11) {
                if (remainingPoints < templateUnit.getCost()) {
                    break;
                }
                // Клонируем юнита
                Unit newUnit = cloneUnit(templateUnit);

                selectedUnits.add(newUnit);
                computerArmy.setPoints(computerArmy.getPoints() + newUnit.getCost());
                remainingPoints -= newUnit.getCost();
                count++;
            }
        }
        return computerArmy;
    }


    private Unit cloneUnit(Unit template) {
        // Придумайте уникальное имя
        String newName = template.getName() + "_clone";

        // Можно взять поля напрямую из template
        String unitType   = template.getUnitType();
        int health        = template.getHealth();
        int baseAttack    = template.getBaseAttack();
        int cost          = template.getCost();
        String attackType = template.getAttackType();


        Map<String, Double> map1 = new HashMap<>();
        Map<String, Double> map2 = new HashMap<>();

        // Последние два int: x, y.
        // Если в шаблонном юните есть координаты, вы можете взять их.
        // Иначе поставьте 0, 0 или что-либо подходящее.
        int x = 0;
        int y = 0;

        // Теперь вызываем конструктор на 10 параметров:
        Unit newUnit = new Unit(
                newName,     // String name
                unitType,    // String unitType
                health,      // int health
                baseAttack,  // int baseAttack
                cost,        // int cost
                attackType,  // String attackType
                map1,        // Map<String, Double>
                map2,        // Map<String, Double>
                x,           // int
                y            // int
        );

        // Дополнительно, если нужно:
        newUnit.setAlive(true);
        newUnit.setProgram(template.getProgram());

        return newUnit;
    }
}