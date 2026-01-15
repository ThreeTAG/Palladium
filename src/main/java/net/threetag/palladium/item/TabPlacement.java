package net.threetag.palladium.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public record TabPlacement(Type type, ResourceKey<CreativeModeTab> tab, @Nullable Identifier referencedItem) {

    private static final Codec<TabPlacement> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Type.CODEC.optionalFieldOf("type", Type.ADD).forGetter(TabPlacement::type),
            ResourceKey.codec(Registries.CREATIVE_MODE_TAB).fieldOf("tab").forGetter(TabPlacement::tab),
            Identifier.CODEC.optionalFieldOf("target").forGetter(p -> Optional.ofNullable(p.referencedItem))
    ).apply(instance, (type, tab, item) -> new TabPlacement(type, tab, item.orElse(null))));

    public static final Codec<TabPlacement> CODEC = Codec.either(DIRECT_CODEC, ResourceKey.codec(Registries.CREATIVE_MODE_TAB)).xmap(
            either -> either.map(
                    tabPlacement -> tabPlacement,
                    id -> new TabPlacement(Type.ADD, id, null)
            ),
            tabPlacement -> tabPlacement.type() != Type.ADD ? Either.left(tabPlacement) : Either.right(tabPlacement.tab())
    );

    @SubscribeEvent
    static void placeInTabs(BuildCreativeModeTabContentsEvent e) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (((PalladiumItemExtension) item).palladium$getProperties() instanceof ItemPropertiesCodec.ExtendedProperties properties) {
                for (TabPlacement tab : properties.getTabs()) {
                    if (tab.tab.equals(e.getTabKey())) {
                        if (tab.type == Type.ADD) {
                            e.accept(item);
                        } else {
                            var refItem = BuiltInRegistries.ITEM.getOptional(tab.referencedItem);

                            if (refItem.isEmpty() || refItem.get() == Items.AIR) {
                                AddonPackLog.warning("Could not find item with id " + tab.referencedItem);
                                return;
                            }

                            if (tab.type == Type.ADD_AFTER) {
                                e.insertAfter(refItem.get().getDefaultInstance(), item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                            } else {
                                e.insertBefore(refItem.get().getDefaultInstance(), item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                            }
                        }
                    }
                }
            }
        }
    }

    public enum Type implements StringRepresentable {

        ADD("add"),
        ADD_AFTER("add_after"),
        ADD_BEFORE("add_before");

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
