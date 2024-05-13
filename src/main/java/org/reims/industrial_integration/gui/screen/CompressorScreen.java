package org.reims.industrial_integration.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.reims.industrial_integration.block.entity.CompressorBlockEntity;
import org.reims.industrial_integration.gui.menu.CompressorMenu;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class CompressorScreen extends AbstractMachineWithEnergyScreen<CompressorBlockEntity, CompressorMenu> {
    static {
        machineData = MachineInterfaces.COMPRESSOR;
    }

    public CompressorScreen(CompressorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
    }
}
