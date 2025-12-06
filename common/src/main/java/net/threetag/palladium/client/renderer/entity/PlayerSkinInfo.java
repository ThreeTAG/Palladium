package net.threetag.palladium.client.renderer.entity;

import net.minecraft.resources.ResourceLocation;

public class PlayerSkinInfo {

    private String skinModel;
    private ResourceLocation texture;
    private boolean failed;

    public PlayerSkinInfo() {
        this.skinModel = null;
        this.texture = null;
    }

    public PlayerSkinInfo(String skinModel, ResourceLocation texture) {
        this.skinModel = skinModel;
        this.texture = texture;
    }

    public boolean isLoading() {
        return this.texture == null;
    }

    public void set(String skinModel, ResourceLocation texture) {
        this.skinModel = skinModel;
        this.texture = texture;
    }

    public boolean hasFailed() {
        return this.failed;
    }

    public void setFailed() {
        this.failed = true;
    }

    public String getModelName() {
        return this.skinModel;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

}
