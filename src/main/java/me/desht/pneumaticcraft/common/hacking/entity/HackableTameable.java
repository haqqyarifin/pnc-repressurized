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

package me.desht.pneumaticcraft.common.hacking.entity;

import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IHackableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;
import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.xlate;

public class HackableTameable implements IHackableEntity {

    @Override
    public ResourceLocation getHackableId() {
        return RL("tameable");
    }

    @Override
    public boolean canHack(Entity entity, PlayerEntity player) {
        return entity instanceof TameableEntity && ((TameableEntity) entity).getOwner() != player;
    }

    @Override
    public void addHackInfo(Entity entity, List<ITextComponent> curInfo, PlayerEntity player) {
        curInfo.add(xlate("pneumaticcraft.armor.hacking.result.tame"));
    }

    @Override
    public void addPostHackInfo(Entity entity, List<ITextComponent> curInfo, PlayerEntity player) {
        curInfo.add(xlate("pneumaticcraft.armor.hacking.finished.tamed"));
    }

    @Override
    public int getHackTime(Entity entity, PlayerEntity player) {
        return 60;
    }

    @Override
    public void onHackFinished(Entity entity, PlayerEntity player) {
        if (entity.level.isClientSide) {
            entity.handleEntityEvent((byte) 7);
        } else {
            TameableEntity tameable = (TameableEntity) entity;
            tameable.getNavigation().stop();
            tameable.setTarget(null);
            tameable.setHealth(20.0F);
            tameable.setOwnerUUID(player.getUUID());
            tameable.level.broadcastEntityEvent(entity, (byte) 7);
            tameable.setTame(true);

            // TODO: code smell
            // Would be better to have a HackableCat subclass, but HackableHandler.getHackableForEntity() isn't
            // set up to prioritise getting a cat over a generic tameable.
            if (entity instanceof CatEntity) {
                ((CatEntity) entity).setCatType(-1);  // < 0 means "use a random type"
            }
        }
    }

    @Override
    public boolean afterHackTick(Entity entity) {
        return false;
    }

}
