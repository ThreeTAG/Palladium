package net.threetag.threecore.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.threetag.threecore.ThreeCore;

public class CompoundIcon implements IIcon {

    private IIcon[] icons;
    private int width, height;

    public CompoundIcon(IIcon... icons) {
        this.icons = icons;

        for (IIcon icon : this.icons) {
            if (icon.getWidth() > this.width) {
                this.width = icon.getWidth();
            }
            if (icon.getHeight() > this.height) {
                this.height = icon.getHeight();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(Minecraft mc, MatrixStack stack, int x, int y) {
        for (IIcon icon : this.icons) {
            icon.draw(mc, stack, x, y);
        }
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

    public static class Serializer implements IIconSerializer<CompoundIcon> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "compound");

        @Override
        public CompoundIcon read(JsonObject json) {
            JsonArray jsonArray = JSONUtils.getJsonArray(json, "icons");
            IIcon[] icons = new IIcon[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                icons[i] = IconSerializer.deserialize(jsonArray.get(i).getAsJsonObject());
            }
            return new CompoundIcon(icons);
        }

        @Override
        public CompoundIcon read(CompoundNBT nbt) {
            ListNBT list = nbt.getList("Icons", Constants.NBT.TAG_COMPOUND);
            IIcon[] icons = new IIcon[list.size()];
            for (int i = 0; i < list.size(); i++) {
                icons[i] = IconSerializer.deserialize(list.getCompound(i));
            }
            return new CompoundIcon(icons);
        }

        @Override
        public CompoundNBT serialize(CompoundIcon icon) {
            ListNBT list = new ListNBT();
            CompoundNBT nbt = new CompoundNBT();
            for (IIcon i : icon.icons) {
                list.add(i.getSerializer().serializeExt(i));
            }
            nbt.put("Icons", list);
            return nbt;
        }

        @Override
        public JsonObject serializeJson(CompoundIcon icon) {
            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            for (IIcon i : icon.icons) {
                jsonArray.add(i.getSerializer().serializeJsonExt(i));
            }
            jsonObject.add("icons", jsonArray);
            return jsonObject;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
