package com.narxoz.rpg.combatant;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.*;
public class DungeonBoss implements GameObserver {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private CombatStrategy strategy;
    private int currentPhase = 1;
    private final GameEventManager eventManager;

    public DungeonBoss(String name, int hp, int attackPower, int defense, GameEventManager em) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.eventManager = em;
        this.strategy = new BalancedStrategy();
    }
    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - amount);
        checkPhaseTransitions();
    }

    private void checkPhaseTransitions() {
        double hpPercent = (double) hp / maxHp;

        if (currentPhase == 1 && hpPercent <= 0.6) {
            currentPhase = 2;
            eventManager.notify(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        else if (currentPhase == 2 && hpPercent <= 0.3) {
            currentPhase = 3;
            eventManager.notify(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }
    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED && event.getSourceName().equals(this.name)) {
            if (event.getValue() == 2) {
                this.strategy = new AggressiveStrategy();
            } else if (event.getValue() == 3) {
                this.strategy = new CombatStrategy() {
                    public int calculateDamage(int bp) { return bp * 2; }
                    public int calculateDefense(int bd) { return 0; }
                    public String getName() { return "DESPERATE MODE"; }
                };
            }
        }
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public CombatStrategy getStrategy() { return strategy; }
}