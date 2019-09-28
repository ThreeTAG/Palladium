package net.threetag.threecore.util.fluid;

import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {

    private final FluidStack[] matching;
    private final Tag<Fluid> tag;

    public FluidIngredient(Tag<Fluid> tag, int amount) {
        this.tag = tag;
        Iterator<Fluid> iterator = tag.getAllElements().iterator();
        this.matching = new FluidStack[tag.getAllElements().size()];
        int i = 0;
        while (iterator.hasNext()) {
            matching[i] = new FluidStack(iterator.next(), amount);
            i++;
        }
    }

    public FluidIngredient(FluidStack... matching) {
        this.matching = matching;
        this.tag = null;
    }

    public FluidStack[] getFluids() {
        return this.matching;
    }

    public Tag<Fluid> getTag() {
        return tag;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        for (FluidStack stack : this.matching) {
            if (fluidStack.containsFluid(stack)) {
                return true;
            }
        }
        return false;
    }

    public static FluidIngredient read(PacketBuffer buffer) {
        int size = buffer.readInt();
        FluidStack[] fluids = new FluidStack[size];

        for (int i = 0; i < size; i++) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
            int amount = buffer.readInt();
            if (fluid != null) {
                fluids[i] = new FluidStack(fluid, amount);
            }
        }

        return new FluidIngredient(fluids);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeInt(this.matching.length);

        for (int i = 0; i < this.matching.length; i++) {
            buffer.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(this.matching[i].getFluid()));
            buffer.writeInt(this.matching[i].getAmount());
        }
    }

    public static FluidIngredient deserialize(JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();

                if (JSONUtils.hasField(jsonObject, "fluid") && JSONUtils.hasField(jsonObject, "tag"))
                    throw new JsonParseException("An ingredient entry is either a tag or a fluid, not both");

                if (JSONUtils.hasField(jsonObject, "fluid")) {
                    return new FluidIngredient(TCFluidUtil.deserializeFluidStack(jsonObject));
                } else if (JSONUtils.hasField(jsonObject, "tag")) {
                    ResourceLocation tag = new ResourceLocation(JSONUtils.getString(jsonObject, "tag"));
                    Tag<Fluid> fluidTag = FluidTags.getCollection().get(tag);
                    if (fluidTag == null) {
                        throw new JsonSyntaxException("Unknown fluid tag '" + tag + "'");
                    } else {
                        return new FluidIngredient(fluidTag, JSONUtils.getInt(jsonObject, "amount"));
                    }
                } else {
                    throw new JsonParseException("An ingredient entry needs either a tag or a fluid");
                }

            } else if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    throw new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must be defined");
                } else {
                    FluidStack[] fluidStacks = new FluidStack[jsonArray.size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        fluidStacks[0] = TCFluidUtil.deserializeFluidStack(jsonArray.get(i).getAsJsonObject());
                    }
                    return new FluidIngredient(fluidStacks);
                }
            } else {
                throw new JsonSyntaxException("Expected fluid to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Fluid cannot be null");
        }
    }
}
