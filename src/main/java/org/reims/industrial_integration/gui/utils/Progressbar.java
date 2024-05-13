package org.reims.industrial_integration.gui.utils;


public class Progressbar {
    public enum AnimationDirection {
        Right, Down
    }

    public final int offsetX, offsetY, startX, startY, width, height;
    public final AnimationDirection direction;

    public Progressbar(int offsetX, int offsetY, int startX, int startY, int width, int height,
                       AnimationDirection direction) {

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }
}
