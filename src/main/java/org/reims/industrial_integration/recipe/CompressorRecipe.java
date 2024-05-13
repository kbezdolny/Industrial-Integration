package org.reims.industrial_integration.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;

import javax.annotation.Nullable;

public class CompressorRecipe extends AbstractMachineRecipe {
    private static final int inputSlotIndex = MachineInterfaces.COMPRESSOR.slots.get(0).index;

    public CompressorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int outputCount, int craftSpeed, int energyReq) {
        super(id, output, recipeItems, outputCount, craftSpeed, energyReq);
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return recipeItems.get(inputSlotIndex).test(simpleContainer.getItem(inputSlotIndex));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CompressorRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = MachineInterfaces.COMPRESSOR.UID;
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Industrial_Integration.MODID, MachineInterfaces.COMPRESSOR.UID);
        private RecipeFactory<CompressorRecipe> factory = CompressorRecipe::new;

        @Override
        public CompressorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return CompressorRecipe.fromJson(pRecipeId, pSerializedRecipe, factory);
        }

        @Override
        public @Nullable CompressorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return CompressorRecipe.fromNetwork(id, buf, factory);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompressorRecipe recipe) {
            CompressorRecipe.toNetwork(buf, recipe);
        }
    }
}
