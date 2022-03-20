package net.threetag.palladium.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TexturedIcon implements IIcon {

    public static final ResourceLocation LOCK = new ResourceLocation(Palladium.MOD_ID, "textures/icons/lock.png");

    public final ResourceLocation texture;
    public final Color tint;

    public TexturedIcon(ResourceLocation texture) {
        this(texture, null);
    }

    public TexturedIcon(ResourceLocation texture, Color tint) {
        this.texture = texture;
        this.tint = tint;
    }

    @Override
    public void draw(Minecraft mc, PoseStack stack, int x, int y, int w, int h) {
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        var tesselator = Tesselator.getInstance();
        var buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        var m = stack.last().pose();
        var color = this.tint != null ? this.tint : Color.WHITE;
        var r = color.getRed();
        var g = color.getGreen();
        var b = color.getBlue();
        var a = color.getAlpha();
        buffer.vertex(m, x, y + h, 0).color(r, g, b, a).uv(0, 1).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(r, g, b, a).uv(1, 1).endVertex();
        buffer.vertex(m, x + w, y, 0).color(r, g, b, a).uv(1, 0).endVertex();
        buffer.vertex(m, x, y, 0).color(r, g, b, a).uv(0, 0).endVertex();
        tesselator.end();
    }

    @Override
    public IconSerializer<?> getSerializer() {
        return IconSerializers.TEXTURE.get();
    }

    @Override
    public String toString() {
        return "TexturedIcon{" +
                "texture=" + texture +
                ", tint=" + tint +
                '}';
    }

    public static class Serializer extends IconSerializer<TexturedIcon> {

        @Override
        public @NotNull TexturedIcon fromJSON(JsonObject json) {
            ResourceLocation texture = new ResourceLocation(GsonHelper.getAsString(json, "texture"));
            Color tint = null;
            if (GsonHelper.isValidNode(json, "tint")) {
                int[] color = GsonUtil.getIntArray(json, 3, "tint", 255, 255, 255);
                tint = new Color(color[0], color[1], color[2]);
            }
            return new TexturedIcon(texture, tint);
        }

        @Override
        public TexturedIcon fromNBT(CompoundTag nbt) {
            ResourceLocation texture = new ResourceLocation(nbt.getString("Texture"));
            Color tint = null;
            if (nbt.contains("ColorRed") && nbt.contains("ColorGreen") && nbt.contains("ColorBlue")) {
                tint = new Color(nbt.getInt("ColorRed"), nbt.getInt("ColorGreen"), nbt.getInt("ColorBlue"));
            }
            return new TexturedIcon(texture, tint);
        }

        @Override
        public JsonObject toJSON(TexturedIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("texture", icon.texture.toString());
            if (icon.tint != null) {
                JsonArray array = new JsonArray();
                array.add(icon.tint.getRed());
                array.add(icon.tint.getGreen());
                array.add(icon.tint.getBlue());
                jsonObject.add("tint", array);
            }
            return jsonObject;
        }

        @Override
        public CompoundTag toNBT(TexturedIcon icon) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Texture", icon.texture.toString());
            if (icon.tint != null) {
                nbt.putInt("ColorRed", icon.tint.getRed());
                nbt.putInt("ColorGreen", icon.tint.getGreen());
                nbt.putInt("ColorBlue", icon.tint.getBlue());
            }
            return nbt;
        }
    }
}
