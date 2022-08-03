package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AgeableListModel.class)
public interface AgeableListModelInvoker {

    @Invoker("bodyParts")
    Iterable<ModelPart> invokeBodyParts();

    @Invoker("headParts")
    Iterable<ModelPart> invokeHeadParts();

}
