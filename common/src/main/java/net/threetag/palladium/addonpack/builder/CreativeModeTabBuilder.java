package net.threetag.palladium.addonpack.builder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

public class CreativeModeTabBuilder extends AddonBuilder<CreativeModeTab> {

    private ResourceLocation iconItemId;
    private Component title;

    public CreativeModeTabBuilder(ResourceLocation id) {
        super(id);
        this.title = Component.translatable("itemGroup." + id.getNamespace() + "." + id.getPath());
    }

    @Override
    protected CreativeModeTab create() {
        return CreativeModeTabRegistry.create(builder -> {
            builder.icon(() -> new ItemStack(BuiltInRegistries.ITEM.get(this.iconItemId)));
            builder.title(this.title);
            // TODO more settings?
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
}
