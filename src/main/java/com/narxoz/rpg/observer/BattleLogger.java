package com.narxoz.rpg.observer;

public class BattleLogger implements GameObserver {
    public void onEvent(GameEvent e) {
        System.out.println("[BATTLE LOG] " + e.getType() + " | Source: " + e.getSourceName() + " | Value: " + e.getValue());
    }
}