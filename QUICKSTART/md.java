# Quickstart Guide — Homework 7

        ## Prerequisites

- Java 17 or later installed
- A terminal / command prompt
- An IDE (optional but recommended)

Check your Java version:
        ```bash
java -version
```

You should see something like `java version "17.x.x"` or higher.

        ---

        ## Project Structure

```
homework-rpg-7/
        ├── ASSIGNMENT.md          ← start here for requirements
├── README.md
├── QUICKSTART.md          ← you are here
├── STUDENT_CHECKLIST.md
├── FAQ.md
└── src/
        └── com/
        └── narxoz/
        └── rpg/
        ├── Main.java
                ├── strategy/
        │   └── CombatStrategy.java    ← provided interface
                ├── observer/
        │   ├── GameObserver.java       ← provided interface
                │   ├── GameEvent.java          ← provided
                │   └── GameEventType.java      ← provided
                ├── combatant/
        │   └── Hero.java               ← provided skeleton
                └── engine/
        └── EncounterResult.java    ← provided
```

You will add new `.java` files alongside the provided ones. The package structure is `com.narxoz.rpg.*` — keep all your files inside the `src/com/narxoz/rpg/` directory tree.

        ---

        ## Compile and Run (Command Line)

From the `homework-rpg-7/` directory:

        **On macOS / Linux:**
        ```bash
# Create output directory
mkdir -p out

# Compile all Java files
find src -name "*.java" | xargs javac -d out

# Run
java -cp out com.narxoz.rpg.Main
```

        **On Windows (Command Prompt):**
        ```cmd
mkdir out
javac -d out src\com\narxoz\rpg\strategy\*.java ^
src\com\narxoz\rpg\observer\*.java ^
src\com\narxoz\rpg\combatant\*.java ^
src\com\narxoz\rpg\engine\*.java ^
src\com\narxoz\rpg\*.java
java -cp out com.narxoz.rpg.Main
```

        > **Tip:** As you add new packages (e.g., `src/com/narxoz/rpg/boss/`), add them to the compile command or use the `find` approach on macOS/Linux.

---

        ## IntelliJ IDEA Setup

1. Open IntelliJ → **File > Open** → select the `homework-rpg-7/` folder
2. Right-click `src/` → **Mark Directory as > Sources Root**
        3. Go to **File > Project Structure > Project** → set **SDK** to Java 17
        4. Right-click `Main.java` → **Run 'Main.main()'**

        ---

        ## VS Code Setup

1. Install the **Extension Pack for Java** from the VS Code marketplace
2. Open the `homework-rpg-7/` folder
3. VS Code will auto-detect the Java project
4. Open `Main.java` → click **Run** above the `main` method

---

        ## Troubleshooting

**"package com.narxoz.rpg.X does not exist"**
You forgot to add the new package directory to your compile command, or the `package` declaration in your file is wrong.

        **"cannot find symbol: class CombatStrategy"**
Make sure your class imports the correct package: `import com.narxoz.rpg.strategy.CombatStrategy;`

        **"Main method not found"**
Ensure `Main.java` has `public static void main(String[] args)` and is in package `com.narxoz.rpg`.