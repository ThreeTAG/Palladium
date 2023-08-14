package net.threetag.palladium.util.icon;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.NotNull;

public class ItemInSlotIcon implements IIcon {

    public final PlayerSlot slot;

    public ItemInSlotIcon(PlayerSlot slot) {
        this.slot = slot;
    }

    @Override
    public void draw(Minecraft mc, DataContext context, PoseStack stack, int x, int y, int width, int height) {
        stack.pushPose();
        stack.translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F, s / 16F);
        }

        var item = new ItemStack(Items.BARRIER);
        var items = this.slot.getItems(context.getLivingEntity());

        if (!items.isEmpty()) {
            var found = items.get(0);

            if (!found.isEmpty()) {
                item = found;
            }
        }

        GuiUtil.drawItem(stack, item, 0, true, null);
        stack.popPose();
    }

    @Override
    public IconSerializer<ItemInSlotIcon> getSerializer() {
        return IconSerializers.ITEM_IN_SLOT.get();
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "slot=" + this.slot.toString() + '}';
    }

    public static class Serializer extends IconSerializer<ItemInSlotIcon> {

        @Override
        public @NotNull ItemInSlotIcon fromJSON(JsonObject json) {
            return new ItemInSlotIcon(PlayerSlot.get(GsonHelper.getAsString(json, "slot")));
        }

        @Override
        public ItemInSlotIcon fromNBT(CompoundTag nbt) {
            return new ItemInSlotIcon(PlayerSlot.get(nbt.getString("Slot")));
        }

        @Override
        public JsonObject toJSON(ItemInSlotIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("slot", icon.slot.toString());
            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(ItemInSlotIcon icon) {
            CompoundTag tag = new CompoundTag();
            tag.putString("Slot", icon.slot.toString());
            return tag;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Item in Slot Icon");
            builder.setDescription("Uses the item that's in the specified slot.");

            builder.addProperty("slot", String.class)
                    .description("Name of the slot.")
                    .required().exampleJson(new JsonPrimitive("chest"));
        }
    }

}
