# FAQ — Homework 7: The Cursed Dungeon

---

        ## Strategy Pattern

**Q: What is the difference between Strategy and State pattern?**
A: They look structurally similar but serve different purposes. State is about an object managing its own transitions through a set of internal states — the object knows when and why it changes. Strategy is about making an algorithm interchangeable from the outside — the client chooses which strategy to plug in. In this assignment the boss switches strategy in response to an external event, but the swappable algorithm objects are pure Strategy. The distinction matters more conceptually than structurally here.

        **Q: Do my hero strategies need to produce sufficiently different values?**
A: Yes. If `calculateDamage(100)` returns 100 for every strategy, you have not implemented the pattern — you have just created classes that do nothing different. Each strategy must produce measurably different outputs so the grader can see the pattern working in the demo.

        **Q: Can a hero or boss have no strategy (null)?**
A: No. Every combatant must always have an active strategy. Set a default strategy in the constructor.

        **Q: Can I add more than 3 hero strategies?**
A: Yes. Three is the minimum required. Extra strategies are welcome.

        **Q: Should strategy objects be stateless?**
A: Yes. A strategy takes inputs and returns outputs. It should not track per-battle data or modify combatant fields directly.

**Q: Can two heroes share the same strategy object instance?**
A: Yes. Because strategies are stateless, sharing instances is safe and efficient.

**Q: What multiplier values should I use?**
A: This is your design decision. Make sure differences are visible. A common approach: aggressive multiplies damage by 1.5 and defense by 0.7; defensive does the inverse; balanced keeps both at 1.0.

        **Q: Does the strategy affect both attack and defense at the same time?**
A: `calculateDamage` is used when the combatant attacks. `calculateDefense` is used when the combatant is receiving an attack. Both are active, but at different moments in the round.

**Q: How does the boss know which strategy to switch to in phase 3?**
A: The boss decides this inside its own `onEvent()` method when it receives `BOSS_PHASE_CHANGED`. It can check its own current HP percentage to determine which phase it has entered, then assign the correct strategy.

**Q: Can I hardcode strategy instantiation inside DungeonBoss?**
A: Yes. The boss can create its own strategy objects internally. They must still be proper `CombatStrategy` implementations, not inline logic.

---

        ## Observer Pattern

**Q: Can a class be both a publisher and an observer at the same time?**
A: Yes. This is exactly what DungeonBoss does. There is nothing wrong with a class both firing events and listening to events. Many real-world systems are built this way.

**Q: Does the boss need to be in the same observer list as the other 5 observers?**
A: That depends on your publisher design. If all observers share one list, the boss registers itself there too. If you filter by event type, the boss only subscribes to `BOSS_PHASE_CHANGED`. Both are valid.

**Q: Should observers know about each other?**
A: No. Each observer reacts independently to the events it receives. If observers called each other, they would become coupled — which defeats the purpose of the pattern.

**Q: What should I put in `GameEvent.getValue()`?**
A: Use the most relevant number for each event type. For `ATTACK_LANDED` — damage dealt. For `HERO_LOW_HP` — remaining HP. For `BOSS_PHASE_CHANGED` — new phase number (1, 2, or 3). For `BOSS_DEFEATED` — rounds survived.

        **Q: Can I add more event types beyond the required 5?**
A: Yes. `CRITICAL_HIT`, `HERO_HEALED`, `LOOT_DROPPED` — any extras you define are fine, as long as all 5 required types are present and fire at the right moments.

        **Q: Should I filter events inside each observer's `onEvent()`?**
A: Yes. Each observer should check `event.getType()` and only act on the types it cares about. `BattleLogger` is the exception — it logs everything.

**Q: What is a "publisher" and do I need to make it an interface?**
A: A publisher is any object that holds a list of observers and calls `onEvent()` on each of them when something happens. Whether you extract it into an interface, an abstract class, or keep it as a concrete class is your design decision. All approaches are valid.

        **Q: Can I use a single global event bus?**
A: Yes. A central publisher shared by all objects (boss, engine, heroes) is a valid and clean design. Alternatively, each publisher can maintain its own private observer list. Both work for this assignment.

**Q: What happens if no observer is registered?**
A: Nothing should crash. If the observer list is empty, the notification loop simply iterates over nothing. Guard against null, but an empty list is fine.

        **Q: Does the order in which observers are notified matter?**
A: Not for this assignment. If you find that order matters for your implementation, that is a sign of hidden coupling between observers — which you should remove.

        **Q: Can observers modify game state (e.g., heal a hero)?**
A: `PartySupport` is required to do exactly this. However, observers should not call back into the engine to alter the battle flow — only modify entity state directly (HP values, etc.).

        **Q: How does PartySupport know which heroes exist so it can heal one?**
