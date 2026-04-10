package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.*;
import com.narxoz.rpg.observer.*;
import java.util.List;

public class DungeonEngine {
    private final GameEventManager eventManager;

    public DungeonEngine(GameEventManager eventManager) {
        this.eventManager = eventManager;
    }
    public EncounterResult runEncounter(List<Hero> heroes, DungeonBoss boss) {
        int rounds = 0;
        final int MAX_ROUNDS = 50;

        while (boss.getHp() > 0 && isAnyHeroAlive(heroes) && rounds < MAX_ROUNDS) {
            rounds++;
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    int damage = Math.max(1, hero.getStrategy().calculateDamage(hero.getAttackPower()) -
                            boss.getStrategy().calculateDefense(boss.getDefense()));

                    boss.takeDamage(damage);
                    eventManager.notify(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), damage));
                }
            }
            if (boss.getHp() > 0) {
                for (Hero hero : heroes) {
                    if (hero.isAlive()) {
                        int damage = Math.max(1, boss.getStrategy().calculateDamage(boss.getAttackPower()) -
                                hero.getStrategy().calculateDefense(hero.getDefense()));

                        hero.takeDamage(damage);
                        eventManager.notify(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), damage));
                        if (!hero.isAlive()) {
                            eventManager.notify(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
                        } else if (hero.getHp() < (hero.getMaxHp() * 0.3)) {
                            eventManager.notify(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
                        }
                    }
                }
            }
        }
        if (boss.getHp() <= 0) {
            eventManager.notify(new GameEvent(GameEventType.BOSS_DEFEATED, boss.getName(), rounds));
        }

        int survivors = (int) heroes.stream().filter(Hero::isAlive).count();
        return new EncounterResult(boss.getHp() <= 0, rounds, survivors);
    }

    private boolean isAnyHeroAlive(List<Hero> heroes) {
        return heroes.stream().anyMatch(Hero::isAlive);
    }
}