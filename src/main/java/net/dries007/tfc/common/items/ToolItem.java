package net.dries007.tfc.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.TFCTags;

public class ToolItem extends DiggerItem
{
    /**
     * The vanilla constructor sets the attack damage to {@code attackDamage + tier.getAttackDamageBonus()}.
     * Whereas, we want it to be equal to {@code attackDamage * tier.getAttackDamageBonus()}.
     * So, we pass upwards a reverse-engineered constant, in order to get the value we want at the end
     */
    public static float calculateVanillaAttackDamage(float attackDamage, Tier tier)
    {
        return (attackDamage - 1) * tier.getAttackDamageBonus();
    }

    public ToolItem(Tier tier, float attackDamage, float attackSpeed, Tag<Block> mineableBlocks, Properties properties)
    {
        super(calculateVanillaAttackDamage(attackDamage, tier), attackSpeed, tier, mineableBlocks, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity)
    {
        // Mining plants should consume some durability
        if (!level.isClientSide && (TFCTags.Blocks.PLANT.contains(state.getBlock()) || state.getDestroySpeed(level, pos) != 0.0F))
        {
            stack.hurtAndBreak(1, entity, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }
}