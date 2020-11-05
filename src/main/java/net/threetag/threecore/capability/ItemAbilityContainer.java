package net.threetag.threecore.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.*;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.compat.curios.DefaultCuriosHandler;
import net.threetag.threecore.network.UpdateAbilityMessage;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.ItemIcon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemAbilityContainer implements IAbilityContainer {

    public final ItemStack stack;
    public ResourceLocation id;
    protected final AbilityMap map;
    public boolean init = false;

    public ItemAbilityContainer(ItemStack stack) {
        this.stack = stack;
        this.map = new AbilityMap();
    }

    @Override
    public void tick(LivingEntity entity) {
        this.id = null;
        for (EquipmentSlotType slots : EquipmentSlotType.values()) {
            if (entity.getItemStackFromSlot(slots) == this.stack) {
                this.id = new ResourceLocation(ThreeCore.MODID, slots.toString().toLowerCase());
                break;
            }
        }

        if (this.id == null) {
            DefaultCuriosHandler.INSTANCE.getCurioEquipped(stack -> stack == this.stack, entity).ifPresent(triple -> {
                this.id = new ResourceLocation("curios", triple.getLeft());
            });
        }

        if (!this.init) {
            if (stack.getOrCreateTag().contains("Abilities")) {
                AbilityHelper.loadFromNBT(stack.getOrCreateTag().getCompound("Abilities"), this.map);
            } else if (stack.getItem() instanceof IAbilityProvider && !entity.world.isRemote) {
                this.addAbilities(null, (IAbilityProvider) stack.getItem());
            } else {
                return;
            }
        }

        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.sync != EnumSync.NONE || !init || a.dirty) {
                onUpdated(entity, a, a.sync);
                a.sync = EnumSync.NONE;
            }
            if (a.dirty) {
                a.dirty = false;
                this.stack.getOrCreateTag().put("Abilities", AbilityHelper.saveToNBT(this.map));
            }
        });

        this.init = true;
    }

    @Override
    public void onUpdated(LivingEntity entity, Ability ability, EnumSync sync) {
        if (entity.world.isRemote)
            return;

        this.stack.getOrCreateTag().put("Abilities", AbilityHelper.saveToNBT(this.map));

        if (sync != EnumSync.NONE && entity instanceof ServerPlayerEntity) {
            ThreeCore.NETWORK_CHANNEL.sendTo(new UpdateAbilityMessage(entity.getEntityId(), this.getId(), ability.getId(), ability.getUpdateTag()), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        if (sync == EnumSync.EVERYONE && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateAbilityMessage(entity.getEntityId(), this.getId(), ability.getId(), ability.getUpdateTag()));
        }
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.map;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ITextComponent getTitle() {
        return this.stack.getDisplayName();
    }

    @Override
    public IIcon getIcon() {
        return new ItemIcon(this.stack);
    }

    @Override
    public CompoundNBT getNbtTag(@Nonnull LivingEntity entity) {
        return this.stack.getOrCreateTag();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull LivingEntity entity, @Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.stack.getCapability(cap, side);
    }
}
