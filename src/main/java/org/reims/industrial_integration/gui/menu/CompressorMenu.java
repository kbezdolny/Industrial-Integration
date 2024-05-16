package org.reims.industrial_integration.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.reims.industrial_integration.block.entity.CompressorBlockEntity;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class CompressorMenu extends AbstractMachineMenu<CompressorBlockEntity> {
    static {
        inventorySize = MachineInterfaces.COMPRESSOR.slotsCount;
    }

    public CompressorMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(id, inventory, extraData, ModMenuTypes.COMPRESSOR_MENU.get());
    }

    public CompressorMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(id, inventory, entity, data, ModMenuTypes.COMPRESSOR_MENU.get());
    }
}
