package com.narxoz.rpg.strategy;

public class DefensiveStrategy implements CombatStrategy {
    public int calculateDamage(int bp) { return (int)(bp * 0.7); }
    public int calculateDefense(int bd) { return (int)(bd * 1.5); }
    public String getName() { return "Defensive"; }
}