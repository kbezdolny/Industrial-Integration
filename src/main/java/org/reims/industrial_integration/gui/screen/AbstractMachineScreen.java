package org.reims.industrial_integration.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.reims.industrial_integration.block.entity.AbstractMachineBlockEntity;
import org.reims.industrial_integration.gui.menu.AbstractMachineMenu;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.gui.utils.Progressbar;

public abstract class AbstractMachineScreen<Entity extends AbstractMachineBlockEntity, Menu extends AbstractMachineMenu<Entity>>
        extends AbstractContainerScreen<Menu> {
    protected static MachineInterfaceData machineData;
    private static ResourceLocation BACKGROUND;
    protected int WIDTH;
    protected int HEIGHT;

    public AbstractMachineScreen(Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        BACKGROUND = machineData.BACKGROUND;
    }

    @Override
    protected void init() {
        super.init();
        WIDTH = (width - imageWidth) / 2;
        HEIGHT = (height - imageHeight) / 2;
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.blit(poseStack, WIDTH, HEIGHT, 0, 0, imageWidth, imageHeight);

        Progressbar progressbar = machineData.progressbar;
        if (progressbar != null) {
            int barWidth = progressbar.width;
            int barHeight = progressbar.height;
            switch (progressbar.direction) {
                case Right -> barWidth = menu.getScaledProgress(barWidth);
                case Down -> barHeight = menu.getScaledProgress(barHeight);
            }

            renderProgressbar(poseStack, WIDTH+progressbar.startX, HEIGHT+progressbar.startY,
                    progressbar.offsetX, progressbar.offsetY, barWidth, barHeight);
        }
    }

    protected void renderProgressbar(PoseStack poseStack, int startX, int startY, int offsetX,
                                     int offsetY, int progressbarWidth, int progressbarHeight) {
        if(menu.isCrafting()) {
            blit(poseStack, startX, startY, offsetX, offsetY, progressbarWidth, progressbarHeight);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
