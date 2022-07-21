/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.common.blocks.crop;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import net.dries007.tfc.common.blockentities.RottingBlockEntity;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.util.loot.TFCLoot;
import org.jetbrains.annotations.Nullable;

public class RottingBlock extends ExtendedBlock implements EntityBlockExtension
{
    private final Supplier<? extends Block> rotted;

    public RottingBlock(ExtendedProperties properties, Supplier<? extends Block> rotted)
    {
        super(properties);
        this.rotted = rotted;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, entity, stack);
        if (level.getBlockEntity(pos) instanceof RottingBlockEntity rotting)
        {
            stack.getCapability(FoodCapability.CAPABILITY).ifPresent(food -> rotting.setRottenTick(food.getRottenDate()));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        if (level.getBlockEntity(pos) instanceof RottingBlockEntity rotting)
        {
            if (rotting.isRotten())
            {
                level.setBlockAndUpdate(pos, rotted.get().defaultBlockState());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        builder = builder.withParameter(TFCLoot.DECAY_HANDLED, true);
        return super.getDrops(state, builder);
    }
}
