package org.reims.industrial_integration.gui.utils;

public class MachineSlot {
    public final int index, posX, posY;
    public final SlotType type;

    public enum SlotType {
        INPUT, OUTPUT
    }

    public MachineSlot(int index, int posX, int posY, SlotType type) {
        this.index = index;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
    }
}
