package net.threetag.palladium.power.ability.unlocking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.icon.ExperienceIcon;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.util.Utils;

import java.util.Collections;
import java.util.List;

public class ExperienceLevelBuyableUnlockingHandler extends BuyableUnlockingHandler {

    public static final MapCodec<ExperienceLevelBuyableUnlockingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("xp_level").forGetter(h -> h.xpLevel),
            Condition.LIST_CODEC.optionalFieldOf("conditions", Collections.emptyList()).forGetter(h -> h.conditions)
    ).apply(instance, ExperienceLevelBuyableUnlockingHandler::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceLevelBuyableUnlockingHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, h -> h.xpLevel,
            ByteBufCodecs.collection(Utils::newList, Condition.STREAM_CODEC), h -> h.conditions,
            ExperienceLevelBuyableUnlockingHandler::new
    );

    private final int xpLevel;

    public ExperienceLevelBuyableUnlockingHandler(int xpLevel, List<Condition> conditions) {
        super(conditions);
        this.xpLevel = xpLevel;
    }

    @Override
    public boolean hasEnoughCurrency(LivingEntity entity) {
        if (entity instanceof Player player) {
            return player.experienceLevel >= this.xpLevel;
        }

        return false;
    }

    @Override
    public void consumeCurrency(LivingEntity entity) {
        if (entity instanceof Player player) {
            player.giveExperienceLevels(-this.xpLevel);
        }
    }

    @Override
    public Display getDisplay() {
        return new Display(new ExperienceIcon(1, true), this.xpLevel, Component.translatable("gui.palladium.powers.buy_ability.experience_level" + (this.xpLevel > 1 ? "_plural" : ""), this.xpLevel));
    }

    @Override
    public UnlockingHandlerSerializer<?> getSerializer() {
        return UnlockingHandlerSerializers.XP_BUYABLE.get();
    }

    public static class Serializer extends UnlockingHandlerSerializer<ExperienceLevelBuyableUnlockingHandler> {

        @Override
        public MapCodec<ExperienceLevelBuyableUnlockingHandler> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExperienceLevelBuyableUnlockingHandler> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
