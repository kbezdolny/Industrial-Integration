package org.reims.industrial_integration.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reims.industrial_integration.Industrial_Integration;

import static org.reims.industrial_integration.Industrial_Integration.MODID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> INCOMPLETE_RAW_RUBBER = ITEMS.register("incomplete_raw_rubber",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_RUBBER = ITEMS.register("raw_rubber",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));


    public static final RegistryObject<Item> CAPACITOR = ITEMS.register("capacitor",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> DIODE = ITEMS.register("diode",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> INDUCTOR = ITEMS.register("inductor",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> RESISTOR = ITEMS.register("resistor",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> TRANSISTOR = ITEMS.register("transistor",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> ELECTRONIC_CIRCUIT = ITEMS.register("electronic_circuit",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));

    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> IRON_ROD = ITEMS.register("iron_rod",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> IRON_BOLT = ITEMS.register("iron_bolt",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> IRON_RING = ITEMS.register("iron_ring",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));
    public static final RegistryObject<Item> IRON_GEAR = ITEMS.register("iron_gear",
            () -> new Item(new Item.Properties().tab(Industrial_Integration.MATERIALS_TAB)));

}
