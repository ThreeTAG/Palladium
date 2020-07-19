package net.threetag.threecore.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.TCJsonUtil;

import java.awt.*;

public class TexturedIcon implements IIcon {

    public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/icons.png");

    public final ResourceLocation texture;
    public final int u;
    public final int v;
    public final int width;
    public final int height;
    public final int textureWidth;
    public final int textureHeight;
    public final Color tint;

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.tint = null;
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

    //TODO fix rendering
    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(Minecraft mc, MatrixStack stack, int x, int y) {
        stack.push();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderHelper.setupGuiFlatDiffuseLighting();
        mc.getTextureManager().bindTexture(this.texture);
//        if (this.tint != null)
//            RenderSystem.color3f(this.tint.getRed() / 255F, this.tint.getGreen() / 255F, this.tint.getBlue() / 255F);
        AbstractGui.func_238463_a_(stack, x, y, this.u, this.v, this.width, this.height, this.textureWidth, this.textureHeight);
        RenderHelper.setupGui3DDiffuseLighting();
        RenderSystem.enableDepthTest();
        stack.pop();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public IIconSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIconSerializer<TexturedIcon> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "texture");

        @Override
        public TexturedIcon read(JsonObject json) {
            ResourceLocation texture = new ResourceLocation(JSONUtils.getString(json, "texture"));
            int u = JSONUtils.getInt(json, "u");
            int v = JSONUtils.getInt(json, "v");
            int width = JSONUtils.getInt(json, "width", 16);
            int height = JSONUtils.getInt(json, "height", 16);
            int textureWidth = JSONUtils.getInt(json, "texture_width", 256);
            int textureHeight = JSONUtils.getInt(json, "texture_height", 256);
            Color tint = null;
            if (JSONUtils.hasField(json, "tint")) {
                int[] color = TCJsonUtil.getIntArray(json, 3, "tint", 255, 255, 255);
                tint = new Color(color[0], color[1], color[2]);
            }
            return new TexturedIcon(texture, u, v, width, height, textureWidth, textureHeight, tint);
        }

        @Override
        public TexturedIcon read(CompoundNBT nbt) {
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
        public CompoundNBT serialize(TexturedIcon icon) {
            CompoundNBT nbt = new CompoundNBT();
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

        @Override
        public JsonObject serializeJson(TexturedIcon icon) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("texture", icon.texture.toString());
            jsonObject.addProperty("u", icon.u);
            jsonObject.addProperty("v", icon.v);
            jsonObject.addProperty("width", icon.width);
            jsonObject.addProperty("height", icon.height);
            jsonObject.addProperty("texture_width", icon.textureWidth);
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
        public ResourceLocation getId() {
            return ID;
        }
    }
}
