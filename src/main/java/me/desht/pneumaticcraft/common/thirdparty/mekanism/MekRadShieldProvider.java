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

package me.desht.pneumaticcraft.common.thirdparty.mekanism;

public class MekRadShieldProvider /*implements ICapabilityProvider*/ {
//    private final IRadiationShielding impl;
//    private final LazyOptional<IRadiationShielding> lazy;
//
//    public MekRadShieldProvider(ItemStack stack, EquipmentSlot slot) {
//        this.impl = new PneumaticArmorRadiationShield(stack, slot);
//        this.lazy = LazyOptional.of(() -> impl);
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        return MekanismIntegration.CAPABILITY_RADIATION_SHIELDING.orEmpty(cap, lazy);
//    }
//
//    public static class PneumaticArmorRadiationShield implements IRadiationShielding {
//        private final ItemStack stack;
//        private final EquipmentSlot slot;
//
//        public PneumaticArmorRadiationShield(ItemStack stack, EquipmentSlot slot) {
//            this.stack = stack;
//            this.slot = slot;
//        }
//
//        @Override
//        public double getRadiationShielding() {
//            boolean upgrade = UpgradableItemUtils.getUpgrades(stack, ModUpgrades.RADIATION_SHIELDING.get()) > 0;
//            if (!upgrade) return 0d;
//            switch (slot) {
//                case HEAD: return 0.25;
//                case CHEST: return 0.4;
//                case LEGS: return 0.2;
//                case FEET: return 0.15;
//                default: return 0d;
//            }
//        }
//    }
}
