package com.narxoz.rpg.observer;
import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class HeroStatusMonitor implements GameObserver {
    private List<Hero> heroes;
    public HeroStatusMonitor(List<Hero> heroes) { this.heroes = heroes; }
    public void onEvent(GameEvent e) {
        if (e.getType() == GameEventType.HERO_LOW_HP || e.getType() == GameEventType.HERO_DIED) {
            System.out.println("--- STATUS MONITOR ---");
            for (Hero h : heroes) System.out.println(h.getName() + ": " + h.getHp() + " HP (" + (h.isAlive()?"ALIVE":"DEAD") + ")");
        }
    }
}