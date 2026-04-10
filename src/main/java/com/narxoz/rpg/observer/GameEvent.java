package com.narxoz.rpg.observer;
public class GameEvent {

    private final GameEventType type;
    private final String sourceName;
    private final int value;
    public GameEvent(GameEventType type, String sourceName, int value) {
        this.type = type;
        this.sourceName = sourceName;
        this.value = value;
    }

    public GameEventType getType() { return type; }
    public String getSourceName() { return sourceName; }
    public int getValue() { return value; }
}