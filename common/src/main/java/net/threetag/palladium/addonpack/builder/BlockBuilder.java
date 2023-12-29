package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.BlockParser;
import net.threetag.palladium.block.AddonBlock;
import net.threetag.palladium.block.IAddonBlock;

public class BlockBuilder extends AddonBuilder<Block> {

    private final JsonObject json;
    private ResourceLocation typeSerializerId = null;
    private MapColor mapColor;
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
        var properties = BlockBehaviour.Properties.of().mapColor(this.mapColor)
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

        if (this.typeSerializerId == null) {
            this.typeSerializerId = BlockParser.FALLBACK_SERIALIZER;
        }

        BlockParser.BlockTypeSerializer serializer = BlockParser.getTypeSerializer(this.typeSerializerId);

        if (serializer == null) {
            AddonPackLog.warning("Unknown block type '" + this.typeSerializerId + "', falling back to '" + BlockParser.FALLBACK_SERIALIZER + "'");
        }

        IAddonBlock block = serializer != null ? serializer.parse(this.json, properties) : new AddonBlock(properties);

        block.setRenderType(this.renderType);

        return (Block) block;
    }

    public BlockBuilder type(ResourceLocation serializerId) {
        this.typeSerializerId = serializerId;
        return this;
    }

    public BlockBuilder mapColor(MapColor color) {
        this.mapColor = color;
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
