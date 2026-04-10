package com.narxoz.rpg.engine;
public class EncounterResult {

    private final boolean heroesWon;
    private final int roundsPlayed;
    private final int survivingHeroes;

    public EncounterResult(boolean heroesWon, int roundsPlayed, int survivingHeroes) {
        this.heroesWon = heroesWon;
        this.roundsPlayed = roundsPlayed;
        this.survivingHeroes = survivingHeroes;
    }

    public boolean isHeroesWon()      { return heroesWon; }
    public int getRoundsPlayed()      { return roundsPlayed; }
    public int getSurvivingHeroes()   { return survivingHeroes; }
}