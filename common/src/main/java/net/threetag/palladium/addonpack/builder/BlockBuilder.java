package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.threetag.palladium.addonpack.parser.BlockParser;
import net.threetag.palladium.block.AddonBlock;
import net.threetag.palladium.block.IAddonBlock;
import net.threetag.palladium.util.Utils;

public class BlockBuilder extends AddonBuilder<Block> {

    private final JsonObject json;
    private BlockParser.BlockTypeSerializer typeSerializer = null;
    private Material material;
    private MaterialColor materialColor;
    private SoundType soundType;
    private float destroyTime;
    private float explosionResistance;
    private boolean noOcclusion = false;
    private boolean requiresCorrectToolForDrops = false;
    private String renderType = null;

    public BlockBuilder(ResourceLocation id, JsonObject json) {
        super(id);
        this.json = json;
    }

    @Override
    protected Block create() {
        var material = Utils.orElse(this.material, Material.STONE);
        var materialColor = Utils.orElse(this.materialColor, material.getColor());
        var properties = BlockBehaviour.Properties.of(material, materialColor)
                .strength(this.destroyTime, this.explosionResistance);

        if (this.soundType != null) {
            properties.sound(this.soundType);
        }

        if (this.noOcclusion) {
            properties.noOcclusion();
        }

        if (this.requiresCorrectToolForDrops) {
            properties.requiresCorrectToolForDrops();
        }

        IAddonBlock block = this.typeSerializer != null ? this.typeSerializer.parse(this.json, properties) : new AddonBlock(properties);

        block.setRenderType(this.renderType);

        return (Block) block;
    }

    public BlockBuilder type(BlockParser.BlockTypeSerializer serializer) {
        this.typeSerializer = serializer;
        return this;
    }

    public BlockBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public BlockBuilder materialColor(MaterialColor color) {
        this.materialColor = color;
        return this;
    }

    public BlockBuilder soundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public BlockBuilder destroyTime(float destroyTime) {
        this.destroyTime = destroyTime;
        return this;
    }

    public BlockBuilder explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }

    public BlockBuilder renderType(String name) {
        this.renderType = name;
        return this;
    }

    public BlockBuilder noOcclusion() {
        this.noOcclusion = true;
        return this;
    }

    public BlockBuilder requiresCorrectToolForDrops() {
        this.requiresCorrectToolForDrops = true;
        return this;
    }

}
