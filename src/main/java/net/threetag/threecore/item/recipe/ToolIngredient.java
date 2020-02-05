package net.threetag.threecore.item.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.item.HammerItem;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ToolIngredient extends Ingredient {

    public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "tool");
    private final ToolType toolType;

    public ToolIngredient(ToolType type) {
        super(Stream.of(new ItemList(type)));
        this.toolType = type;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return this.toolType.test(stack);
    }

    @Override
    public IIngredientSerializer<ToolIngredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<ToolIngredient> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public ToolIngredient parse(PacketBuffer buffer) {
            ToolType type = ToolType.fromName(buffer.readString(32767));
            return new ToolIngredient(type == null ? ToolType.ALL : type);
        }

        @Override
        public ToolIngredient parse(JsonObject json) {
            ToolType type = ToolType.fromName(JSONUtils.getString(json, "tool"));
            if (type == null)
                throw new JsonParseException("Unknown tool type '" + JSONUtils.getString(json, "tool") + "' in ingredient!");
            return new ToolIngredient(type);
        }

        @Override
        public void write(PacketBuffer buffer, ToolIngredient ingredient) {
            buffer.writeString(ingredient.toolType.getName());
        }
    }

    public static class ItemList implements IItemList {

        private final ToolType toolType;

        public ItemList(ToolType toolType) {
            this.toolType = toolType;
        }

        @Override
        public Collection<ItemStack> getStacks() {
            List<ItemStack> list = Lists.newArrayList();

            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                ItemStack stack = new ItemStack(item);

                if (this.toolType.test(stack)) {
                    list.add(stack);
                }
            }

            return list;
        }

        @Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("type", ID.toString());
            json.addProperty("tool", this.toolType.getName());
            return json;
        }
    }

    public enum ToolType implements IStringSerializable, Predicate<ItemStack> {

        ALL("all", s -> {
            for (ToolType type : values()) {
                if (!type.getName().equalsIgnoreCase("all") && type.test(s)) {
                    return true;
                }
            }
            return false;
        }),
        AXE("axe", s -> s.getToolTypes().contains(net.minecraftforge.common.ToolType.AXE)),
        PICKAXE("pickaxe", s -> s.getToolTypes().contains(net.minecraftforge.common.ToolType.PICKAXE)),
        SHOVEL("shovel", s -> s.getToolTypes().contains(net.minecraftforge.common.ToolType.SHOVEL)),
        SWORD("sword", s -> s.getItem() instanceof SwordItem),
        SHEARS("shears", s -> s.getItem() instanceof ShearsItem),
        HAMMER("hammer", s -> s.getItem() instanceof HammerItem);

        private final String name;
        private final Predicate<ItemStack> predicate;

        ToolType(String name, Predicate<ItemStack> predicate) {
            this.name = name;
            this.predicate = predicate;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean test(ItemStack stack) {
            return this.predicate.test(stack);
        }

        public static ToolType fromName(String name) {
            for (ToolType type : values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }

            return null;
        }
    }

}
