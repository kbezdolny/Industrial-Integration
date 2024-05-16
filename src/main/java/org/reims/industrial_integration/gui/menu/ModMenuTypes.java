package org.reims.industrial_integration.gui.menu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Industrial_Integration.MODID);

    public static final RegistryObject<MenuType<StationMenu>> STATION_MENU =
            registerMenuType(StationMenu::new, "station_menu");

    public static final RegistryObject<MenuType<CompressorMenu>> COMPRESSOR_MENU =
            registerMenuType(CompressorMenu::new, MachineInterfaces.COMPRESSOR.UID + "_menu");

    public static final RegistryObject<MenuType<SlicerMenu>> SLICER_MENU =
            registerMenuType(SlicerMenu::new, MachineInterfaces.SLICER.UID + "_menu");


    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
