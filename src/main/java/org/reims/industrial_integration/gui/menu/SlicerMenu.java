package org.reims.industrial_integration.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.reims.industrial_integration.block.entity.SlicerBlockEntity;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class SlicerMenu extends AbstractMachineMenu<SlicerBlockEntity> {
    public SlicerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(id, inventory, extraData, ModMenuTypes.SLICER_MENU.get(), MachineInterfaces.SLICER);
    }

    public SlicerMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(id, inventory, entity, data, ModMenuTypes.SLICER_MENU.get(), MachineInterfaces.SLICER);
    }
}
