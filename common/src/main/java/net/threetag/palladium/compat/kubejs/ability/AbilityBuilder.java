//package net.threetag.palladium.compat.kubejs.ability;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.Items;
//import net.threetag.palladium.addonpack.log.AddonPackLog;
//import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
//import net.threetag.palladium.power.IPowerHolder;
//import net.threetag.palladium.power.ability.Ability;
//import net.threetag.palladium.power.ability.AbilityEntry;
//import net.threetag.palladium.util.icon.IIcon;
//import net.threetag.palladium.util.icon.ItemIcon;
//import net.threetag.palladium.util.property.PalladiumProperty;
//import net.threetag.palladium.util.property.PalladiumPropertyLookup;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AbilityBuilder extends BuilderBase<Ability> {
//
//    public transient IIcon icon;
//    public transient TickFunction firstTick, tick, lastTick;
//
//    public transient List<DeserializePropertyInfo> extraProperties;
//    public transient List<DeserializePropertyInfo> uniqueProperties; // disregards the configureDesc property
//    public transient String documentationDescription;
//
//    public AbilityBuilder(ResourceLocation id) {
//        super(id);
//        this.icon = new ItemIcon(Items.BARRIER);
//        this.firstTick = null;
//        this.tick = null;
//        this.lastTick = null;
//        this.documentationDescription = null;
//        this.extraProperties = new ArrayList<>();
//        this.uniqueProperties = new ArrayList<>();
//    }
//
//    @Override
//    public RegistryInfo getRegistryType() {
//        return PalladiumKubeJSPlugin.ABILITY;
//    }
//
//    @Override
//    public Ability createObject() {
//        return new ScriptableAbility(this);
//    }
//
//    public AbilityBuilder icon(IIcon icon) {
//        this.icon = icon;
//        return this;
//    }
//
//    public AbilityBuilder documentationDescription(String documentationDescription) {
//        this.documentationDescription = documentationDescription;
//        return this;
//    }
//
//    @SuppressWarnings("rawtypes")
//    public AbilityBuilder addProperty(String key, String type, Object defaultValue, String configureDesc) {
//        PalladiumProperty property = PalladiumPropertyLookup.get(type, key);
//
//        if (property != null) {
//            this.extraProperties.add(new DeserializePropertyInfo(key, type, defaultValue, configureDesc));
//        } else {
//            AddonPackLog.error("Failed to register ability property \"%s\", type \"%s\" is not supported", key, type);
//        }
//
//        return this;
//    }
//
//    @SuppressWarnings({"rawtypes"})
//    public AbilityBuilder addUniqueProperty(String key, String type, Object defaultValue) {
//        PalladiumProperty property = PalladiumPropertyLookup.get(type, key);
//
//        if (property != null) {
//            this.uniqueProperties.add(new DeserializePropertyInfo(key, type, defaultValue, null));
//        } else {
//            AddonPackLog.error("Failed to register ability unique property \"%s\", type \"%s\" is not supported", key, type);
//        }
//
//        return this;
//    }
//
//    public AbilityBuilder firstTick(TickFunction firstTick) {
//        this.firstTick = firstTick;
//        return this;
//    }
//
//    public AbilityBuilder tick(TickFunction tick) {
//        this.tick = tick;
//        return this;
//    }
//
//    public AbilityBuilder lastTick(TickFunction lastTick) {
//        this.lastTick = lastTick;
//        return this;
//    }
//
//    public String getDocumentationDescription(String description) {
//        this.documentationDescription = description;
//        return this.documentationDescription;
//    }
//
//    @FunctionalInterface
//    public interface TickFunction {
//        void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled);
//    }
//
//    public static class DeserializePropertyInfo {
//
//        public String key;
//        public String type;
//        public Object defaultValue;
//        public String configureDesc;
//
//        public DeserializePropertyInfo(String key, String type, Object defaultValue, String configureDesc) {
//            this.key = key;
//            this.type = type;
//            this.defaultValue = defaultValue;
//            this.configureDesc = configureDesc;
//        }
//    }
//}
