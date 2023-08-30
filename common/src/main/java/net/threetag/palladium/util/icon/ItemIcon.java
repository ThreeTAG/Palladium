package net.threetag.palladium.util.icon;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;

public class ItemIcon implements IIcon {

    public final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    public ItemIcon(ItemLike itemLike) {
        this.stack = new ItemStack(itemLike);
    }

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            guiGraphics.pose().scale(s / 16F, s / 16F, s / 16F);
        }

        var item = this.stack;

        if (item.isEmpty()) {
            var contextItem = context.getItem();

            if(!contextItem.isEmpty()) {
                item = contextItem;
            } else {
                item = new ItemStack(Items.BARRIER);
            }
        }

        GuiUtil.drawItem(guiGraphics, item, 0, true, null);
        guiGraphics.pose().popPose();
    }

    @Override
    public IconSerializer<ItemIcon> getSerializer() {
        return IconSerializers.ITEM.get();
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "stack=" + stack + '}';
    }

    public static class Serializer extends IconSerializer<ItemIcon> {

        @Override
        public @NotNull ItemIcon fromJSON(JsonObject json) {
            return new ItemIcon(json.has("item") ? GsonUtil.readItemStack(json) : ItemStack.EMPTY);
        }

        @Override
        public ItemIcon fromNBT(CompoundTag nbt) {
            return new ItemIcon(ItemStack.of(nbt));
        }

        @Override
        public JsonObject toJSON(ItemIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", BuiltInRegistries.ITEM.getKey(icon.stack.getItem()).toString());
            jsonObject.addProperty("count", icon.stack.getCount());
            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(ItemIcon icon) {
            return icon.stack.save(new CompoundTag());
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Item Icon");
            builder.setDescription("Uses an item as an icon.");

            builder.addProperty("item", ResourceLocation.class)
                    .description("ID of the item that's supposed to be displayed. If you leave it out, it will display the item from the current context (if given).")
                    .fallback(new ResourceLocation("minecraft:air")).exampleJson(new JsonPrimitive("minecraft:apple"));
        }
    }

}
