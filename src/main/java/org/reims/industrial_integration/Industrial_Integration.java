package org.reims.industrial_integration;

import com.mojang.logging.LogUtils;
import earth.terrarium.botarium.Botarium;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.reims.industrial_integration.block.ModBlocks;
import org.reims.industrial_integration.block.entity.ModBlockEntities;
import org.reims.industrial_integration.gui.screen.CompressorScreen;
import org.reims.industrial_integration.gui.screen.SlicerScreen;
import org.reims.industrial_integration.item.ModItems;
import org.reims.industrial_integration.networking.ModMessages;
import org.reims.industrial_integration.recipe.ModRecipes;
import org.reims.industrial_integration.gui.menu.ModMenuTypes;
import org.reims.industrial_integration.gui.screen.StationScreen;
import org.slf4j.Logger;

@Mod(Industrial_Integration.MODID)
public class Industrial_Integration {
    public static final String MODID = "industrial_integration";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Industrial_Integration() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });

        ModMessages.register();
    }

    public static final CreativeModeTab MATERIALS_TAB = new CreativeModeTab(MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.IRON_GEAR.get());
        }
    };

    public static final CreativeModeTab MACHINES_TAB = new CreativeModeTab(MODID+".machines") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModBlocks.SLICER_BLOCK.get());
        }
    };

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.STATION_MENU.get(), StationScreen::new);
            MenuScreens.register(ModMenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
            MenuScreens.register(ModMenuTypes.SLICER_MENU.get(), SlicerScreen::new);
        }
    }
}
