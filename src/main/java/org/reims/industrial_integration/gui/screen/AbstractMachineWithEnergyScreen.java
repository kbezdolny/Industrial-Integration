package org.reims.industrial_integration.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.reims.industrial_integration.block.entity.AbstractMachineWithEnergyBlockEntity;
import org.reims.industrial_integration.gui.menu.AbstractMachineMenu;
import org.reims.industrial_integration.gui.screen.renderer.EnergyInfoArea;
import org.reims.industrial_integration.gui.utils.EnergyBar;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.util.MouseUtil;

import java.util.Optional;

public abstract class AbstractMachineWithEnergyScreen<Entity extends AbstractMachineWithEnergyBlockEntity, Menu extends AbstractMachineMenu<Entity>>
        extends AbstractMachineScreen<Entity, Menu> {
    protected EnergyInfoArea energyInfoArea;

    public AbstractMachineWithEnergyScreen(Menu pMenu, Inventory pPlayerInventory, Component pTitle, MachineInterfaceData machineData) {
        super(pMenu, pPlayerInventory, pTitle, machineData);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea(machineData.energyBar);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);

        if (machineData.energyBar == null) { return; }
        energyInfoArea.draw(poseStack, machineData.energyBar, WIDTH, HEIGHT);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY);
    }

    private void assignEnergyInfoArea(EnergyBar energyBar) {
        if (energyBar == null) { return; }
        energyInfoArea = new EnergyInfoArea(WIDTH+energyBar.startX, HEIGHT+energyBar.startY, menu.blockEntity.getEnergyStorage(), energyBar.width, energyBar.height);
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        EnergyBar energyBar = machineData.energyBar;
        if (energyBar == null) { return; }

        if(isMouseAboveArea(pMouseX, pMouseY, WIDTH, HEIGHT, energyBar.startX, energyBar.startY, energyBar.width, energyBar.height)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX-WIDTH, pMouseY-HEIGHT);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
