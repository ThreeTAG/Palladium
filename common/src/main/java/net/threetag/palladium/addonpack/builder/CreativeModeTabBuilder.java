package net.threetag.palladium.addonpack.builder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.LinkedList;

public class CreativeModeTabBuilder extends AddonBuilder<CreativeModeTab, CreativeModeTabBuilder> {

    private ResourceLocation iconItemId;
    private Component title;
    private LinkedList<ResourceLocation> itemIds;

    public CreativeModeTabBuilder(ResourceLocation id) {
        super(id);
        this.title = Component.translatable("itemGroup." + id.getNamespace() + "." + id.getPath());
    }

    @Override
    protected CreativeModeTab create() {
        return CreativeModeTabRegistry.create(builder -> {
            builder.icon(() -> new ItemStack(BuiltInRegistries.ITEM.get((ResourceLocation) this.getValue(b -> b.iconItemId))));
            builder.title(this.getValue(b -> b.title));
            builder.displayItems((itemDisplayParameters, output) -> {
                for (ResourceLocation itemId : this.getValue(b -> b.itemIds, new LinkedList<ResourceLocation>())) {
                    var item = BuiltInRegistries.ITEM.get(itemId);

                    if (item != Items.AIR) {
                        output.accept(item);
                    } else {
                        AddonPackLog.warning("Tried to add unknown item '" + itemId + "' to creative mode tab '" + this.getId() + "'");
                    }
                }
            });
        });
    }

    public CreativeModeTabBuilder itemIconId(ResourceLocation iconItemId) {
        this.iconItemId = iconItemId;
        return this;
    }

    public CreativeModeTabBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public CreativeModeTabBuilder addItem(ResourceLocation itemId) {
        if (this.itemIds == null) {
            this.itemIds = new LinkedList<>();
        }
        this.itemIds.add(itemId);
        return this;
    }
}
