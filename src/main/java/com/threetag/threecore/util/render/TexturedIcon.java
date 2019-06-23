package com.threetag.threecore.util.render;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class TexturedIcon implements IIcon {

    public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/icons.png");

    public final ResourceLocation texture;
    public final int u;
    public final int v;
    public final int width;
    public final int height;
    public final int textureWidth;
    public final int textureHeight;

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height) {
        this(texture, u, v, width, height, 256, 256);
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        mc.getTextureManager().bindTexture(this.texture);
        AbstractGui.blit(x, y, this.u, this.v, this.width, this.height, this.textureWidth, this.textureHeight);
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
            return new TexturedIcon(texture, u, v, width, height, textureWidth, textureHeight);
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
            return new TexturedIcon(texture, u, v, width, height, textureWidth, textureHeight);
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
            return nbt;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
