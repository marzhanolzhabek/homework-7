# Homework 7 — The Cursed Dungeon: Boss Encounter System

## Learning Objectives

By completing this assignment, you will:

        - Apply the **Strategy** pattern to make combat algorithms interchangeable at runtime
- Apply the **Observer** pattern to decouple event producers from event consumers
- Discover how two behavioral patterns can **cooperate** — where one pattern drives the other
- Practice extending an existing codebase (your HW6 Hero class)

---

        ## What Is Provided

The following files are given to you. **Do not modify them.**

        | File | Purpose |
        |------|---------|
        | `strategy/CombatStrategy.java` | Strategy interface |
        | `observer/GameObserver.java` | Observer interface |
        | `observer/GameEvent.java` | Event data class |
        | `observer/GameEventType.java` | Event type enum |
        | `combatant/Hero.java` | Hero skeleton (adapted from HW6) |
        | `engine/EncounterResult.java` | Result data class |
        | `Main.java` | Entry point skeleton |

        ---

        ## What You Must Build

You are building **The Cursed Dungeon Boss Encounter** — a multi-phase boss fight where the boss evolves its combat strategy as it takes damage, and every significant battle event notifies a set of subscribed observer systems.

---

        ## Part 1: Strategy Pattern

### The Interface

`CombatStrategy` defines how a combatant fights:

        ```java
int calculateDamage(int basePower);    // returns effective damage to deal
int calculateDefense(int baseDefense); // returns effective defense to apply
String getName();                       // strategy name (used in logs)
```

        ### Hero Strategies

Implement at least **3 strategies** that heroes can use:

        - One that favors dealing more damage at the cost of defense
- One that favors absorbing damage at the cost of offense
- One that keeps base values unchanged (serves as a default)

Heroes must be able to **switch their active strategy at any time**, including mid-battle.
Each strategy must produce **measurably different** `calculateDamage` and `calculateDefense` values —
        if all strategies return the same numbers, the pattern is not implemented correctly.

        ### Boss Strategies

The boss fights in **3 phases** based on its remaining HP percentage:

        | Phase | HP Range | Expected Fighting Style |
        |-------|----------|------------------------|
        | 1 | 100% – 60% | Measured and calculated |
        | 2 | 60% – 30% | Aggressive, presses the attack |
        | 3 | Below 30% | Desperate — maximum aggression, ignores defense |

Each phase must use a **distinct `CombatStrategy` implementation**.

        **Critical requirement:** The boss must switch to the correct phase strategy **automatically as a result of the Observer mechanism** — the engine must never call a strategy switch method directly on the boss.

---

        ## Part 2: Observer Pattern

### The Interface

`GameObserver` has a single method:

        ```java
void onEvent(GameEvent event);
```

A `GameEvent` carries:
        - `getType()` — one of the `GameEventType` values
- `getSourceName()` — name of the entity that triggered the event
- `getValue()` — a relevant numeric value (damage dealt, HP remaining, phase number, etc.)

### Required Event Types

Your system must fire all of the following events at the correct moments:

        | Event | When to fire |
        |-------|-------------|
        | `ATTACK_LANDED` | Any combatant successfully attacks another |
        | `HERO_LOW_HP` | A hero's HP drops below 30% of their max HP |
        | `HERO_DIED` | A hero's HP reaches 0 |
        | `BOSS_PHASE_CHANGED` | The boss crosses a HP phase threshold (60% or 30%) |
        | `BOSS_DEFEATED` | The boss's HP reaches 0 |

        ### Publisher Design

You must design and implement your own publisher/subject mechanism. How observers are registered, stored, and notified is entirely your design decision. There is no provided publisher class.

        ### Required Observers

Implement all five of the following observers:

        **1. BattleLogger**
        - Reacts to: all event types
- Prints a formatted log line for every event that occurs during the encounter

**2. AchievementTracker**
        - Reacts to: `BOSS_DEFEATED`, `HERO_DIED`, `ATTACK_LANDED`
        - Unlocks and prints named achievements when conditions are met
- Must define and unlock at least 3 distinct achievements

**3. PartySupport**
        - Reacts to: `HERO_LOW_HP`
        - Heals a random living ally for a fixed amount when any hero is critically low on HP

**4. HeroStatusMonitor**
        - Reacts to: `HERO_LOW_HP`, `HERO_DIED`
        - Maintains a running status summary of all heroes and prints it when a hero condition changes

**5. LootDropper**
        - Reacts to: `BOSS_PHASE_CHANGED`, `BOSS_DEFEATED`
        - Generates and prints a loot drop each time the boss transitions to a new phase or is defeated

---

        ## Part 3: DungeonBoss

Design and implement `DungeonBoss`. It must:

        - Track its own HP, max HP, attack power, and defense
- Know which phase it is currently in (1, 2, or 3)
- Hold and use an active `CombatStrategy`
        - Fire events when its HP crosses phase thresholds
- React to a `BOSS_PHASE_CHANGED` event by switching to the appropriate phase strategy

The boss must be capable of both **publishing events** and **receiving events**.
How you architect this dual role — and how you wire it up — is part of the assignment.

---

        ## Part 4: DungeonEngine

Design and implement `DungeonEngine`. It must run the encounter in rounds:

        - Each round, every living hero attacks the boss using their active strategy
- After all hero attacks, the boss attacks every living hero using its active strategy
- The correct events must fire at the correct moments (see event table above)
- The encounter ends when the boss is defeated or all heroes are dead
- A max-round limit must be enforced to prevent infinite loops
- Returns a completed `EncounterResult`

        **The engine must never call a strategy switch method directly on the boss.**
All boss phase transitions must flow through the event system.

        ---

        ## Demo Requirements (Main.java)

Your `Main.java` must demonstrate all of the following:

        1. At least **3 heroes** each starting with a different combat strategy
2. A **DungeonBoss** with meaningful stats (high enough HP that all 3 phases are visible)
3. All **5 observers** registered before the encounter starts
4. The boss visibly **transitioning through at least 2 phases** in the output
5. At least one hero **switching strategy** during the battle
6. All 5 observer types **producing output** during the encounter
7. The final **EncounterResult** printed at the end

---

        ## Deliverables

Submit a ZIP file containing:

        - All Java source files (`.java`) — scaffold files + your implementations
- `Main.java` producing the demo output listed above
- **UML Diagram 1:** Strategy pattern — interface, all implementations, Hero and DungeonBoss using it
- **UML Diagram 2:** Observer pattern — observer interface, your publisher, all 5 observers, event classes

Do **not** include compiled `.class` files.

---

        ## Grading (15 points)

| Area | Points | What the grader checks |
        |------|--------|----------------------|
        | **Strategy** | 6 | Interface used correctly; 3+ hero strategies with measurably different values; 3 boss phase strategies; heroes and boss actually switch strategies at runtime; damage/defense formulas produce different results per strategy |
        | **Observer** | 6 | Observer interface used; publisher design is functional; all 5 observers implemented and react to correct events; boss both fires and receives events; boss strategy switch is triggered by event notification, not direct engine call |
        | **Engine + Demo** | 3 | Round loop functions correctly; events fire in the right sequence; EncounterResult returned; Main.java demonstrates all required behaviors visibly |