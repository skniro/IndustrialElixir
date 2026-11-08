package com.skniro.industrial_elixir.mixin;

import com.skniro.industrial_elixir.item.init.RollingCuttingToolItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.1's {@code Recipe#getRemainingItems} can only produce a fixed item for each ingredient
 * ({@code Item#getCraftingRemainingItem()}), so a rolling/cutting tool used as a crafting
 * ingredient would be consumed outright. 26.1 passes the actual input stack to
 * {@code Item#getCraftingRemainder(ItemStack)}; reproduce that by replacing the tool's
 * remainder with a copy that has one extra point of damage (or nothing when it breaks).
 */
@Mixin(Recipe.class)
public interface RecipeRemainderMixin {

    @Inject(method = "getRemainingItems", at = @At("RETURN"), cancellable = true)
    private void industrialElixir$damageRollingCuttingTool(RecipeInput input, CallbackInfoReturnable<NonNullList<ItemStack>> cir) {
        NonNullList<ItemStack> original = cir.getReturnValue();
        NonNullList<ItemStack> result = null;

        for (int i = 0; i < input.size() && i < original.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof RollingCuttingToolItem) {
                if (result == null) {
                    result = NonNullList.withSize(original.size(), ItemStack.EMPTY);
                    for (int j = 0; j < original.size(); j++) {
                        result.set(j, original.get(j));
                    }
                }
                result.set(i, RollingCuttingToolItem.getRemainingTool(stack));
            }
        }

        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
