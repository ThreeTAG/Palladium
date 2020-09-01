package net.threetag.threecore.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.fluid.FluidIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TCFluidUtil {

    public static FluidStack deserializeFluidStack(JsonObject jsonObject) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, "fluid")));
        if (fluid == null)
            throw new JsonSyntaxException("Unknown fluid '" + JSONUtils.getString(jsonObject, "fluid") + "'");
        return new FluidStack(fluid, JSONUtils.getInt(jsonObject, "amount"));
    }

    public static JsonObject serializeFluidStack(FluidStack stack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
        jsonObject.addProperty("amount", stack.getAmount());
        return jsonObject;
    }

    public static FluidActionResult interactWithFluidHandler(ItemStack stack, IFluidHandler fluidHandler, IItemHandler itemHandler, @Nullable PlayerEntity player) {
        if (stack.isEmpty() || fluidHandler == null || itemHandler == null) {
            return FluidActionResult.FAILURE;
        }

        FluidActionResult fillResult = FluidUtil.tryFillContainerAndStow(stack, fluidHandler, itemHandler, Integer.MAX_VALUE, player, true);
        if (fillResult.isSuccess()) {
            return fillResult;
        } else {
            return FluidUtil.tryEmptyContainerAndStow(stack, fluidHandler, itemHandler, Integer.MAX_VALUE, player, true);
        }
    }

    public static FluidActionResult transferFluidFromTankToItem(ItemStack stack, IFluidHandler fluidHandler, IItemHandler itemHandler, @Nullable PlayerEntity player) {
        if (stack.isEmpty() || fluidHandler == null || itemHandler == null) {
            return FluidActionResult.FAILURE;
        }

        return FluidUtil.tryFillContainerAndStow(stack, fluidHandler, itemHandler, Integer.MAX_VALUE, player, true);
    }

    public static FluidActionResult transferFluidFromItemToTank(ItemStack stack, IFluidHandler fluidHandler, IItemHandler itemHandler, @Nullable PlayerEntity player) {
        if (stack.isEmpty() || fluidHandler == null || itemHandler == null) {
            return FluidActionResult.FAILURE;
        }

        return FluidUtil.tryEmptyContainerAndStow(stack, fluidHandler, itemHandler, Integer.MAX_VALUE, player, true);
    }

    public static boolean drainIngredient(IFluidTank fluidTank, FluidIngredient ingredient, IFluidHandler.FluidAction action) {
        for (FluidStack fluidStack : ingredient.getFluids()) {
            if (fluidTank.getFluid().containsFluid(fluidStack)) {
                fluidTank.drain(fluidStack, action);
                return true;
            }
        }

        return false;
    }

    public static SoundEvent getSound(FluidStack fluidStack, boolean fill) {
        SoundEvent soundEvent = fill ? fluidStack.getFluid().getAttributes().getFillSound(fluidStack) : fluidStack.getFluid().getAttributes().getEmptySound(fluidStack);
        if (fluidStack.getFluid().isEquivalentTo(Fluids.LAVA))
            return fill ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
        return soundEvent != null ? soundEvent : (fill ? SoundEvents.ITEM_BUCKET_FILL : SoundEvents.ITEM_BUCKET_EMPTY);
    }

    @OnlyIn(Dist.CLIENT)
    public static String getFormattedFluidInfo(int amount, int capacity) {
        return I18n.format("threecore.util.fluid_display", amount, "mB");
    }

    @OnlyIn(Dist.CLIENT)
    public static List<IReorderingProcessor> getFormattedFluidInfo(FluidTank fluidTank) {
        List<IReorderingProcessor> list = new ArrayList<>();
        list.add(fluidTank.getFluid().getDisplayName().func_241878_f());
        list.add(new TranslationTextComponent("threecore.util.fluid_tank_display", fluidTank.getFluidAmount(), fluidTank.getCapacity(), "mB").mergeStyle(TextFormatting.GRAY).func_241878_f());
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    public static List<IReorderingProcessor> getFormattedFluidInfo(FluidStack fluidStack, int capacity) {
        List<IReorderingProcessor> list = new ArrayList<>();
        list.add(fluidStack.getDisplayName().func_241878_f());
        list.add(new TranslationTextComponent("threecore.util.fluid_tank_display", fluidStack.getAmount(), capacity, "mB").mergeStyle(TextFormatting.GRAY).func_241878_f());
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawTooltip(FluidTank fluidTank, MatrixStack stack, ContainerScreen gui, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            gui.renderTooltip(stack, getFormattedFluidInfo(fluidTank), mouseX + 10, mouseY);
        }
    }

}
