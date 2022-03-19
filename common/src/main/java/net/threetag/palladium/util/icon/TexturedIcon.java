package net.threetag.palladium.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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
    public final int u;
    public final int v;
    public final int width;
    public final int height;
    public final int textureWidth;
    public final int textureHeight;
    public final Color tint;

    public TexturedIcon(ResourceLocation texture) {
        this(texture, 0, 0, 16, 16, 16, 16);
    }

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        this(texture, u, v, width, height, textureWidth, textureHeight, null);
    }

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight, Color tint) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.tint = tint;
    }

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height) {
        this(texture, u, v, width, height, 256, 256);
    }

    @Override
    public void draw(Minecraft mc, PoseStack stack, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        Lighting.setupForFlatItems();
        if (this.tint != null)
            RenderSystem.setShaderColor(this.tint.getRed() / 255F, this.tint.getGreen() / 255F, this.tint.getBlue() / 255F, 1.0F);
        GuiComponent.blit(stack, x, y, this.u, this.v, this.width, this.height, this.textureWidth, this.textureHeight);
        Lighting.setupFor3DItems();
        RenderSystem.enableDepthTest();
    }

    @Override
    public IconSerializer<?> getSerializer() {
        return IconSerializers.TEXTURE.get();
    }

    @Override
    public String toString() {
        return "TexturedIcon{" +
                "texture=" + texture +
                ", u=" + u +
                ", v=" + v +
                ", width=" + width +
                ", height=" + height +
                ", textureWidth=" + textureWidth +
                ", textureHeight=" + textureHeight +
                ", tint=" + tint +
                '}';
    }

    public static class Serializer extends IconSerializer<TexturedIcon> {

        @Override
        public @NotNull TexturedIcon fromJSON(JsonObject json) {
            ResourceLocation texture = new ResourceLocation(GsonHelper.getAsString(json, "texture"));
            int u = GsonHelper.getAsInt(json, "u", 0);
            int v = GsonHelper.getAsInt(json, "v", 0);
            int width = GsonHelper.getAsInt(json, "width", 16);
            int height = GsonHelper.getAsInt(json, "height", 16);
            int textureWidth = GsonHelper.getAsInt(json, "texture_width", 16);
            int textureHeight = GsonHelper.getAsInt(json, "texture_height", 16);
            Color tint = null;
            if (GsonHelper.isValidNode(json, "tint")) {
                int[] color = GsonUtil.getIntArray(json, 3, "tint", 255, 255, 255);
                tint = new Color(color[0], color[1], color[2]);
            }
            return new TexturedIcon(texture, u, v, width, height, textureWidth, textureHeight, tint);
        }

        @Override
        public TexturedIcon fromNBT(CompoundTag nbt) {
            ResourceLocation texture = new ResourceLocation(nbt.getString("Texture"));
            int u = nbt.getInt("U");
            int v = nbt.getInt("V");
            int width = nbt.getInt("Width");
            int height = nbt.getInt("Height");
            int textureWidth = nbt.getInt("TextureWidth");
            int textureHeight = nbt.getInt("TextureHeight");
            Color tint = null;
            if (nbt.contains("ColorRed") && nbt.contains("ColorGreen") && nbt.contains("ColorBlue")) {
                tint = new Color(nbt.getInt("ColorRed"), nbt.getInt("ColorGreen"), nbt.getInt("ColorBlue"));
            }
            return new TexturedIcon(texture, u, v, width, height, textureWidth, textureHeight, tint);
        }

        @Override
        public JsonObject toJSON(TexturedIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("texture", icon.texture.toString());
            if (icon.u != 0)
                jsonObject.addProperty("u", icon.u);
            if (icon.v != 0)
                jsonObject.addProperty("v", icon.v);
            if (icon.width != 16)
                jsonObject.addProperty("width", icon.width);
            if (icon.height != 16)
                jsonObject.addProperty("height", icon.height);
            if (icon.textureWidth != 16)
                jsonObject.addProperty("texture_width", icon.textureWidth);
            if (icon.textureHeight != 16)
                jsonObject.addProperty("texture_height", icon.textureHeight);
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
            nbt.putInt("U", icon.u);
            nbt.putInt("V", icon.v);
            nbt.putInt("Width", icon.width);
            nbt.putInt("Height", icon.height);
            nbt.putInt("TextureWidth", icon.textureWidth);
            nbt.putInt("TextureHeight", icon.textureHeight);
            if (icon.tint != null) {
                nbt.putInt("ColorRed", icon.tint.getRed());
                nbt.putInt("ColorGreen", icon.tint.getGreen());
                nbt.putInt("ColorBlue", icon.tint.getBlue());
            }
            return nbt;
        }
    }
}
