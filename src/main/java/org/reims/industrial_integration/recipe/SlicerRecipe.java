package org.reims.industrial_integration.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.reims.industrial_integration.Industrial_Integration;
import org.reims.industrial_integration.gui.utils.MachineInterfaces;
import org.reims.industrial_integration.util.RecipeDto;

import javax.annotation.Nullable;

public class SlicerRecipe extends AbstractMachineRecipe {
    public SlicerRecipe(ResourceLocation id, RecipeDto recipeDto) {
        super(id, recipeDto, MachineInterfaces.SLICER);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SlicerRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = MachineInterfaces.SLICER.UID;
    }

    public static class Serializer implements RecipeSerializer<SlicerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Industrial_Integration.MODID, MachineInterfaces.SLICER.UID);
        private RecipeFactory<SlicerRecipe> factory = SlicerRecipe::new;

        @Override
        public SlicerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return SlicerRecipe.fromJson(pRecipeId, pSerializedRecipe, factory);
        }

        @Override
        public @Nullable SlicerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return SlicerRecipe.fromNetwork(id, buf, factory);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SlicerRecipe recipe) {
            recipe.toNetwork(buf, recipe);
        }
    }
}
