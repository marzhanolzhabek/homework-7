package com.narxoz.rpg.strategy;

public interface CombatStrategy {
    int calculateDamage(int basePower);
    int calculateDefense(int baseDefense);
    String getName();
}