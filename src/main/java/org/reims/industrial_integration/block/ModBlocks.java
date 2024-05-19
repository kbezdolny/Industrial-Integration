package org.reims.industrial_integration.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.block.custom.CompressorBlock;
import org.reims.industrial_integration.block.custom.SlicerBlock;
import org.reims.industrial_integration.block.custom.StationBlock;
import org.reims.industrial_integration.item.ModItems;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

import java.util.function.Supplier;

import static org.reims.industrial_integration.Industrial_Integration.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final RegistryObject<Block> TEST_BLOCK = registerBlock("test_block",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)),
            new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB));

    public static final RegistryObject<Block> IRON_CASING = registerBlock("iron_casing",
            () -> new Block(BlockBehaviour.Properties
                    .of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)),
            new Item.Properties().tab(Industrial_Integration.MACHINES_TAB));

    public static final RegistryObject<Block> STATION_BLOCK = registerBlock("station_block",
            () -> new StationBlock(BlockBehaviour.Properties
                    .of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)),
            new Item.Properties().tab(Industrial_Integration.MACHINES_TAB));

    public static final RegistryObject<Block> COMPRESSOR_BLOCK = registerBlock(MachineInterfaces.COMPRESSOR.UID + "_block",
            () -> new CompressorBlock(BlockBehaviour.Properties
                    .of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)),
            new Item.Properties().tab(Industrial_Integration.MACHINES_TAB));

    public static final RegistryObject<Block> SLICER_BLOCK = registerBlock(MachineInterfaces.SLICER.UID + "_block",
            () -> new SlicerBlock(BlockBehaviour.Properties
                    .of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)),
            new Item.Properties().tab(Industrial_Integration.MACHINES_TAB));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
