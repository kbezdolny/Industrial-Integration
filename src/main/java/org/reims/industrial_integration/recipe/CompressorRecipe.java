package org.reims.industrial_integration.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.util.RecipeDto;

import javax.annotation.Nullable;

public class CompressorRecipe extends AbstractMachineRecipe {
    public CompressorRecipe(ResourceLocation id, RecipeDto recipeDto) {
        super(id, recipeDto, MachineInterfaces.COMPRESSOR);
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
            recipe.toNetwork(buf, recipe);
        }
    }
}
