package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.*;
import net.threetag.threecore.addonpacks.item.ItemParser;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;
import net.threetag.threecore.client.renderer.entity.model.DummyBipedModel;
import net.threetag.threecore.client.renderer.entity.modellayer.*;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class AbilityArmorItem extends ArmorItem implements IAbilityProvider, IModelLayerProvider {

    private List<Supplier<Ability>> abilityGenerators;
    private List<ITextComponent> description;
    public List layers = new LinkedList<>();

    public AbilityArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    public AbilityArmorItem setAbilities(List<Supplier<Ability>> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public AbilityArmorItem addAbility(Supplier<Ability> abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public AbilityArmorItem setDescription(List<ITextComponent> description) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.description = description);
        return this;
    }

    public AbilityArmorItem addDescriptionLine(ITextComponent line) {
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

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) DummyBipedModel.INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE.toString();
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<IModelLayer> getModelLayers(IModelLayerContext context) {
        return this.layers;
    }

    public static AbilityArmorItem parse(JsonObject jsonObject, Item.Properties properties) {
        EquipmentSlotType slot = EquipmentSlotType.fromString(JSONUtils.getString(jsonObject, "slot"));
        if (slot.getSlotType() == EquipmentSlotType.Group.HAND)
            throw new JsonParseException("Slot type must be an armor slot!");

        JsonElement materialJson = jsonObject.get("armor_material");
        IArmorMaterial material = materialJson.isJsonPrimitive() ? ArmorMaterialRegistry.getArmorMaterial(materialJson.getAsString()) : ItemParser.parseArmorMaterial(materialJson.getAsJsonObject(), false);
        if (material == null)
            throw new JsonParseException("The armor material '" + materialJson.getAsString() + "' can not be found!");
        AbilityArmorItem item = new AbilityArmorItem(material, slot, properties);

        // Since items are registered before any resources are loaded I need to push this back using the resource callback
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ModelLayerLoader.POST_LOAD_CALLBACKS.add(() -> {
                item.layers.clear();

                if (JSONUtils.hasField(jsonObject, "layers")) {
                    if (jsonObject.get("layers").isJsonPrimitive() || jsonObject.get("layers").isJsonObject()) {
                        IModelLayer layer = ModelLayerManager.parseLayer(jsonObject.get("layers"));

                        if (layer != null) {
                            item.layers.add(layer);
                        }
                    } else if (jsonObject.get("layers").isJsonArray()) {
                        JsonArray layersArray = JSONUtils.getJsonArray(jsonObject, "layers");

                        for (int i = 0; i < layersArray.size(); i++) {
                            IModelLayer layer = ModelLayerManager.parseLayer(layersArray.get(i));
                            if (layer != null) {
                                item.layers.add(layer);
                            }
                        }
                    }
                }
            });
        });

        item.setDescription(JSONUtils.hasField(jsonObject, "description") ? ItemParser.parseDescriptionLines(jsonObject.get("description")) : null);

        return item.setAbilities(JSONUtils.hasField(jsonObject, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(jsonObject, "abilities"), true) : null);
    }

}
