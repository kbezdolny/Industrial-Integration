package org.reims.industrial_integration.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

public class ModRecipes {
    public static DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Industrial_Integration.MODID);

    public static final RegistryObject<RecipeSerializer<StationRecipe>> STATION_SERIALIZER =
            SERIALIZERS.register("station", () -> StationRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIALIZER =
            SERIALIZERS.register(MachineInterfaces.COMPRESSOR.UID, () -> CompressorRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<SlicerRecipe>> SLICER_SERIALIZER =
            SERIALIZERS.register(MachineInterfaces.SLICER.UID, () -> SlicerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
