package net.threetag.palladium.util.icon;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.json.GsonUtil;

public class ItemIcon implements IIcon {

    public final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    public ItemIcon(ItemLike itemLike) {
        this.stack = new ItemStack(itemLike);
    }

    @Override
    public void draw(Minecraft mc, PoseStack stack, int x, int y, int width, int height) {
        stack.pushPose();
        stack.translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F, s / 16F);
        }

        GuiUtil.drawItem(stack, this.stack, 0, true, null);
        stack.popPose();
    }

    @Override
    public IconSerializer<?> getSerializer() {
        return IconSerializers.ITEM.get();
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "stack=" + stack + '}';
    }

    public static class Serializer extends IconSerializer<ItemIcon> {

        @Override
        public ItemIcon fromJSON(JsonObject json) {
            return new ItemIcon(GsonUtil.readItemStack(json));
        }

        @Override
        public ItemIcon fromNBT(CompoundTag nbt) {
            return new ItemIcon(ItemStack.of(nbt));
        }

        @Override
        public JsonObject toJSON(ItemIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registry.ITEM.getKey(icon.stack.getItem()).toString());
            jsonObject.addProperty("count", icon.stack.getCount());
            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(ItemIcon icon) {
            return icon.stack.save(new CompoundTag());
        }
    }

}
