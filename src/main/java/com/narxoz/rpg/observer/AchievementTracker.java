package com.narxoz.rpg.observer;

public class AchievementTracker implements GameObserver {
    public void onEvent(GameEvent e) {
        if (e.getType() == GameEventType.BOSS_DEFEATED) System.out.println("ACHIEVEMENT: [Boss Slayer] Unlocked!");
        if (e.getType() == GameEventType.HERO_DIED) System.out.println("ACHIEVEMENT: [First Blood] A hero has fallen.");
        if (e.getType() == GameEventType.ATTACK_LANDED && e.getValue() > 40) System.out.println("ACHIEVEMENT: [Brute Force] Massive hit!");
    }
}