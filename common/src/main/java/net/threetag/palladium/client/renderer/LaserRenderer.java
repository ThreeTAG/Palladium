package net.threetag.palladium.client.renderer;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Vector2f;

import java.awt.*;

public class LaserRenderer {

    private DynamicColor glowColor;
    private DynamicColor coreColor = DynamicColor.staticColor(Color.WHITE);
    private float rainbow = 0F;
    private float glowOpacity = 1F;
    private float coreOpacity = 1F;
    private int bloom = 3;
    private Vector2f size = new Vector2f(1F / 16F, 1F / 16F);
    private float length = 1F;
    private boolean normalTransparency = false;
    private float rotation = 0F;
    private float rotationSpeed = 0F;
    private float opacityAndSizeModifier = 1F;

    public LaserRenderer(DynamicColor color) {
        this.glowColor = color;
    }

    public LaserRenderer(DynamicColor glowColor, DynamicColor coreColor) {
        this.glowColor = glowColor;
        this.coreColor = coreColor;
    }

    public LaserRenderer color(DynamicColor color) {
        this.glowColor = color;
        return this;
    }

    public LaserRenderer color(DynamicColor glowColor, DynamicColor coreColor) {
        this.glowColor = glowColor;
        this.coreColor = coreColor;
        return this;
    }

    public DynamicColor getCoreColor() {
        return this.coreColor;
    }

    public DynamicColor getGlowColor() {
        return this.glowColor;
    }

    public LaserRenderer enableRainbow(float speed) {
        this.rainbow = speed;
        return this;
    }

    public float getRainbowSpeed() {
        return this.rainbow;
    }

    public LaserRenderer opacity(float opacity) {
        this.glowOpacity = this.coreOpacity = opacity;
        return this;
    }

    public LaserRenderer opacity(float glowOpacity, float coreOpacity) {
        this.glowOpacity = glowOpacity;
        this.coreOpacity = coreOpacity;
        return this;
    }

    public float getCoreOpacity() {
        return this.coreOpacity;
    }

    public float getGlowOpacity() {
        return this.glowOpacity;
    }

    public LaserRenderer bloom(int bloom) {
        this.bloom = Mth.clamp(bloom, 0, 10);
        return this;
    }

    public int getBloom() {
        return this.bloom;
    }

    public LaserRenderer size(float size) {
        return this.size(size, size);
    }

    public LaserRenderer size(float width, float height) {
        this.size = new Vector2f(width, height);
        return this;
    }

    public LaserRenderer size(Vector2f size) {
        this.size = size;
        return this;
    }

    public Vector2f getSize() {
        return this.size;
    }

    public LaserRenderer length(float length) {
        this.length = length;
        return this;
    }

    public float getLength() {
        return this.length;
    }

    public LaserRenderer normalTransparency() {
        this.normalTransparency = true;
        return this;
    }

    public LaserRenderer normalTransparency(boolean normalTransparency) {
        this.normalTransparency = normalTransparency;
        return this;
    }

    public boolean hasNormalTransparency() {
        return this.normalTransparency;
    }

