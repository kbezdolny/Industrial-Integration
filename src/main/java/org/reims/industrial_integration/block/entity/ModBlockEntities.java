package org.reims.industrial_integration.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Industrial_Integration.MODID);

    public static final RegistryObject<BlockEntityType<StationBlockEntity>> STATION = BLOCK_ENTITIES.register("station", () ->
            BlockEntityType.Builder.of(StationBlockEntity::new, ModBlocks.STATION_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<CompressorBlockEntity>> COMPRESSOR = BLOCK_ENTITIES.register(MachineInterfaces.COMPRESSOR.UID, () ->
            BlockEntityType.Builder.of(CompressorBlockEntity::new, ModBlocks.COMPRESSOR_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
