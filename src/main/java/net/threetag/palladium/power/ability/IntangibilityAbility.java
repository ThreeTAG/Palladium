package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.tag.PalladiumBlockTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class IntangibilityAbility extends Ability {

    public static final MapCodec<IntangibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("vertical", false).forGetter(ab -> ab.vertical),
                    TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("whitelist").forGetter(ab -> Optional.ofNullable(ab.whitelist)),
                    TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("blacklist", PalladiumBlockTags.PREVENTS_INTANGIBILITY).forGetter(ab -> ab.blacklist),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, (vertical, whitelist, blacklist, props, state, bar) ->
                    new IntangibilityAbility(vertical, whitelist.orElse(null), blacklist, props, state, bar)));

    public final boolean vertical;
    @Nullable
    public final TagKey<Block> whitelist;
    public final TagKey<Block> blacklist;

    public IntangibilityAbility(boolean vertical, @Nullable TagKey<Block> whitelist, TagKey<Block> blacklist, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.vertical = vertical;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public static boolean canGoThrough(AbilityInstance<IntangibilityAbility> instance, BlockState state) {
        var whitelist = instance.getAbility().whitelist;
        var blacklist = instance.getAbility().blacklist;

        if (whitelist != null) {
            if (blacklist != null) {
                return state.is(whitelist) && !state.is(blacklist);
            } else {
                return state.is(whitelist);
            }
        } else {
            if (blacklist != null) {
                return !state.is(blacklist);
            } else {
                return true;
            }
        }
    }

    public static BlockState getInWallBlockState(LivingEntity playerEntity) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 8; ++i) {
            double d = playerEntity.getX() + (double) (((float) ((i) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
            double e = playerEntity.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = playerEntity.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = playerEntity.level().getBlockState(mutable);
            if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(playerEntity.level(), mutable)) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    public AbilitySerializer<IntangibilityAbility> getSerializer() {
        return AbilitySerializers.INTANGIBILITY.get();
    }

    public static class Serializer extends AbilitySerializer<IntangibilityAbility> {

        @Override
        public MapCodec<IntangibilityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, IntangibilityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the entity intangible to certain blocks.")
                    .addOptional("vertical", TYPE_BOOLEAN, "Makes the player vertically intangible aswell.", false)
                    .addOptional("whitelist", TYPE_BLOCK_TAG, "Block tag which includes the block the player can phase through. Leave null for all blocks.")
                    .addOptional("blacklist", TYPE_BLOCK_TAG, "Block tag which includes the block the player can phase through. Leave null for all blocks.", PalladiumBlockTags.PREVENTS_INTANGIBILITY.location().toString())
                    .addExampleObject(new IntangibilityAbility(false, null, PalladiumBlockTags.PREVENTS_INTANGIBILITY, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
