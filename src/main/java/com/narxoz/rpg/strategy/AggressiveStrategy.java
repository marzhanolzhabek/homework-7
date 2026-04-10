package com.narxoz.rpg.strategy;

public class AggressiveStrategy implements CombatStrategy {
    public int calculateDamage(int bp) { return (int)(bp * 1.5); }
    public int calculateDefense(int bd) { return (int)(bd * 0.5); }
    public String getName() { return "Aggressive"; }
}