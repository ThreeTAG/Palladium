package net.threetag.palladium.item;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddonBlockItem extends BlockItem implements IAddonItem {

    private List<Component> tooltipLines;
    private RenderLayerContainer renderLayerContainer = null;
    private final AddonAttributeContainer attributeContainer = new AddonAttributeContainer();

    public AddonBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (this.tooltipLines != null) {
            tooltipComponents.addAll(this.tooltipLines);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return this.attributeContainer.get(PlayerSlot.get(slot), super.getDefaultAttributeModifiers(slot));
    }

    @Override
    public AddonAttributeContainer getAttributeContainer() {
        return this.attributeContainer;
    }

    @Override
    public void setTooltip(List<Component> lines) {
        this.tooltipLines = lines;
    }

    @Override
    public void setRenderLayerContainer(RenderLayerContainer container) {
        this.renderLayerContainer = container;
    }

    @Override
    public RenderLayerContainer getRenderLayerContainer() {
        return this.renderLayerContainer;
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            var blockId = GsonUtil.getAsResourceLocation(json, "block");

            if (!Registry.BLOCK.containsKey(blockId)) {
                throw new JsonParseException("Unknown block '" + blockId + "'");
            }

            return new AddonBlockItem(Registry.BLOCK.get(blockId), properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Block Item");
            builder.setDescription("Item for a block, duh");

            builder.addProperty("block", ResourceLocation.class)
                    .description("ID of the block that this item is for")
                    .required().exampleJson(new JsonPrimitive("test:test_block"));
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Palladium.MOD_ID, "block_item");
        }
    }
}