    public LaserRenderer rotate(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getRotation() {
        return this.rotation;
    }

    public LaserRenderer rotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
        return this;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public LaserRenderer opacityAndSizeModifier(float modifier) {
        this.opacityAndSizeModifier = modifier;
        return this;
    }

    public void face(PoseStack poseStack, Vec3 origin, Vec3 target) {
        RenderUtil.faceVec(poseStack, origin, target);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
    }

    public void faceAndRender(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, Vec3 origin, Vec3 target, int ticks, float partialTick) {
        poseStack.pushPose();
        this.face(poseStack, origin, target);
        this.render(context, poseStack, bufferSource, ticks, partialTick);
        poseStack.popPose();
    }

    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, int ticks, float partialTick) {
        var rot = this.rotation;

        if (this.rotationSpeed > 0F) {
            rot += (ticks + partialTick) * rotationSpeed;
        }

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot % 360F));

        var consumer = bufferSource.getBuffer(this.normalTransparency ? PalladiumRenderTypes.LASER_NORMAL_TRANSPARENCY : PalladiumRenderTypes.LASER);
        var size = new Vector2f(this.size).mul(this.opacityAndSizeModifier);
        AABB box = new AABB(-size.x / 2F, 0, -size.y / 2F, size.x / 2F, this.length, size.y / 2F);

        if (this.coreOpacity > 0F) {
            var coreColor = this.coreColor.getColor(context);
            RenderUtil.renderFilledBox(poseStack, consumer, box, coreColor.getRed() / 255F, coreColor.getGreen() / 255F, coreColor.getBlue() / 255F, this.coreOpacity * this.opacityAndSizeModifier, 15728640);
        }

        var glowColor = getRenderedGlowColor(context, ticks, partialTick);
        var r = glowColor.getRed() / 255F;
        var g = glowColor.getGreen() / 255F;
        var b = glowColor.getBlue() / 255F;

        if (this.glowOpacity > 0F) {
            for (int i = 0; i < this.bloom + 1; i++) {
                RenderUtil.renderFilledBox(poseStack, consumer, box.inflate(i * 0.5F * 0.0625F), r, g, b, (1F / i / 2) * this.glowOpacity * this.opacityAndSizeModifier, 15728640);
            }
        }

        poseStack.popPose();
    }

    private Color getRenderedGlowColor(DataContext context, int ticks, float partialTick) {
        if (this.rainbow > 0F) {
            int rate = Math.max((int) (25 * (1F - this.rainbow)), 1);
            int j = ticks / rate;
            int k = DyeColor.values().length;
            int l = j % k;
            int m = (j + 1) % k;
            float f = ((float) (ticks % rate) + partialTick) / (float) rate;
            float[] fs = Sheep.getColorArray(DyeColor.byId(l));
            float[] gs = Sheep.getColorArray(DyeColor.byId(m));
            return new Color(fs[0] * (1.0F - f) + gs[0] * f, fs[1] * (1.0F - f) + gs[1] * f, fs[2] * (1.0F - f) + gs[2] * f);
        } else {
            return this.glowColor.getColor(context);
        }
    }

    public static LaserRenderer fromJson(JsonObject json) {
        return fromJson(json, 2);
    }

    public static LaserRenderer fromJson(JsonObject json, int defaultBloom) {
        var laser = new LaserRenderer(
                DynamicColor.getFromJson(json, "glow_color", DynamicColor.WHITE),
                DynamicColor.getFromJson(json, "core_color", DynamicColor.WHITE))
                .enableRainbow(GsonUtil.getAsBooleanFloat(json, "rainbow", 0F))
                .opacity(GsonUtil.getAsFloatRanged(json, "glow_opacity", 0F, 1F, 1F),
                        GsonUtil.getAsFloatRanged(json, "core_opacity", 0F, 1F, 1F))
                .bloom(GsonUtil.getAsIntRanged(json, "bloom", 0, 10, defaultBloom))
                .size(parseSize(json, "size", new Vector2f(1, 1)).mul(1F / 16F))
                .length(GsonUtil.getAsFloatMin(json, "length", 0F, 1F) / 16F)
                .normalTransparency(GsonHelper.getAsBoolean(json, "normal_transparency", false))
                .rotate(GsonUtil.getAsFloatMin(json, "rotation", 0F, 0F))
                .rotationSpeed(GsonUtil.getAsFloatMin(json, "rotation_speed", 0.0F, 0F));

        if (json.has("thickness")) {
            laser.size((GsonUtil.getAsFloatMin(json, "thickness", 0, 0.05F) * 20) / 16);
        }

        return laser;
    }

    private static Vector2f parseSize(JsonObject json, String memberName, Vector2f fallback) {
        if (json.has(memberName)) {
            var el = json.get(memberName);

            if (el.isJsonPrimitive()) {
                var val = GsonHelper.convertToFloat(el, memberName);
                return new Vector2f(val, val);
            } else if (el.isJsonArray()) {
                var array = el.getAsJsonArray();

                if (array.size() != 2) {
                    throw new JsonSyntaxException("Size must be an array of 2 numbers");
                }

                return new Vector2f(array.get(0).getAsFloat(), array.get(1).getAsFloat());
            } else {
                throw new JsonSyntaxException(memberName + " must be a simple number or an array of 2");
            }
        } else {
            return fallback;
        }
    }

    public static void generateDocumentation(JsonDocumentationBuilder builder, int defaultBloom, boolean withLength) {
        builder.addProperty("glow_color", Color.class)
                .description("Color of the laser glow").fallback(Color.WHITE).exampleJson(new JsonPrimitive("#ffffff"));
        builder.addProperty("core_color", Color.class)
                .description("Color of the inner core").fallback(Color.WHITE).exampleJson(new JsonPrimitive("#ffffff"));
        builder.addProperty("rainbow", Boolean.class)
                .description("If enabled, the glow will have a rainbow effect. Can also be defined as a float to determine the speed").fallback(false).exampleJson(new JsonPrimitive(false));
        builder.addProperty("glow_opacity", Float.class)
                .description("Opacity for the laser glow").fallback(1F).exampleJson(new JsonPrimitive(1F));
        builder.addProperty("core_opacity", Float.class)
                .description("Opacity for the inner core").fallback(1F).exampleJson(new JsonPrimitive(1F));
        builder.addProperty("bloom", Integer.class)
                .description("Describes how many stages of \"bloom\" is visible as the glow").fallback(defaultBloom).exampleJson(new JsonPrimitive(defaultBloom));
        builder.addProperty("size", Vector2f.class)
                .description("Size of the laser. Can either be defined as a simple float (e.g. 1.0), or 2-dimensional using an array (e.g. [1.0, 2.0])").fallback(new Vector2f(1, 1), "[1.0, 1.0]").exampleJson(new JsonPrimitive(1));

        if (withLength) {
            builder.addProperty("length", Float.class)
                    .description("Length of the laser").fallback(1F).exampleJson(new JsonPrimitive(1));
        }

        builder.addProperty("normal_transparency", Boolean.class)
                .description("You only ever really need to set it to true if you intend to make the core or glow black").fallback(false).exampleJson(new JsonPrimitive(false));

        builder.addProperty("rotation", Float.class)
                .description("Rotation of the laser").fallback(0F).exampleJson(new JsonPrimitive(0F));
        builder.addProperty("rotation_speed", Float.class)
                .description("Speed at which the laser rotates").fallback(0F).exampleJson(new JsonPrimitive(0F));
    }
}
