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
import net.minecraft.world.level.Level;
import org.reims.industrial_integration.gui.utils.MachineInterfaceData;
import org.reims.industrial_integration.gui.utils.MachineSlot;
import org.reims.industrial_integration.util.RecipeDto;

import javax.annotation.Nullable;

public abstract class AbstractMachineRecipe implements Recipe<SimpleContainer> {
    interface RecipeFactory<R extends AbstractMachineRecipe> {
        R create(ResourceLocation id, RecipeDto recipeDto);
    }

    protected final MachineInterfaceData machineData;
    protected final ResourceLocation id;
    protected final int craftDuration;
    protected final int energyReq;
    protected NonNullList<RecipeDto.ItemInterface> recipeItems;
    protected NonNullList<RecipeDto.ItemInterface> resultItems;

    public AbstractMachineRecipe(ResourceLocation id, RecipeDto recipeDto, MachineInterfaceData machineData) {
        this.machineData = machineData;
        this.id = id;
        this.recipeItems = recipeDto.recipeItems;
        this.resultItems = recipeDto.resultItems;
        this.craftDuration = recipeDto.craftDuration;
        this.energyReq = recipeDto.energyReq;
    }

    public NonNullList<RecipeDto.ItemInterface> getRecipeItems() {
        return recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        var slots = machineData.slots;
        for (int i = 0; i < slots.toArray().length; i++) {
            MachineSlot slot = slots.get(i);
            if (slot.type != MachineSlot.SlotType.INPUT) {
                continue;
            }

            if (!recipeItems.get(slot.index).itemIngredient.test(simpleContainer.getItem(slot.index))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return null;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    public NonNullList<RecipeDto.ItemInterface> getResultItems() {
        return resultItems;
    }

    public int getCraftDuration() {
        return craftDuration;
    }

    public int getEnergyReq() {
        return energyReq;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static <R extends AbstractMachineRecipe> R fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe, RecipeFactory<R> factory) {
        JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
        NonNullList<RecipeDto.ItemInterface> recipeItems = NonNullList.withSize(ingredients.size(), new RecipeDto.ItemInterface());

        for (int i = 0; i < recipeItems.size(); i++) {
            int itemCount = getValidItemCount((JsonObject) ingredients.get(i));
            recipeItems.set(i, new RecipeDto.ItemInterface(Ingredient.fromJson(ingredients.get(i)), itemCount));
        }

        JsonArray outputs = GsonHelper.getAsJsonArray(pSerializedRecipe, "output");
        NonNullList<RecipeDto.ItemInterface> resultItems = NonNullList.withSize(outputs.size(), new RecipeDto.ItemInterface());

        for (int i = 0; i < resultItems.size(); i++) {
            ItemStack output = ShapedRecipe.itemStackFromJson((JsonObject) outputs.get(i));
            resultItems.set(i, new RecipeDto.ItemInterface(output));
        }

        int craftDuration = 140; // Default craft speed
        if (pSerializedRecipe.has("duration")) {
            craftDuration = pSerializedRecipe.getAsJsonPrimitive("duration").getAsInt();
        }

        int energyReq = 0; // Default energy required
        if (pSerializedRecipe.has("energy_req")) {
            energyReq = pSerializedRecipe.getAsJsonPrimitive("energy_req").getAsInt();
        }

        RecipeDto recipeDto = new RecipeDto(recipeItems, resultItems, craftDuration, energyReq);
        return factory.create(pRecipeId, recipeDto);
    }

    private static int getValidItemCount(JsonObject jsonObject) {
        int itemCount = 1;
        if (jsonObject.has("count")) {
            itemCount = GsonHelper.getAsInt(jsonObject, "count");
            if (itemCount < 1) {
                itemCount = 1;
            } else if (itemCount > 64) {
                itemCount = 64;
            }
        }
        return itemCount;
    }

    public static @Nullable <R extends AbstractMachineRecipe> R fromNetwork(ResourceLocation id, FriendlyByteBuf buf, RecipeFactory<R> factory) {
        return factory.create(id, RecipeDto.readRecipeDto(buf));
    }

    public void toNetwork(FriendlyByteBuf buf, AbstractMachineRecipe recipe) {
        RecipeDto recipeDto = new RecipeDto(recipeItems, resultItems, craftDuration, energyReq);
        recipeDto.writeRecipeDto(buf);
    }
}
