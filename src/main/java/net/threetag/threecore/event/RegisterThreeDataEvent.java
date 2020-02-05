package net.threetag.threecore.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;

public class RegisterThreeDataEvent extends EntityEvent {

    private final IThreeDataHolder threeData;

    public RegisterThreeDataEvent(Entity entity, IThreeDataHolder threeData) {
        super(entity);
        this.threeData = threeData;
    }

    public IThreeDataHolder getThreeData() {
        return threeData;
    }

    public <T> void register(ThreeData<T> data, T defaultValue) {
        getThreeData().register(data, defaultValue);
    }

}
