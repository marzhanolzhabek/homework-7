package com.narxoz.rpg.observer;
import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class PartySupport implements GameObserver {
    private List<Hero> heroes;
    public PartySupport(List<Hero> heroes) { this.heroes = heroes; }
    public void onEvent(GameEvent e) {
        if (e.getType() == GameEventType.HERO_LOW_HP) {
            System.out.println("PartySupport: EMERGENCY HEAL initiated!");
            for (Hero h : heroes) if (h.isAlive()) h.heal(20);
        }
    }
}