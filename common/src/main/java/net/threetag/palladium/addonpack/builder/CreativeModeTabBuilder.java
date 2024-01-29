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

public class CreativeModeTabBuilder extends AddonBuilder<CreativeModeTab> {

    private ResourceLocation iconItemId;
    private Component title;
    private final LinkedList<ResourceLocation> itemIds = new LinkedList<>();

    public CreativeModeTabBuilder(ResourceLocation id) {
        super(id);
        this.title = Component.translatable("itemGroup." + id.getNamespace() + "." + id.getPath());
    }

    @Override
    protected CreativeModeTab create() {
        return CreativeModeTabRegistry.create(builder -> {
            builder.icon(() -> new ItemStack(BuiltInRegistries.ITEM.get(this.iconItemId)));
            builder.title(this.title);
            builder.displayItems((itemDisplayParameters, output) -> {
                for (ResourceLocation itemId : this.itemIds) {
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
        this.itemIds.add(itemId);
        return this;
    }
}
