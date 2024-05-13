package org.reims.industrial_integration.gui.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;
import org.reims.industrial_integration.gui.utils.EnergyBar;

import java.util.List;

public class EnergyInfoArea extends GuiComponent {
    protected final Rect2i area;
    public final IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        this.area = new Rect2i(xMin, yMin, width, height);
        this.energy = energy;
    }

    private String getFloatValue(int value) {
        if (value < 1000)
            return value+"FE";
        return String.format("%.1fkFE", (float) value/1000);
    }

    public List<Component> getTooltips() {
        int stored = energy.getEnergyStored();
        int max = energy.getMaxEnergyStored();
        return List.of(Component.literal(getFloatValue(stored)+"/"+getFloatValue(max)));
    }

    public void draw(PoseStack poseStack, EnergyBar energyBar, int x, int y) {
        final int height = energyBar.height;
        int stored = height - (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        blit(poseStack, x+energyBar.startX, y+energyBar.startY+stored, energyBar.offsetX, stored, energyBar.width, height);
    }
}
