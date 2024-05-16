package org.reims.industrial_integration.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.reims.industrial_integration.block.entity.SlicerBlockEntity;
import org.reims.industrial_integration.gui.menu.SlicerMenu;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class SlicerScreen extends AbstractMachineWithEnergyScreen<SlicerBlockEntity, SlicerMenu> {
    public SlicerScreen(SlicerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, MachineInterfaces.SLICER);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
    }
}
