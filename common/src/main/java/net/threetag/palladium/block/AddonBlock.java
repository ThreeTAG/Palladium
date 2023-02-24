package net.threetag.palladium.block;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.BlockParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;

public class AddonBlock extends Block implements IAddonBlock {

    private String renderType = null;

    public AddonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setRenderType(String name) {
        this.renderType = name;
    }

    @Override
    public String getRenderType() {
        return this.renderType;
    }

    public static class Parser implements BlockParser.BlockTypeSerializer {

        @Override
        public IAddonBlock parse(JsonObject json, Properties properties) {
            return new AddonBlock(properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {

        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("default");
        }
    }

}
