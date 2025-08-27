package net.threetag.palladium.client.renderer;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.dynamictexture.DynamicTextureManager;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.network.SyncAbilityStateMessage;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Function;

public abstract class DynamicColor {

    public static final DynamicColor WHITE = staticColor(Color.WHITE);

    public abstract Color getColor(DataContext context, @Nullable Function<String, String> stringConverter);

    public Color getColor(DataContext context) {
        return this.getColor(context, null);
    }

    public static DynamicColor staticColor(Color color) {
        return new Static(color);
    }

    public static DynamicColor variable(ITextureVariable variable) {
        return new Variable(variable);
    }

    public static DynamicColor stringWithVariables(String raw) {
        return new StringWithVariables(raw);
    }

    public static DynamicColor segmented(ColorSegment red, ColorSegment green, ColorSegment blue) {
        return new Segmented(red, green, blue);
    }

    public static DynamicColor segmented(ColorSegment red, ColorSegment green, ColorSegment blue, ColorSegment alpha) {
        return new Segmented(red, green, blue, alpha);
    }

    public static DynamicColor getFromJson(JsonObject json, String memberName) {
        if (!GsonHelper.isValidNode(json, memberName)) {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an color");
        } else {
            var el = json.get(memberName);
            return fromJson(el);
        }
    }

    public static DynamicColor getFromJson(JsonObject json, String memberName, DynamicColor fallback) {
        if (!GsonHelper.isValidNode(json, memberName)) {
            return fallback;
        } else {
            var el = json.get(memberName);
            return fromJson(el);
        }
    }

    public static DynamicColor fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsString().startsWith("#")) {
                return stringWithVariables(element.getAsString());
            } else {
                return staticColor(GsonUtil.convertToColor(element));
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();

            if (array.size() == 3) {
                return segmented(
                        ColorSegment.fromJson(array.get(0)),
                        ColorSegment.fromJson(array.get(1)),
                        ColorSegment.fromJson(array.get(2))
                );
            } else if (array.size() == 4) {
                return segmented(
                        ColorSegment.fromJson(array.get(0)),
                        ColorSegment.fromJson(array.get(1)),
                        ColorSegment.fromJson(array.get(2)),
                        ColorSegment.fromJson(array.get(3))
                );
            } else {
                throw new JsonParseException("Color array must either have 3 (RGB) or 4 (RGBA) segments");
            }
        } else if (element.isJsonObject()) {
            var json = element.getAsJsonObject();
            var serializer = DynamicTextureManager.getTextureVariableSerializer(GsonUtil.getAsResourceLocation(json, "type"));

            if (serializer != null) {
                return variable(serializer.parse(json));
            } else {
                throw new JsonParseException("Unknown texture variable serializer type " + GsonUtil.getAsResourceLocation(json, "type"));
            }
        } else {
            throw new JsonParseException("Color must either be defined as RGB-string, a variable, or array of integers/variables");
        }
    }

    private static class Static extends DynamicColor {

        private final Color color;

        public Static(Color color) {
            this.color = color;
        }

        @Override
        public Color getColor(DataContext context, @Nullable Function<String, String> stringConverter) {
            return this.color;
        }
    }

    private static class Variable extends DynamicColor {

        private final ITextureVariable variable;

        private Variable(ITextureVariable variable) {
            this.variable = variable;
        }

        @Override
        public Color getColor(DataContext context, @Nullable Function<String, String> stringConverter) {
            Object value = this.variable.get(context);
            if (value instanceof Number number) {
                return new Color(number.intValue());
            } else if (value instanceof String s) {
                return Color.decode(s.startsWith("#") ? s : "#" + s);
            }
            return Color.WHITE;
        }
    }

    private static class StringWithVariables extends DynamicColor {

        private final String raw;
        private Color parsed;

        private StringWithVariables(String raw) {
            this.raw = raw;

            if (raw.lastIndexOf("#") > 0) {
                this.parsed = null;
            } else {
                try {
                    this.parsed = Color.decode(raw);
                } catch (Exception e) {
                    AddonPackLog.error(e.getMessage());
                    this.parsed = null;
                }
            }
        }

        @Override
        public Color getColor(DataContext context, @Nullable Function<String, String> stringConverter) {
            if (this.parsed != null) {
                return this.parsed;
            }

            var raw = stringConverter != null ? stringConverter.apply(this.raw) : this.raw;
            var color = Color.WHITE;

            try {
                color = Color.decode(raw);
            } catch (Exception e) {
                AddonPackLog.error(e.getMessage());
            }

            return color;
        }
    }

    private static class Segmented extends DynamicColor {

        private final ColorSegment red, green, blue, alpha;

        private Segmented(ColorSegment red, ColorSegment green, ColorSegment blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = null;
        }

        private Segmented(ColorSegment red, ColorSegment green, ColorSegment blue, ColorSegment alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        @Override
        public Color getColor(DataContext context, @Nullable Function<String, String> stringConverter) {
            if (this.alpha != null) {
                return new Color(
                        this.red.getValue(context),
                        this.green.getValue(context),
                        this.blue.getValue(context),
                        this.alpha.getValue(context)
                );
            }

            return new Color(
                    this.red.getValue(context),
                    this.green.getValue(context),
                    this.blue.getValue(context)
            );
        }
    }

    public static abstract class ColorSegment {

        public static ColorSegment staticValue(int value) {
            return new Static(value);
        }

        public static ColorSegment variable(ITextureVariable variable) {
            return new Variable(variable);
        }

        public static ColorSegment fromJson(JsonElement element) {
            if (element.isJsonPrimitive()) {
                var number = element.getAsNumber();
                if (number instanceof Float) {
                    return staticValue((int) (number.floatValue() * 255F));
                } else if (number instanceof Double) {
                    return staticValue((int) (number.doubleValue() * 255F));
                } else {
                    return staticValue(number.intValue());
                }
            } else if (element.isJsonObject()) {
                var json = element.getAsJsonObject();
                var serializer = DynamicTextureManager.getTextureVariableSerializer(GsonUtil.getAsResourceLocation(json, "type"));

                if (serializer != null) {
                    return variable(serializer.parse(json));
                } else {
                    throw new JsonParseException("Unknown texture variable serializer type " + GsonUtil.getAsResourceLocation(json, "type"));
                }
            }

            throw new JsonParseException("Color segment must be number or texture variable");
        }

        public abstract int getValue(DataContext context);

        private static class Static extends ColorSegment {

            private final int value;

            private Static(int value) {
                this.value = value;
            }

            @Override
            public int getValue(DataContext context) {
                return this.value;
            }
        }

        private static class Variable extends ColorSegment {

            private final ITextureVariable variable;

            private Variable(ITextureVariable variable) {
                this.variable = variable;
            }

            @Override
            public int getValue(DataContext context) {
                Object value = this.variable.get(context);
                if (value instanceof Number number) {
                    return Mth.clamp(number.intValue(), 0, 255);
                }
                return 0;
            }
        }
    }

}
