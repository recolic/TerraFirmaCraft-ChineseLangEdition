/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.config.animals;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec;

public record ProducingAnimalConfig(AnimalConfig inner, ForgeConfigSpec.IntValue produceTicks, ForgeConfigSpec.DoubleValue produceFamiliarity)
{
    public static ProducingAnimalConfig build(Function<String, ForgeConfigSpec.Builder> builder, String name, double familiarityCap, int adulthoodDays, int uses, boolean eatsRottenFood, int produceTicks, double produceFamiliarity)
    {
        return new ProducingAnimalConfig(
            AnimalConfig.build(builder, name, familiarityCap, adulthoodDays, uses, eatsRottenFood),
            builder.apply("%sProduceTicks".formatted(name)).comment("Ticks until produce is ready").defineInRange("%sProduceTicks".formatted(name), produceTicks, 0, Integer.MAX_VALUE),
            builder.apply("%sMinProduceFamiliarity".formatted(name)).comment("Minimum familiarity [0-1] needed for produce. Set above 1 to disable produce.").defineInRange("%sMinProduceFamiliarity".formatted(name), produceFamiliarity, 0, Float.MAX_VALUE)
        );
    }
}
