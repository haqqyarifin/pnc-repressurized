/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.desht.pneumaticcraft.common.thirdparty.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import me.desht.pneumaticcraft.api.crafting.recipe.ExplosionCraftingRecipe;
import me.desht.pneumaticcraft.common.recipes.PneumaticCraftRecipeType;
import me.desht.pneumaticcraft.common.recipes.machine.ExplosionCraftingRecipeImpl;
import me.desht.pneumaticcraft.common.thirdparty.crafttweaker.CTUtils;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@Document("mods/PneumaticCraft/ExplosionCrafting")
@ZenCodeType.Name("mods.pneumaticcraft.explosioncrafting")
@ZenRegister
public class ExplosionCrafting implements IRecipeManager {
    public static final String name = "PneumaticCraft Explosion Crafting";

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredientWithAmount input, IItemStack[] outputs, int lossRate) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new ExplosionCraftingRecipeImpl(
                        new ResourceLocation("crafttweaker", fixRecipeName(name)),
                        CTUtils.toStackedIngredient(input),
                        lossRate,
                        CTUtils.toItemStacks(outputs))
                ));
    }

    @Override
    public void removeRecipe(IIngredient output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe(this, iRecipe -> {
            if (iRecipe instanceof ExplosionCraftingRecipe) {
                return ((ExplosionCraftingRecipe)iRecipe).getOutputs().stream()
                        .anyMatch(stack -> output.matches(new MCItemStack(stack)));
            }
            return false;
        }));
    }

    @Override
    public IRecipeType<ExplosionCraftingRecipe> getRecipeType() {
        return PneumaticCraftRecipeType.EXPLOSION_CRAFTING;
    }
}
