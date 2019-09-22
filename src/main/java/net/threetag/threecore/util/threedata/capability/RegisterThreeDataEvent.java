package net.threetag.threecore.util.threedata.capability;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.threetag.threecore.util.threedata.ThreeData;

public class RegisterThreeDataEvent extends EntityEvent {

    private final IThreeData threeData;

    public RegisterThreeDataEvent(Entity entity, IThreeData threeData) {
        super(entity);
        this.threeData = threeData;
    }

    public IThreeData getThreeData() {
        return threeData;
    }

    public <T> void register(ThreeData<T> data, T defaultValue) {
        getThreeData().register(data, defaultValue);
    }

}
