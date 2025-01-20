package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private final PrintBattleLog printBattleLog;

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        int round = 1;
        while (true) {
            System.out.println("=== Раунд " + round + " ===");

            List<Unit> livingUnits = collectLivingUnits(playerArmy, computerArmy);
            if (livingUnits.isEmpty()) {
                break;
            }

            livingUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : livingUnits) {
                if (!unit.isAlive()) {
                    continue;
                }
                Unit target = unit.getProgram().attack();
                if (target != null) {
                    printBattleLog.printBattleLog(unit, target);
                }
            }

            boolean playerHasLiving = hasLiving(playerArmy);
            boolean compHasLiving = hasLiving(computerArmy);

            if (!playerHasLiving && !compHasLiving) {
                System.out.println("Обе армии погибли одновременно!");
                break;
            } else if (!playerHasLiving) {
                System.out.println("Армия игрока побеждена!");
                break;
            } else if (!compHasLiving) {
                System.out.println("Армия компьютера побеждена!");
                break;
            }
            round++;
            Thread.sleep(50);
        }
        System.out.println("Бой завершён!");
    }

    private List<Unit> collectLivingUnits(Army playerArmy, Army computerArmy) {
        List<Unit> result = new ArrayList<>();
        for (Unit u : playerArmy.getUnits()) {
            if (u.isAlive()) {
                result.add(u);
            }
        }
        for (Unit u : computerArmy.getUnits()) {
            if (u.isAlive()) {
                result.add(u);
            }
        }
        return result;
    }

    private boolean hasLiving(Army army) {
        for (Unit u : army.getUnits()) {
            if (u.isAlive()) {
                return true;
            }
        }
        return false;
    }
}