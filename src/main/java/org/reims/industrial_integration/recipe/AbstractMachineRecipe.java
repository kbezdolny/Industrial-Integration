package org.reims.industrial_integration.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nullable;

public abstract class AbstractMachineRecipe implements Recipe<SimpleContainer> {
    interface RecipeFactory<R extends AbstractMachineRecipe> {
        R create(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int outputCount, int craftSpeed, int energyReq);
    }

    protected final ResourceLocation id;
    protected final ItemStack output;
    protected final int outputCount;
    protected final NonNullList<Ingredient> recipeItems;
    protected final int craftSpeed;
    protected final int energyReq;

    public AbstractMachineRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int outputCount, int craftSpeed, int energyReq) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.outputCount = outputCount;
        this.craftSpeed = craftSpeed;
        this.energyReq = energyReq;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public int getOutputCount() {
        return outputCount;
    }

    public int getCraftSpeed() {
        return craftSpeed;
    }

    public int getEnergyReq() {
        return energyReq;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static <R extends AbstractMachineRecipe> R fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe, RecipeFactory<R> factory) {
        JsonObject outputJson = GsonHelper.getAsJsonObject(pSerializedRecipe, "output");
        ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);

        int outputCount = 1;
        if (outputJson.has("count")) {
            outputCount = outputJson.getAsJsonPrimitive("count").getAsInt();
            if (outputCount < 1) {
                outputCount = 1;
            } else if (outputCount > 64) {
                outputCount = 64;
            }
        }

        int craftSpeed = 140; // Default craft speed
        if (pSerializedRecipe.has("speed")) {
            craftSpeed = pSerializedRecipe.getAsJsonPrimitive("speed").getAsInt();
        }

        int energyReq = 1; // Default energy required
        if (pSerializedRecipe.has("energy_req")) {
            energyReq = pSerializedRecipe.getAsJsonPrimitive("energy_req").getAsInt();
        }

        JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
        NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
        }

        return factory.create(pRecipeId, output, inputs, outputCount, craftSpeed, energyReq);
    }

    public static @Nullable <R extends AbstractMachineRecipe> R fromNetwork(ResourceLocation id, FriendlyByteBuf buf, RecipeFactory<R> factory) {
        NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromNetwork(buf));
        }

        ItemStack output = buf.readItem();
        int outputCount = buf.readInt();
        int craftSpeed = buf.readInt();
        int energyReq = buf.readInt();
        return factory.create(id, output, inputs, outputCount, craftSpeed, energyReq);
    }

    public static void toNetwork(FriendlyByteBuf buf, AbstractMachineRecipe recipe) {
        buf.writeInt(recipe.getIngredients().size());

        for (Ingredient ing : recipe.getIngredients()) {
            ing.toNetwork(buf);
        }
        buf.writeItemStack(recipe.getResultItem(), false);
        buf.writeInt(recipe.getOutputCount());
        buf.writeInt(recipe.getCraftSpeed());
        buf.writeInt(recipe.getEnergyReq());
    }
}
