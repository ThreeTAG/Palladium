package com.threetag.threecore.util.render;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class TexturedIcon implements IIcon {

    public final ResourceLocation texture;
    public final int u;
    public final int v;
    public final int width;
    public final int height;

    public TexturedIcon(ResourceLocation texture, int u, int v, int width, int height) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Minecraft mc, int x, int y) {
        mc.getTextureManager().bindTexture(this.texture);
        Gui.drawModalRectWithCustomSizedTexture(x, y, this.u, this.v, this.width, this.height, this.width, this.height);
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
            ResourceLocation texture = new ResourceLocation(JsonUtils.getString(json, "texture"));
            int u = JsonUtils.getInt(json, "u");
            int v = JsonUtils.getInt(json, "v");
            int width = JsonUtils.getInt(json, "width", 16);
            int height = JsonUtils.getInt(json, "height", 16);
            return new TexturedIcon(texture, u, v, width, height);
        }

        @Override
        public TexturedIcon read(NBTTagCompound nbt) {
            ResourceLocation texture = new ResourceLocation(nbt.getString("Texture"));
            int u = nbt.getInt("U");
            int v = nbt.getInt("V");
            int width = nbt.getInt("Width");
            int height = nbt.getInt("Height");
            return new TexturedIcon(texture, u, v, width, height);
        }

        @Override
        public NBTTagCompound serialize(TexturedIcon icon) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.putString("Texture", icon.texture.toString());
            nbt.putInt("U", icon.u);
            nbt.putInt("V", icon.v);
            nbt.putInt("Width", icon.width);
            nbt.putInt("Height", icon.height);
            return nbt;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
