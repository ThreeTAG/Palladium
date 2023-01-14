package net.threetag.palladium.compat.kubejs.ability;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.compat.kubejs.AbilityEntryJS;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyLookup;

import java.util.ArrayList;
import java.util.List;

public class AbilityBuilder extends BuilderBase<Ability> {

	public static class DeserializePropertyInfo {
		public String key;
		public String type;
		public Object defaultValue;
		public String configureDesc;

		public DeserializePropertyInfo(String key, String type, Object defaultValue, String configureDesc) {
			this.key = key;
			this.type = type;
			this.defaultValue = defaultValue;
			this.configureDesc = configureDesc;
		}
	}

    public transient IIcon icon;
    public transient TickFunction firstTick, tick, lastTick;

	public transient List<DeserializePropertyInfo> extraProperties;

    public AbilityBuilder(ResourceLocation id) {
        super(id);
        this.icon = new ItemIcon(Items.BARRIER);
        this.firstTick = null;
        this.tick = null;
        this.lastTick = null;
		this.extraProperties = new ArrayList<>();
    }

    @Override
    public RegistryObjectBuilderTypes<? super Ability> getRegistryType() {
        return PalladiumKubeJSPlugin.ABILITY;
    }

    @Override
    public Ability createObject() {
        return new ScriptableAbility(this);
    }

    public AbilityBuilder icon(IIcon icon) {
        this.icon = icon;
        return this;
    }

	public AbilityBuilder addProperty(String key, String type, Object defaultValue, String configureDesc) {
		Palladium.LOGGER.info("AbilityBuilder#addProperty");
		PalladiumProperty property = PalladiumPropertyLookup.get(type, key);

		if (property != null)
			this.extraProperties.add(new DeserializePropertyInfo(key, type, defaultValue, configureDesc));
		else
			Palladium.LOGGER.error(String.format("Failed to register ability property \"%s\", type \"%s\" is not supported", key, type));
		return this;
	}

    public AbilityBuilder firstTick(TickFunction firstTick) {
        this.firstTick = firstTick;
        return this;
    }

    public AbilityBuilder tick(TickFunction tick) {
        this.tick = tick;
        return this;
    }

    public AbilityBuilder lastTick(TickFunction lastTick) {
        this.lastTick = lastTick;
        return this;
    }

    @FunctionalInterface
    public interface TickFunction {
        void tick(LivingEntity entity, AbilityEntryJS entry, IPowerHolder holder, boolean enabled);
    }
}
