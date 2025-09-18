package net.threetag.palladium.power.ability.unlocking;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.gui.screen.power.BuyAbilityScreen;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.logic.condition.AbilityUnlockedCondition;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.network.AbilityClickedPacket;
import net.threetag.palladium.network.OpenAbilityBuyScreenPacket;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BuyableUnlockingHandler extends UnlockingHandler {

    public final List<Condition> conditions;
    private final List<AbilityReference> parentAbilities;

    protected BuyableUnlockingHandler(List<Condition> conditions) {
        this.conditions = conditions;
        this.parentAbilities = new ArrayList<>();

        for (Condition condition : conditions) {
            if (condition instanceof AbilityUnlockedCondition(AbilityReference ability)) {
                this.parentAbilities.add(ability);
            }
        }
    }

    @Override
    public boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        return abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.BOUGHT.get(), false) && Condition.checkConditions(this.conditions, DataContext.forAbility(entity, abilityInstance));
    }

    @Override
    public void registerDataComponents(DataComponentMap.Builder builder) {
        builder.set(PalladiumDataComponents.Abilities.BOUGHT.get(), false);
    }

    public abstract boolean hasEnoughCurrency(LivingEntity entity);

    public abstract void consumeCurrency(LivingEntity entity);

    public void unlock(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        abilityInstance.set(PalladiumDataComponents.Abilities.BOUGHT.get(), true);
    }

    public abstract Display getDisplay();

    @Override
    public void onClicked(Player player, AbilityInstance<?> abilityInstance) {
        if(abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.BOUGHT.get(), false)) {
            return;
        }

        if (player.level().isClientSide) {
            NetworkManager.sendToServer(new AbilityClickedPacket(abilityInstance.getReference()));
        } else if(player instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, new OpenAbilityBuyScreenPacket(abilityInstance.getReference(), this.hasEnoughCurrency(player)));
        }
    }

    @Environment(EnvType.CLIENT)
    public @Nullable Screen getScreen(PowersScreen parentScreen, AbilityReference abilityReference, boolean available) {
        return new BuyAbilityScreen(abilityReference, this.getDisplay(), available, parentScreen);
    }

    @Override
    public List<AbilityReference> getParentAbilities() {
        return this.parentAbilities;
    }

    public record Display(Icon icon, int amount, Component description) {

    }
}
