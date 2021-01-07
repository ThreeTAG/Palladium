package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.NonNullFunction;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.client.renderer.entity.modellayer.texture.DefaultModelTexture;
import net.threetag.threecore.client.renderer.entity.modellayer.texture.ModelLayerTexture;
import net.threetag.threecore.util.RenderUtil;
import net.threetag.threecore.util.SupporterHandler;
import net.threetag.threecore.util.documentation.IDocumentationSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CapeModelLayer implements IModelLayer {

    public final ModelLayerTexture texture;
    public final List<IModelLayerPredicate> glowPredicates;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();
    public final boolean allowSupporterCloakOverride;

    public CapeModelLayer(ModelLayerTexture texture) {
        this.texture = texture;
        this.glowPredicates = Collections.emptyList();
        this.allowSupporterCloakOverride = true;
    }

    public CapeModelLayer(ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates) {
        this.texture = texture;
        this.glowPredicates = glowPredicates;
        this.allowSupporterCloakOverride = true;
    }

    public CapeModelLayer(ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates, boolean allowSupporterCloakOverride) {
        this.texture = texture;
        this.glowPredicates = glowPredicates;
        this.allowSupporterCloakOverride = allowSupporterCloakOverride;
    }

    @Override
    public void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityRenderer != null && entityRenderer.getEntityModel() instanceof BipedModel && context.getAsEntity() instanceof LivingEntity) {

            if (((LivingEntity) context.getAsEntity()).getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem) {
                return;
            }

            if (context.getAsEntity() instanceof ClientPlayerEntity && ((PlayerEntity) context.getAsEntity()).isWearing(PlayerModelPart.CAPE) && ((ClientPlayerEntity) context.getAsEntity()).getLocationCape() != null) {
                return;
            }

            matrixStack.push();
            float rotation = 0F;

            if (context.getAsEntity() instanceof LivingEntity && ((LivingEntity) context.getAsEntity()).isElytraFlying()) {
                rotation = 0F;
            } else if (context.getAsEntity() instanceof PlayerEntity) {
                PlayerEntity entity = (PlayerEntity) context.getAsEntity();
                double d0 = MathHelper.lerp(partialTicks, entity.prevChasingPosX, entity.chasingPosX) - MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX());
                double d1 = MathHelper.lerp(partialTicks, entity.prevChasingPosY, entity.chasingPosY) - MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY());
                double d2 = MathHelper.lerp(partialTicks, entity.prevChasingPosZ, entity.chasingPosZ) - MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ());
                float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset);
                double d3 = MathHelper.sin(f * ((float) Math.PI / 180F));
                double d4 = -MathHelper.cos(f * ((float) Math.PI / 180F));
                float f1 = (float) d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = MathHelper.lerp(partialTicks, entity.prevCameraYaw, entity.cameraYaw);
                f1 = f1 + MathHelper.sin(MathHelper.lerp(partialTicks, entity.prevDistanceWalkedModified, entity.distanceWalkedModified) * 6.0F) * 32.0F * f4;

                rotation = 6.0F + f2 / 2.0F + f1;
            }

            ((BipedModel) entityRenderer.getEntityModel()).bipedBody.translateRotate(matrixStack);
            matrixStack.translate(0, -0.02F, 0.2F);

            IVertexBuilder vertex = ItemRenderer.getEntityGlintVertexBuilder(renderTypeBuffer, RenderType.getEntityTranslucent(this.getTexture(context).getTexture(context)), false, context.getAsItem() != null && context.getAsItem().hasEffect());
            int color = getColor(context);
            if (color > -1) {
                renderCape(context, matrixStack, vertex, rotation, RenderUtil.red(color), RenderUtil.green(color), RenderUtil.blue(color), packedLight, partialTicks);
            } else {
                renderCape(context, matrixStack, vertex, rotation, 255, 255, 255, packedLight, partialTicks);
            }

            matrixStack.pop();
        }
    }

    public ModelLayerTexture getTexture(IModelLayerContext context) {
        if (context.getAsEntity() instanceof PlayerEntity) {
            ResourceLocation cloak = SupporterHandler.getPlayerData(context.getAsEntity().getUniqueID()).getCloakTexture();

            if (cloak != null) {
                return new DefaultModelTexture(null, null) {
                    @Override
                    public ResourceLocation getTexture(IModelLayerContext context) {
                        return cloak;
                    }
                };
            }
        }
        return this.texture;
    }

    public int getColor(IModelLayerContext context) {
        return -1;
    }

    public void renderCape(IModelLayerContext context, MatrixStack matrixStack, IVertexBuilder vertex, float rotation, int red, int green, int blue, int packedLight, float partialTicks) {
        drawVertex(matrixStack, vertex, 0.4F, 0, 0, 0F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0, 0, 14F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0, -0.4F, 14F / 64F, 0F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, 0.4F, 0, -0.4F, 0F, 0F, 0, 1, 0, packedLight, red, green, blue);

        drawVertex(matrixStack, vertex, 0.4F, 0.0001F, 0, 14F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0.0001F, 0, 28F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0.0001F, -0.4F, 28F / 64F, 0F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, 0.4F, 0.0001F, -0.4F, 14F / 64F, 0F, 0, 1, 0, packedLight, red, green, blue);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(rotation));

        drawVertex(matrixStack, vertex, 0.4F, 0, 0, 0F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0, 0, 14F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 1.2F, 0, 14F / 64F, 1F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, 0.4F, 1.2F, 0, 0F, 1F, 0, 1, 0, packedLight, red, green, blue);

        drawVertex(matrixStack, vertex, 0.4F, 0, -0.0001F, 14F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 0, -0.0001F, 28F / 64F, 8F / 32F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, -0.4F, 1.2F, -0.0001F, 28F / 64F, 1F, 0, 1, 0, packedLight, red, green, blue);
        drawVertex(matrixStack, vertex, 0.4F, 1.2F, -0.0001F, 14F / 64F, 1F, 0, 1, 0, packedLight, red, green, blue);
    }

    public void drawVertex(MatrixStack matrixStack, IVertexBuilder vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, int normalX, int normalY, int normalZ, int packedLightIn, int red, int green, int blue) {
        vertexBuilder.pos(matrixStack.getLast().getMatrix(), offsetX, offsetY, offsetZ).color(red, green, blue, 255).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(matrixStack.getLast().getNormal(), (float) normalX, (float) normalZ, (float) normalY).endVertex();
    }

    @Override
    public boolean isActive(IModelLayerContext context) {
        return ModelLayerManager.arePredicatesFulFilled(this.predicateList, context);
    }

    @Override
    public CapeModelLayer addPredicate(IModelLayerPredicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    public static class Parser implements NonNullFunction<JsonObject, IModelLayer>, IDocumentationSettings {

        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "cape");

        @Nonnull
        @Override
        public IModelLayer apply(@Nonnull JsonObject json) {
            List<IModelLayerPredicate> glowPredicates = Lists.newLinkedList();

            if (JSONUtils.hasField(json, "glow")) {
                JsonElement glowJson = json.get("glow");

                if (glowJson.isJsonPrimitive() && glowJson.getAsBoolean()) {
                    glowPredicates.add((c) -> true);
                } else {
                    JsonArray predicateArray = JSONUtils.getJsonArray(json, "glow");
                    for (int i = 0; i < predicateArray.size(); i++) {
                        IModelLayerPredicate predicate = ModelLayerManager.parsePredicate(predicateArray.get(i).getAsJsonObject());
                        if (predicate != null)
                            glowPredicates.add(predicate);
                    }
                }
            } else {
                glowPredicates.add((c) -> false);
            }

            return new CapeModelLayer(ModelLayerTexture.parse(json.get("texture")), glowPredicates, JSONUtils.getBoolean(json, "allow_supporter_cloak_override", true));
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public List<String> getColumns() {
            return Arrays.asList("Setting", "Type", "Description", "Required", "Fallback Value");
        }

        @Override
        public List<Iterable<?>> getRows() {
            List<Iterable<?>> rows = new ArrayList<>();
            rows.add(Arrays.asList("texture", ModelLayerTexture.class, "Texture object", true, null));
            rows.add(Arrays.asList("glow", IModelLayerPredicate[].class, "Array of conditions for the glow to appear OR just a boolean value (true/false)", false, false));
            rows.add(Arrays.asList("allow_supporter_cloak_override", Boolean.class, "If true, the texture of this cape can be overriden be Supporter Cloaks", false, true));
            return rows;
        }

        @Override
        public JsonElement getExampleJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", ID.toString());
            jsonObject.addProperty("texture", "pack:textures/model/my_texture.png");
            jsonObject.addProperty("glow", false);
            jsonObject.addProperty("allow_supporter_cloak_override", true);
            return jsonObject;
        }
    }
}
