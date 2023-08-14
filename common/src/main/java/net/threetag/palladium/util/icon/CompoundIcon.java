package net.threetag.palladium.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Items;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class CompoundIcon implements IIcon {

    private final LinkedList<IIcon> icons = new LinkedList<>();

    @Override
    public void draw(Minecraft mc, DataContext context, PoseStack stack, int x, int y, int width, int height) {
        for (IIcon icon : this.icons) {
            icon.draw(mc, context, stack, x, y, width, height);
        }
    }

    @Override
    public IconSerializer<CompoundIcon> getSerializer() {
        return IconSerializers.COMPOUND.get();
    }

    public static class Serializer extends IconSerializer<CompoundIcon> {

        @Override
        public @NotNull CompoundIcon fromJSON(JsonObject json) {
            JsonArray icons = GsonHelper.getAsJsonArray(json, "icons");
            CompoundIcon compoundIcon = new CompoundIcon();

            for (JsonElement jsonElement : icons) {
                IIcon icon = IconSerializer.parseJSON(jsonElement);
                compoundIcon.icons.add(icon);
            }

            return compoundIcon;
        }

        @Override
        public CompoundIcon fromNBT(CompoundTag nbt) {
            ListTag listTag = nbt.getList("Icons", Tag.TAG_COMPOUND);
            CompoundIcon compoundIcon = new CompoundIcon();

            for (int i = 0; i < listTag.size(); i++) {
                IIcon icon = IconSerializer.parseNBT(listTag.getCompound(i));
                compoundIcon.icons.add(icon);
            }

            return compoundIcon;
        }

        @Override
        public JsonObject toJSON(CompoundIcon icon) {
            JsonObject jsonObject = new JsonObject();

            JsonArray jsonArray = new JsonArray();
            for (IIcon i : icon.icons) {
                jsonArray.add(IconSerializer.serializeJSON(i));
            }

            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(CompoundIcon icon) {
            CompoundTag nbt = new CompoundTag();
            ListTag listTag = new ListTag();

            for (IIcon i : icon.icons) {
                listTag.add(IconSerializer.serializeNBT(i));
            }

            nbt.put("Icons", listTag);
            return nbt;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Compound Icon");
            builder.setDescription("Let's you merge multiple icons into one.");

            JsonArray jsonArray = new JsonArray();
            jsonArray.add(IconSerializer.serializeJSON(new ItemIcon(Items.APPLE)));
            jsonArray.add(IconSerializer.serializeJSON(new TexturedIcon(new ResourceLocation("example:textures/icons/my_icon.png"))));
            builder.addProperty("icons", IIcon[].class)
                    .description("Array of the icons you want to merge")
                    .required().exampleJson(jsonArray);
        }
    }
}
