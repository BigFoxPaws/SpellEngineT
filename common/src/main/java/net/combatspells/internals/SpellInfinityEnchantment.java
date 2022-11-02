package net.combatspells.internals;

import net.combatspells.CombatSpells;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.tinyconfig.models.EnchantmentConfig;

public class SpellInfinityEnchantment extends Enchantment {
    public EnchantmentConfig config;

    public SpellInfinityEnchantment(Enchantment.Rarity weight, EnchantmentConfig config, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
        this.config = config;
    }

    public int getMaxLevel() {
        return config.max_level;
    }

    public int getMinPower(int level) {
        return config.min_cost + (level - 1) * config.step_cost;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        if(!CombatSpells.config.spell_cost_item_allowed) {
            return false;
        }
        var object = (Object)stack;
        if (object instanceof SpellCasterItemStack casterItemStack) {
            return casterItemStack.getSpell() != null;
        }
        return false;
    }

    public boolean canAccept(Enchantment other) {
        return other instanceof MendingEnchantment ? false : super.canAccept(other);
    }
}
