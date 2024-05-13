package org.reims.industrial_integration.gui.utils;

public class EnergyBar {
    public final int offsetX, offsetY, startX, startY, width, height, capacity;

    public EnergyBar(int offsetX, int offsetY, int startX, int startY, int width, int height, int capacity) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
    }
}
