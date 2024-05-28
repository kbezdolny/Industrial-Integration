package org.reims.industrial_integration.util;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;



public class RecipeDto {
    public static class ItemInterface {
        public @Nullable Ingredient itemIngredient;
        public @Nullable ItemStack itemStack;
        public final int itemCount;

        public ItemInterface() {
            this.itemCount = 0;
        }

        public ItemInterface(@Nullable Ingredient itemIngredient, int itemCount) {
            this.itemIngredient = itemIngredient;
            this.itemCount = itemCount;
            this.itemStack = itemIngredient.getItems()[0];
            this.itemStack.setCount(itemCount);
        }

        public ItemInterface(@Nullable ItemStack itemStack, int itemCount) {
            itemStack.setCount(itemCount);
            this.itemStack = itemStack;
            this.itemCount = itemCount;
        }

        public ItemInterface(@Nullable ItemStack itemStack) {
            this.itemStack = itemStack;
            this.itemCount = itemStack.getCount();
        }
    }

    public final NonNullList<ItemInterface> recipeItems;
    public final NonNullList<ItemInterface> resultItems;
    public final int craftDuration;
    public final int energyReq;

    public RecipeDto(NonNullList<ItemInterface> recipeItems, NonNullList<ItemInterface> resultItems, int craftDuration, int energyReq) {
        this.recipeItems = recipeItems;
        this.resultItems = resultItems;
        this.craftDuration = craftDuration;
        this.energyReq = energyReq;
    }

    public void writeRecipeDto(FriendlyByteBuf buf) {
        buf.writeInt(recipeItems.size());
        for (ItemInterface inter : recipeItems) {
            if (inter.itemIngredient == null) {continue;}

            inter.itemIngredient.toNetwork(buf);
            buf.writeInt(inter.itemCount);
        }

        buf.writeInt(resultItems.size());
        for (ItemInterface inter : resultItems) {
            if (inter.itemStack == null) {continue;}

            inter.itemStack.setCount(inter.itemCount);
            buf.writeItemStack(inter.itemStack, false);
        }

        buf.writeInt(craftDuration);
        buf.writeInt(energyReq);
    }

    public static RecipeDto readRecipeDto(FriendlyByteBuf buf) {
        NonNullList<ItemInterface> recipeItems = NonNullList.withSize(buf.readInt(), new ItemInterface());
        recipeItems.replaceAll(ignored -> new ItemInterface(Ingredient.fromNetwork(buf), buf.readInt()));

        NonNullList<ItemInterface> resultItems = NonNullList.withSize(buf.readInt(), new ItemInterface());
        resultItems.replaceAll(ignored -> new ItemInterface(buf.readItem()));

        int craftSpeed = buf.readInt();
        int energyReq = buf.readInt();

        return new RecipeDto(recipeItems, resultItems, craftSpeed, energyReq);
    }
}
