package com.narxoz.rpg;

import com.narxoz.rpg.combatant.*;
import com.narxoz.rpg.engine.*;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.*;
import java.util.*;

/**
 * Entry point for Homework 7 — The Cursed Dungeon: Boss Encounter System.
 */
public class Main {

    public static void main(String[] args) {
        GameEventManager eventManager = new GameEventManager();

        List<Hero> heroes = new ArrayList<>();

        Hero warrior = new Hero("Warrior", 150, 25, 15);
        warrior.setStrategy(new DefensiveStrategy());

        Hero mage = new Hero("Mage", 80, 55, 5);
        mage.setStrategy(new AggressiveStrategy());

        Hero paladin = new Hero("Paladin", 120, 20, 20);
        paladin.setStrategy(new BalancedStrategy());

        heroes.add(warrior);
        heroes.add(mage);
        heroes.add(paladin);

        DungeonBoss boss = new DungeonBoss("Malphas the Demon Lord", 700, 45, 15, eventManager);

        eventManager.subscribe(boss);
        eventManager.subscribe(new BattleLogger());
        eventManager.subscribe(new AchievementTracker());
        eventManager.subscribe(new PartySupport(heroes));
        eventManager.subscribe(new HeroStatusMonitor(heroes));
        eventManager.subscribe(new LootDropper());

        System.out.println("=== THE CURSED DUNGEON ENCOUNTER BEGINS ===");

        System.out.println("[System] Warrior switches to Aggressive stance!");
        warrior.setStrategy(new AggressiveStrategy());

        DungeonEngine engine = new DungeonEngine(eventManager);
        EncounterResult result = engine.runEncounter(heroes, boss);

        System.out.println("\n========================================");
        System.out.println("FINAL ENCOUNTER SUMMARY:");
        System.out.println("Result: " + (result.isHeroesWon() ? "VICTORY!" : "DEFEAT..."));
        System.out.println("Total Rounds: " + result.getRoundsPlayed());
        System.out.println("Heroes Survived: " + result.getSurvivingHeroes());
        System.out.println("========================================");
    }
}