A: Pass the hero list to `PartySupport` at construction time, or include hero references in the event payload. This is your design decision.

**Q: What achievements should AchievementTracker unlock?**
A: Define at least 3. Examples: "First Blood" (first `ATTACK_LANDED`), "Boss Slayer" (`BOSS_DEFEATED`), "No Man Left Behind" (`BOSS_DEFEATED` with all heroes alive), "Relentless" (10+ attacks landed), "Last Stand" (`BOSS_DEFEATED` with only 1 hero alive). Be creative.

        **Q: Should LootDropper always drop the same loot?**
A: No. Vary the loot — phase transitions and boss death can drop different quality items. Pick from a small predefined list, randomly if you like.

        **Q: What should HeroStatusMonitor track exactly?**
A: At minimum, the current HP and alive/dead status of every hero. It should print this summary whenever a `HERO_LOW_HP` or `HERO_DIED` event fires.

        ---

        ## DungeonBoss

**Q: How does the boss get a reference to the publisher so it can fire events?**
A: Pass it at construction time, or have the engine register the boss with the publisher before the encounter starts. If the boss IS its own publisher, it just calls its own notification method.

**Q: When exactly should BOSS_PHASE_CHANGED fire?**
A: The moment the boss's HP crosses a threshold during a hero's attack. Check thresholds after each individual hero attack, not after all heroes have attacked.

        **Q: Should BOSS_PHASE_CHANGED fire multiple times if the boss drops across two thresholds in one hit?**
A: Yes — fire the event once for each threshold crossed. If the boss drops from 70% to 20% in one hit, fire `BOSS_PHASE_CHANGED` twice: once for the 60% threshold, once for the 30% threshold. Note: since your `onEvent()` method will be called twice in quick succession, make sure it checks the boss's current phase before switching strategy. Without this guard, two rapid `BOSS_PHASE_CHANGED` events could trigger two strategy switches in a row, potentially skipping a phase.

        **Q: Does the boss attack all heroes every round or just one?**
A: The boss attacks every living hero each round. This is intentional — it makes the encounter dangerous.

**Q: Should the boss's strategy affect how much damage heroes deal to it?**
A: Yes. When a hero attacks the boss, apply the boss's `calculateDefense` to reduce incoming damage. The boss's active strategy affects both its offense and its defense.

        ---

        ## DungeonEngine

**Q: The spec says the engine must never call a strategy switch directly on the boss. How does the phase transition happen then?**
A: The engine (or the boss itself) fires a `BOSS_PHASE_CHANGED` event. The boss, being an observer, receives this event in its `onEvent()` method and calls `this.strategy = phaseThreeStrategy` (or similar) there. The engine never calls `boss.setStrategy(...)` directly.

**Q: How do I calculate damage dealt?**
A: A simple formula: `damage = attacker.getStrategy().calculateDamage(attacker.getAttackPower()) - defender.getStrategy().calculateDefense(defender.getDefense())`. Ensure damage is at least 1 so fights always progress.

        **Q: Should I add a max-round safeguard?**
A: Yes. If the encounter is not resolved after a fixed limit (e.g., 50 rounds), end it as a draw and return an appropriate `EncounterResult`. This prevents infinite loops.

        **Q: When should HERO_LOW_HP fire?**
A: Fire it the first time a hero's HP drops below 30% of their maximum. Do not fire it every round while they remain low — only on the transition. Track which heroes have already triggered this event.

        **Q: Does EncounterResult need more fields than what is provided?**
A: No. The three provided fields (`heroesWon`, `roundsPlayed`, `survivingHeroes`) are sufficient for the demo.

        ---

        ## General

**Q: Do I need Maven or Gradle?**
A: No. Plain `javac` is sufficient. See `QUICKSTART.md`.

        **Q: Can I add more classes beyond the required ones?**
A: Yes. Helper classes, enums for loot items, a `BossPhaseConfig` record — all are fine as long as the core pattern requirements are met.

        **Q: Should I write unit tests?**
A: Not required for this assignment. A working, well-demonstrated `Main.java` is sufficient.

        **Q: Can I use Java generics, streams, or lambdas?**
A: Yes, if you are comfortable with them. `List<GameObserver>` is natural here. Do not force them in where they complicate the code.

        **Q: Is it okay if the heroes lose in my demo?**
A: Yes, but make the fight meaningful — tune the stats so that phase transitions are visible, heroes take real damage, and all 5 observer types produce output. A one-round sweep is not an acceptable demo.

**Q: Can I reorganize the package structure?**
A: You may add new sub-packages (e.g., `com.narxoz.rpg.boss`, `com.narxoz.rpg.observer.impl`). Do not move or rename the provided scaffold files.