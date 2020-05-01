package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.AbilityGenerator;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ShieldAbilityItem extends ShieldItem implements IAbilityProvider {

    private List<AbilityGenerator> abilityGenerators;
    private List<ITextComponent> description;
    private final int useDuration;
    private final LazyLoadBase<Ingredient> repairMaterial;

    public ShieldAbilityItem(Properties properties, int useDuration, Supplier<Ingredient> repairMaterial) {
        super(properties);
        this.useDuration = useDuration;
        this.repairMaterial = new LazyLoadBase<>(repairMaterial);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairMaterial) {
        return this.repairMaterial.getValue().test(repairMaterial);
    }

    public ShieldAbilityItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public ShieldAbilityItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public ShieldAbilityItem setDescription(List<ITextComponent> description) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.description = description);
        return this;
    }

    public ShieldAbilityItem addDescriptionLine(ITextComponent line) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            if (this.description == null)
                this.description = Lists.newArrayList();
            this.description.add(line);
        });
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.description != null)
            tooltip.addAll(this.description);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemAbilityContainerProvider(stack);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

    public static ShieldAbilityItem parse(JsonObject json, Properties properties) {
        ShieldAbilityItem item = new ShieldAbilityItem(properties,
                JSONUtils.getInt(json, "use_duration", 72000),
                () -> (JSONUtils.hasField(json, "repair_material") ? Ingredient.deserialize(json.get("repair_material")) : Ingredient.EMPTY))
                .setAbilities(JSONUtils.hasField(json, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(json, "abilities"), true) : null);

        return item;
    }

}
